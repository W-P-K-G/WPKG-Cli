package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unused")
public class ClientManager implements ActionListener {
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
        unjoinButton.addActionListener(this);
        refreshButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        var source = e.getSource();

        if (source == unjoinButton)
        {
            UDPClient.sendString("/unjoin");
            Main.ClientManager.clientManager.setVisible(false);
            Main.WPKGManager.wpkgManager.setVisible(true);
            Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
            UDPClient.receiveString();
        }
        if (source == refreshButton)
        {
            Tools.refreshCommandslist(commandsModel);
        }

    }
}
// TODO: ClientManager