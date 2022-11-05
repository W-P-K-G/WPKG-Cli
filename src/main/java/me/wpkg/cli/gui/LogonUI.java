package me.wpkg.cli.gui;

import static me.wpkg.cli.utils.JSONParser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import javax.swing.*;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.net.Protocol;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Globals;
import me.wpkg.cli.utils.JSONParser;
import me.wpkg.cli.utils.Tools;

public class LogonUI {
    public JPanel logonUI;
    private JPasswordField TokenField;
    private JButton Accept;
    public JComboBox<String> IPField;
    private JComboBox<Protocol> protocolBox;

    DefaultComboBoxModel<String> udpModel,tcpModel;

    // Buttons Actions
    public LogonUI() {
        ProgressDialog dialog = new ProgressDialog("Getting servers ip...");
        dialog.start(d -> refreshServerList(IPField));

        Accept.addActionListener(ActionEvent -> acceptAction());
        protocolBox.setModel(new DefaultComboBoxModel<>(Protocol.values()));
        protocolBox.addActionListener(ActionEvent -> protocolComboAction());

        try {
            TokenField.setText(Files.readString(Globals.passwordFile.toPath()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Main.frame, "Error reading file:" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void protocolComboAction()
    {
        switch ((Protocol) Objects.requireNonNull(protocolBox.getSelectedItem()))
        {
            case TCP -> IPField.setModel(tcpModel);
            case UDP -> IPField.setModel(udpModel);
        }
    }

    public void refreshServerList(JComboBox<String> IPField) {
        JSONParser.AddressJSON address = getAddress(Tools.readStringFromURL(Globals.jsonURL + "Addreses.json"));

        udpModel = new DefaultComboBoxModel<>();
        tcpModel = new DefaultComboBoxModel<>();

        for (var addr : address.uAddresses)
            udpModel.addElement(addr.ip + ":" + addr.port);
        for (var addr : address.tAddresses)
            tcpModel.addElement(addr.ip + ":" + addr.port);

        IPField.setModel(tcpModel);
    }

    public void acceptAction() {

        ProgressDialog progressDialog = new ProgressDialog("Connecting...");
        progressDialog.start(dialog -> {
            try {
                String ip = (String) IPField.getSelectedItem();
                if (ip != null) {
                    String[] portAddress = ip.split(":");

                    Client.setProtocol((Protocol) protocolBox.getSelectedItem());
                    Client.connect(portAddress[0], Integer.parseInt(portAddress[1]));

                    Files.writeString(Globals.passwordFile.toPath(), new String(TokenField.getPassword()));

                    switch (Client.sendCommand("/registeradmin " + new String(TokenField.getPassword()))) {
                        case "[REGISTER_SUCCESS]" -> SwingUtilities
                                .invokeLater(() -> StateManager.changeState(State.CLIENT_LIST));
                        case "[WRONG_PASSWORD]" -> {
                            dialog.dispose();
                            JOptionPane.showMessageDialog(Main.frame, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        default -> JOptionPane.showMessageDialog(Main.frame, "Registering error", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    Main.WPKGManager.refreshAction();
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(dialog::dispose);
                JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
