package me.wpkg.cli.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser
{

    public static class ClientJSON {
        public clients[] clients;
        public static class clients {
            public String name;
            public int id;
            public boolean joined;
            public String version = "Unknown";
        }
    }

    public static class AddressJSON{
        public uAddresses[] uAddresses;
        public tAddresses[] tAddresses;
        public static class uAddresses{
            public String ip;
            public int port;
        }
        public static class tAddresses{
            public String ip;
            public int port;
        }
    }
    public static class walletJSON{
        public String coin;
        public String id;
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

    public static AddressJSON getAddress(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        AddressJSON addressJSON;
        try {
            addressJSON = objectMapper.readValue(json, AddressJSON.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return addressJSON;
    }
    public static walletJSON[] getWallet(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        walletJSON[] walletJSON;
        try {
            walletJSON = objectMapper.readValue(json, walletJSON[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return walletJSON;
    }
}
// TODO: dialogi w try catch
