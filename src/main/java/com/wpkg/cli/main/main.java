package com.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.wpkg.cli.actions.Actions;
import com.wpkg.cli.gui.LogonUI;
import com.wpkg.cli.gui.WPKGManager;

import javax.swing.*;

public class main {

    public static JFrame frame = new JFrame("WPKG-CLI");
    public static LogonUI LogonUI;
    public static WPKGManager WPKGManager;

    @SuppressWarnings("MethodNameSameAsClassName")
    public static void main(String[] args){
        // Dark-mode on
        FlatDarkLaf.setup();

        // Declare Windows
        LogonUI = new LogonUI();
        WPKGManager = new WPKGManager();

        // Disconnect on close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(Actions.client == null) return;

            Actions.client.logOff();
        }, "Shutdown-thread"));

        // Setting frame settings
        frame.setSize(765, 445);
        frame.setContentPane(LogonUI.logonUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}