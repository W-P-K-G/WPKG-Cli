package com.wpkg.cli.utilities;
import com.wpkg.cli.networking.ClientJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

public class JSONParser {
    public static ClientJSON getClientList(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        ClientJSON clientJSON;
        try {
            clientJSON = objectMapper.readValue(json, ClientJSON.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return clientJSON;
    }
    public static void setList(DefaultListModel<String> listModel, String json){
        ClientJSON list = getClientList(json);
        for(int i = 0; i < list.clients.length; i++){
            listModel.addElement("\uD83D\uDDA5️     \uD83D\uDCB3 ID: "+list.clients[i].id + "        "
                                                +" \uD83D\uDCD6 NAME: "+list.clients[i].name);
        }
    }
}
