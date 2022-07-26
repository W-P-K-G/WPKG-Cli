package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;

@SuppressWarnings("unused")
public class ClientManager {
    public JPanel clientManager;
    private JButton unjoinButton;
    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar gpuBar;
    private JButton cryptoManager;
    public JList commandList;
    private JButton executeButton;
    private JButton refreshButton;

    private DefaultListModel<String> commandsModel = new DefaultListModel<>();

    public ClientManager()
    {
        unjoinButton.addActionListener(ActionEvent -> unjoinAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
    }
    public void unjoinAction(){
        UDPClient.sendString("/unjoin");
        Main.ClientManager.clientManager.setVisible(false);
        Main.WPKGManager.wpkgManager.setVisible(true);
        Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
        UDPClient.receiveString();
    }
    public void refreshAction(){
        Tools.refreshCommandsList(commandsModel);
    }
}
// TODO: ClientManager