package com.wpkg.cli.networking;


public class ClientJSON {
    public clients[] clients;
    public static class clients {
        public String name;
        public int id;

        public boolean joined;
    }
}
