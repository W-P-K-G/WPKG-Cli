package com.wpkg.cli.gui;

import com.wpkg.cli.actions.Actions;

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

    public ClientManager() {
        unjoinButton.addActionListener(actionEvent -> Actions.unjoinAction());
        refreshButton.addActionListener(actionEvent -> Actions.refreshCommandsList());
    }
}
// TODO: ClientManager
