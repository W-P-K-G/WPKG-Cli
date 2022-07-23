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
}
