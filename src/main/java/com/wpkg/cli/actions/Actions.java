package com.wpkg.cli.actions;

import com.wpkg.cli.networking.*;
import com.wpkg.cli.main.*;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;

public class Actions {
    public static UDPClient client;
    private static final DefaultListModel<String> clientModel = new DefaultListModel<>();
    private static final DefaultListModel<String> commandsModel = new DefaultListModel<>();

    
    /**
     * Login Manager Actions:
     */
    public static void acceptAction() {
        main.LogonUI.logonUI.setVisible(false);
        main.WPKGManager.wpkgManager.setVisible(true);
        main.frame.setContentPane(main.WPKGManager.wpkgManager);

        client = new UDPClient();
        client.sendString("register");
        client.receiveString();
        Tools.refreshClientlist(clientModel, client);
        main.WPKGManager.ClientList.setModel(clientModel);
    }


    /**
     * WPKGManager Actions:
     */
    public static void logoffAction(){
        client.logOff();
        main.WPKGManager.wpkgManager.setVisible(false);
        main.LogonUI.logonUI.setVisible(true);
        main.frame.setContentPane(main.LogonUI.logonUI);
    }

    public static void refreshClientAction(){
        Tools.refreshClientlist(clientModel, client);
    }

    public static void killAction(){
        client.sendString("/close "+ Tools.clientJSON.clients[main.WPKGManager.ClientList.getSelectedIndex()].id);
        client.receiveString();
        Tools.refreshClientlist(clientModel, client);
    }

    public static void joinAction(){
        client.sendString("/join "+ Tools.clientJSON.clients[main.WPKGManager.ClientList.getSelectedIndex()].id);
        main.ClientManager.clientManager.setVisible(true);
        main.WPKGManager.wpkgManager.setVisible(false);
        main.frame.setContentPane(main.ClientManager.clientManager);
        client.receiveString();
    }


    /**
     * ClientManager Actions:
     */
    public static void unjoinAction(){
        client.sendString("/unjoin");
        main.ClientManager.clientManager.setVisible(false);
        main.WPKGManager.wpkgManager.setVisible(true);
        main.frame.setContentPane(main.WPKGManager.wpkgManager);
        client.receiveString();
    }
    public static void refreshCommandsList(){
        Tools.refreshCommandslist(commandsModel, client);
    }
}
