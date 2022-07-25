package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unused")
public class WPKGManager implements ActionListener {
    public JPanel wpkgManager;
    public JList<String> ClientList;
    private JPanel Buttons;
    private JButton selectButton;
    private JButton infoButton;
    private JButton killButton;
    private JButton refreshButton;
    private JButton logOffButton;
    private JLabel WPKGLabel;

    public DefaultListModel<String> clientModel = new DefaultListModel<>();

    // Buttons Actions
    public WPKGManager() {

        logOffButton.addActionListener(this);
        refreshButton.addActionListener(this);
        killButton.addActionListener(this);
        selectButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        var source = actionEvent.getSource();

        if (source == logOffButton)
        {
            UDPClient.logOff();
            wpkgManager.setVisible(false);
            Main.LogonUI.logonUI.setVisible(true);
            Main.frame.setContentPane(Main.LogonUI.logonUI);
        }
        if (source == refreshButton)
        {
            Tools.requestClientList(clientModel);
        }
        if (source == killButton)
        {
            UDPClient.sendString("/close "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
            UDPClient.receiveString();
            Tools.requestClientList(clientModel);
        }
        if (source == selectButton)
        {
            UDPClient.sendString("/join "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
            Main.ClientManager.clientManager.setVisible(true);
            Main.WPKGManager.wpkgManager.setVisible(false);
            Main.frame.setContentPane(Main.ClientManager.clientManager);
            UDPClient.receiveString();
        }
    }
}
