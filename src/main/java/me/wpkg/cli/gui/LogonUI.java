package me.wpkg.cli.gui;

import me.wpkg.cli.networking.UDPClient;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.io.IOException;

public class LogonUI
{
    public JPanel logonUI;
    private JPasswordField TokenField;
    private JButton Accept;
    public JComboBox<String> IPField;

    // Buttons Actions
    public LogonUI()
    {
        Tools.refreshServerList(IPField);

        Accept.addActionListener(ActionEvent -> acceptAction());

    }
    public void acceptAction()
    {

        ProgressDialog progressDialog = new ProgressDialog("Connecting...");
        progressDialog.start(dialog -> {
            try
            {
                String ip = (String) IPField.getSelectedItem();
                if (ip != null)
                {
                    String[] portAddress = ip.split(":");
                    UDPClient.connect(portAddress[0],Integer.parseInt(portAddress[1]));
                    UDPClient.sendRegisterPing();

                    SwingUtilities.invokeLater(() -> StateManager.changeState(State.CLIENT_LIST));
                }
            }
            catch (IOException e)
            {
                SwingUtilities.invokeLater(dialog::dispose);
                JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}