package me.wpkg.cli.gui;

import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Globals;
import me.wpkg.cli.utils.JSONParser;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;

import static me.wpkg.cli.utils.JSONParser.*;

public class LogonUI
{
    public JPanel logonUI;
    private JPasswordField TokenField;
    private JButton Accept;
    public JComboBox<String> IPField;

    // Buttons Actions
    public LogonUI()
    {
        ProgressDialog dialog = new ProgressDialog("Getting servers ip...");
        dialog.start(d -> refreshServerList(IPField));

        Accept.addActionListener(ActionEvent -> acceptAction());

        try
        {
            TokenField.setText(Files.readString(Globals.passwordFile.toPath()));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Error reading file:" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshServerList(JComboBox<String> IPField)
    {
        JSONParser.AddressJSON address = getAddress(Tools.readStringFromURL(Globals.jsonURL + "Addreses.json"));

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (var addr : address.uAddresses)
            model.addElement(addr.ip + ":" + addr.port);

        IPField.setModel(model);
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
                    Client.connect(portAddress[0],Integer.parseInt(portAddress[1]));

                    Files.writeString(Globals.passwordFile.toPath(),new String(TokenField.getPassword()));

                    switch (Client.sendCommand("/registeradmin " + new String(TokenField.getPassword())))
                    {
                        case "[REGISTER_SUCCESS]" -> SwingUtilities.invokeLater(() -> StateManager.changeState(State.CLIENT_LIST));
                        case "[WRONG_PASSWORD]" -> {
                            dialog.dispose();
                            JOptionPane.showMessageDialog(Main.frame,"Wrong password","Error",JOptionPane.ERROR_MESSAGE);
                        }
                        default -> JOptionPane.showMessageDialog(Main.frame,"Registering error","Error",JOptionPane.ERROR_MESSAGE);
                    }
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
