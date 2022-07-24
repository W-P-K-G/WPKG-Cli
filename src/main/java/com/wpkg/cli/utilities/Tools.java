package com.wpkg.cli.utilities;

import com.wpkg.cli.networking.UDPClient;

import javax.swing.*;

public class Tools {
    public static JSONParser.ClientJSON clientJSON;
    public static JSONParser.CommandsJSON commandsJSON;
    public static void refreshClientlist(DefaultListModel<String> ClientListModel, UDPClient client){
        client.sendString("/rat-list");
        ClientListModel.clear();
        clientJSON = JSONParser.getClientList(client.receiveString());
        for(int i = 0; i < clientJSON.clients.length; i++){
            ClientListModel.add(i,"\uD83D\uDDA5️     \uD83D\uDCB3 ID: "+ clientJSON.clients[i].id + "        "
                    +" \uD83D\uDCD6 NAME: "+ clientJSON.clients[i].name);
        }
    }
    public static void refreshCommandslist(DefaultListModel<String> ClientListModel, UDPClient client){
        client.sendString("command-list");
        ClientListModel.clear();
        commandsJSON = JSONParser.getCommandsList(client.receiveString());
        for (int i = 0; i < commandsJSON.commands.length; i++){
            ClientListModel.add(i, commandsJSON.commands[i].name); // TODO: Finish refreshCommandslist()
        }
    }
}
