package com.wpkg.cli.utilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {

    public static class ClientJSON {
        public clients[] clients;
        public static class clients {
            public String name;
            public int id;
            public boolean joined;
        }
    }
    public static class CommandsJSON{
        public commands[] commands;
        public static class commands{
            public String name;
            public String help;
            public String args;
        }
    }

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
    public static CommandsJSON getCommandsList(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        CommandsJSON commandsJSON;
        try {
            commandsJSON = objectMapper.readValue(json, CommandsJSON.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return  commandsJSON;
    }
}
// TODO: dialogi w try catch