package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.state.State;
import com.wpkg.cli.state.StateManager;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.io.IOException;

@SuppressWarnings("unused")
public class LogonUI
{
    public JPanel logonUI;
    private JLabel WPKGLabel;
    private JPasswordField TokenField;
    private JButton Accept;
    public JComboBox IPField;

    // Buttons Actions
    public LogonUI()
    {
        Tools.refreshServerList(IPField);

        Accept.addActionListener(ActionEvent -> acceptAction());

    }
    public void acceptAction()
    {
        try {
            String[] portAddress = Main.LogonUI.IPField.getSelectedItem().toString().split(":");
            UDPClient.connect(portAddress[0],Integer.parseInt(portAddress[1]));
            UDPClient.sendRegisterPing();

            StateManager.changeState(State.CLIENT_LIST);

        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
