package com.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.wpkg.cli.actions.ActionListeners;
import com.wpkg.cli.networking.UDPClient;

import javax.swing.*;

public class MainWindow {
    private JPanel WPKG;
    private JPasswordField TokenField;
    public JTextField IPField;
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
    private JButton logOffButton;
    private JList list1;
    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JButton cryptoManager;
    private JProgressBar gpuBar;

    public MainWindow() {
        Accept.addActionListener(ActionListeners::acceptAction);
        logOffButton.addActionListener(ActionListeners::logoffAction);
        exitButton.addActionListener(ActionListeners::logoffAction);
        refreshButton.addActionListener(ActionListeners::refreshAction);

        ActionListeners.main = this;
        UDPClient.main = this;
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame("WPKG-CLI");
        frame.setSize(765, 445);
        frame.setContentPane(new MainWindow().WPKG);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ActionListeners.client == null) return;

            ActionListeners.client.logOff();
        }, "Shutdown-thread"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
