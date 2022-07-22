package com.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow {
    private JPanel WPKG;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JButton Accept;
    private JPanel LogonUI;
    private JButton refreshButton;
    private JPanel WPKGManager;
    private JList list1;

    public MainWindow() {
        Accept.addActionListener(actionEvent -> {
            LogonUI.setVisible(false);
            LogonUI.setEnabled(false);
            WPKGManager.setVisible(true);
            WPKGManager.setEnabled(true);
        });
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
