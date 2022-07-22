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
            clientJSON = objectMapper.readValue("""
                    {
                        "clients":[
                            {
                                "name":"RAT: 0",
                                "id":0
                            },
                            {
                                "name":"RAT: 1",
                                "id":1
                            }
                        ]
                    }""", ClientJSON.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return clientJSON;
    }
    public static void setList(DefaultListModel<String> listModel, String json){
        ClientJSON list = getClientList(json);
        for(int i = 0; i < list.clients.length; i++){
            listModel.addElement("\uD83D\uDDA5️ ID: "+list.clients[i].id+" NAME: "+list.clients[i].name);
        }
    }
}
