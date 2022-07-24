package com.wpkg.cli.gui;

import com.wpkg.cli.actions.Actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

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

    // Buttons Actions
    public WPKGManager() {
        logOffButton.addActionListener(actionEvent -> Actions.logoffAction());
        refreshButton.addActionListener(actionEvent -> Actions.refreshAction());
        killButton.addActionListener(ActionEvent -> Actions.killAction());
        selectButton.addActionListener(ActionEvent -> Actions.joinAction());
    }
}
