package com.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.wpkg.cli.gui.ClientManager;
import com.wpkg.cli.gui.CryptoManagerGPU;
import com.wpkg.cli.gui.LogonUI;
import com.wpkg.cli.gui.WPKGManager;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;


public class Main {

    public static JFrame frame = new JFrame("WPKG-CLI");
    public static LogonUI LogonUI;
    public static WPKGManager WPKGManager;
    public static ClientManager ClientManager;
    public static CryptoManagerGPU CryptoManager;

    public static void main(String[] args)
    {
        // Dark-mode on
        FlatDarkLaf.setup();

        // Declare Windows
        WPKGManager = new WPKGManager();
        LogonUI = new LogonUI();
        Tools.refreshServerList(LogonUI.IPField);
        ClientManager = new ClientManager();
        CryptoManager = new CryptoManagerGPU();
        CryptoManager.cryptoComboBox.setModel(new DefaultComboBoxModel(CryptoManagerGPU.CryptoCurrencies.values()));

        // Disconnect on close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!UDPClient.isConnected()) return;

            UDPClient.logOff();
        }, "Shutdown-thread"));

        // Setting frame settings
        frame.setSize(765, 445);
        frame.setContentPane(CryptoManager.CryptoPanelGPU);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
