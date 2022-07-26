package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;

@SuppressWarnings("unused")
public class WPKGManager {
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

        logOffButton.addActionListener(ActionEvent -> logOffAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
        killButton.addActionListener(ActionEvent -> killAction());
        selectButton.addActionListener(ActionEvent -> selectAction());
    }

    public void logOffAction(){
        UDPClient.logOff();
        wpkgManager.setVisible(false);
        Main.LogonUI.logonUI.setVisible(true);
        Main.frame.setContentPane(Main.LogonUI.logonUI);
    }
    public void refreshAction(){
        Tools.requestClientList(clientModel);
    }
    public void killAction(){
        UDPClient.sendString("/close "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
        UDPClient.receiveString();
        Tools.requestClientList(clientModel);
    }
    public void selectAction(){
        UDPClient.sendString("/join "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
        Main.ClientManager.clientManager.setVisible(true);
        Main.WPKGManager.wpkgManager.setVisible(false);
        Main.frame.setContentPane(Main.ClientManager.clientManager);
        UDPClient.receiveString();
    }
}
