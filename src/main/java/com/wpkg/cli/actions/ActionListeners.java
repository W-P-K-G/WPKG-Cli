package com.wpkg.cli.actions;

import com.wpkg.cli.networking.*;
import com.wpkg.cli.main.*;
import com.wpkg.cli.utilities.JSONParser;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionListeners {
    public static UDPClient client;
    public static MainWindow main;

    private static DefaultListModel<String> ClientListModel = new DefaultListModel<String>();
    public static void acceptAction(ActionEvent actionEvent) {
        main.LogonUI.setVisible(false);
        main.WPKGManager.setVisible(true);

        client = new UDPClient();
        client.sendString("register");
        client.receiveString();
        Tools.refreshClientlist(ClientListModel, client);
        main.ClientList.setModel(ClientListModel);
    }

    public static void logoffAction(ActionEvent actionEvent){
        client.logOff();
        main.WPKGManager.setVisible(false);
        main.LogonUI.setVisible(true);
    }

    public static void refreshAction(ActionEvent actionEvent){
        Tools.refreshClientlist(ClientListModel, client);
    }
    public  static void killAction(ActionEvent actionEvent){
        client.sendString("/close "+ Tools.list.clients[main.ClientList.getSelectedIndex()].id);
        client.receiveString();
        Tools.refreshClientlist(ClientListModel, client);
    }
}
