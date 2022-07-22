package com.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.wpkg.cli.actions.ActionListeners;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainWindow {
    private JPanel WPKG;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JButton Accept;
    public JPanel LogonUI;
    private JButton refreshButton;
    public JPanel WPKGManager;
    public JList<String> ClientList;
    private JButton selectButton;
    private JButton killButton;
    private JButton infoButton;
    private JLabel WPKGLabel;
    private JPanel Buttons;
    private JPanel ClientManager;
    private JButton exitButton;

    public MainWindow() {
        Accept.addActionListener(ActionListeners::acceptAction);

        ActionListeners.main = this;
    }

    public static void main(String[] args) {

        FlatDarkLaf.setup();
        JFrame frame = new JFrame("WPKG-CLI");
        frame.setSize(765,445);
        frame.setContentPane(new MainWindow().WPKG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
