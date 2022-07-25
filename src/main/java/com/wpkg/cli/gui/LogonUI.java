package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

@SuppressWarnings("unused")
public class LogonUI implements ActionListener
{
    public JPanel logonUI;
    private JLabel WPKGLabel;
    private JPasswordField TokenField;
    private JButton Accept;
    public JTextField IPField;

    public WPKGManager wpkgmanager;

    // Buttons Actions
    public LogonUI(WPKGManager manager)
    {
        wpkgmanager = manager;
        Accept.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        var source = actionEvent.getSource();

        if (source == Accept)
        {
            try {
                String[] portAddress = Main.LogonUI.IPField.getText().split(":");
                UDPClient.connect(portAddress[0],Integer.parseInt(portAddress[1]));
                UDPClient.sendRegisterPing();

                logonUI.setVisible(false);
                wpkgmanager.wpkgManager.setVisible(true);
                Tools.requestClientList(wpkgmanager.clientModel);
                wpkgmanager.ClientList.setModel(wpkgmanager.clientModel);

                Main.frame.setContentPane(wpkgmanager.wpkgManager);
            } catch (IOException e){
                JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
