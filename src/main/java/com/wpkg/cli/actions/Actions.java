package com.wpkg.cli.actions;

import com.wpkg.cli.networking.*;
import com.wpkg.cli.main.*;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Actions {
    public static UDPClient client;
    private static final DefaultListModel<String> ClientListModel = new DefaultListModel<>();
    public static void acceptAction() {

        try
        {
            String[] portAddress = main.LogonUI.IPField.getText().split(":");

            client = new UDPClient(portAddress[0],Integer.parseInt(portAddress[1]));

            client.sendRegisterPing();

            main.LogonUI.logonUI.setVisible(false);
            main.WPKGManager.wpkgManager.setVisible(true);
            main.frame.setContentPane(main.WPKGManager.wpkgManager);
            Tools.refreshClientlist(ClientListModel, client);
            main.WPKGManager.ClientList.setModel(ClientListModel);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,"Can't connect to server: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void logoffAction(){
        client.logOff();
        main.WPKGManager.wpkgManager.setVisible(false);
        main.LogonUI.logonUI.setVisible(true);
        main.frame.setContentPane(main.LogonUI.logonUI);
    }

    public static void refreshAction(){
        Tools.refreshClientlist(ClientListModel, client);
    }

    public static void killAction(){
        client.sendString("/close "+ Tools.list.clients[main.WPKGManager.ClientList.getSelectedIndex()].id);
        client.receiveString();
        Tools.refreshClientlist(ClientListModel, client);
    }

    public static void joinAction(){
        client.sendString("/join "+ Tools.list.clients[main.WPKGManager.ClientList.getSelectedIndex()].id);
        main.ClientManager.clientManager.setVisible(true);
        main.WPKGManager.wpkgManager.setVisible(false);
        main.frame.setContentPane(main.ClientManager.clientManager);
        client.receiveString();
    }

    public static void unjoinAction(){
        client.sendString("/unjoin");
        main.ClientManager.clientManager.setVisible(false);
        main.WPKGManager.wpkgManager.setVisible(true);
        main.frame.setContentPane(main.WPKGManager.wpkgManager);
        client.receiveString();
    }
}
