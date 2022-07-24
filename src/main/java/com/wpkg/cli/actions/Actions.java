package com.wpkg.cli.actions;

import com.wpkg.cli.main.main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.io.IOException;

public class Actions {
    public static UDPClient client;
    private static final DefaultListModel<String> clientModel = new DefaultListModel<>();
    private static final DefaultListModel<String> commandsModel = new DefaultListModel<>();


    /**
     * Login Manager Actions:
     */
    public static void acceptAction() {
        try {
            String[] portAddress = Main.LogonUI.IPField.getText().split(":");
            client = new UDPClient(portAddress[0],Integer.parseInt(portAddress[1]));
            client.sendRegisterPing();

            Main.LogonUI.logonUI.setVisible(false);
            Main.WPKGManager.wpkgManager.setVisible(true);
            Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
            Tools.refreshClientlist(clientModel, client);
            Main.WPKGManager.ClientList.setModel(clientModel);
        } catch (IOException e){
          JOptionPane.showMessageDialog(null, "Can't connect to server: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * WPKGManager Actions:
     */
    public static void logoffAction(){
        client.logOff();
        Main.WPKGManager.wpkgManager.setVisible(false);
        Main.LogonUI.logonUI.setVisible(true);
        Main.frame.setContentPane(Main.LogonUI.logonUI);
    }

    public static void refreshClientAction(){
        Tools.refreshClientlist(clientModel, client);
    }
    public static void killAction(){
        client.sendString("/close "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
        client.receiveString();
        Tools.refreshClientlist(clientModel, client);
    }
    public static void joinAction(){
        client.sendString("/join "+ Tools.clientJSON.clients[Main.WPKGManager.ClientList.getSelectedIndex()].id);
        Main.ClientManager.clientManager.setVisible(true);
        Main.WPKGManager.wpkgManager.setVisible(false);
        Main.frame.setContentPane(Main.ClientManager.clientManager);
        client.receiveString();
    }


    /**
     * ClientManager Actions:
     */
    public static void unjoinAction(){
        client.sendString("/unjoin");
        Main.ClientManager.clientManager.setVisible(false);
        Main.WPKGManager.wpkgManager.setVisible(true);
        Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
        client.receiveString();
    }
    public static void refreshCommandsList(){
        Tools.refreshCommandslist(commandsModel, client);
    }
    public static void executeAction(){
        client.sendString("/join " + Tools.commandsJSON.commands[main.ClientManager.commandList.getSelectedIndex()].name);
        client.receiveString(); //TODO: dokończyć to gówno
    }
}
