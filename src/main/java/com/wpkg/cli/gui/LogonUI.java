package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.state.State;
import com.wpkg.cli.state.StateManager;
import com.wpkg.cli.utilities.Tools;

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
        String token = String.valueOf(TokenField.getPassword());
        if (token.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Authtoken is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog("Connecting...");
        progressDialog.start(dialog -> {
            try
            {
                String ip = (String) IPField.getSelectedItem();
                if (ip != null)
                {
                    String[] portAddress = ip.split(":");
                    UDPClient.connect(portAddress[0],Integer.parseInt(portAddress[1]));
                    UDPClient.connectAndAuthorize(token);

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
