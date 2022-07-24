package com.wpkg.cli.gui;

import com.wpkg.cli.actions.Actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unused")
public class ClientManager {
    public JPanel clientManager;
    private JButton unjoinButton;
    private JList<String> list1;
    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar gpuBar;
    private JButton cryptoManager;

    public ClientManager() {
        unjoinButton.addActionListener(actionEvent -> Actions.unjoinAction());
    }
}
// TODO: ClientManager