package com.wpkg.cli.gui;

import com.wpkg.cli.actions.Actions;

import javax.swing.*;

@SuppressWarnings("unused")
public class LogonUI {
    public JPanel logonUI;
    private JLabel WPKGLabel;
    private JPasswordField TokenField;
    private JButton Accept;
    public JTextField IPField;

    // Buttons Actions
    public LogonUI() {
        Accept.addActionListener(actionEvent -> Actions.acceptAction());
    }
}
