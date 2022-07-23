package com.wpkg.cli.actions;

import com.wpkg.cli.networking.*;
import com.wpkg.cli.main.*;
import com.wpkg.cli.utilities.JSONParser;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionListeners {
    public static UDPClient client;
    public static MainWindow main;

    public static void acceptAction(ActionEvent actionEvent) {
        main.LogonUI.setVisible(false);
        main.WPKGManager.setVisible(true);
        DefaultListModel<String> ClientListModel = new DefaultListModel<String>();
        client = new UDPClient();
        client.sendString("register");
        client.sendString("/rat-list");
        JSONParser.setList(ClientListModel, client.receiveString());
        main.ClientList.setModel(ClientListModel);
    }
}
