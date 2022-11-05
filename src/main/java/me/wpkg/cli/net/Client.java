package me.wpkg.cli.net;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {
    protected static boolean connected;

    private static Protocol protocol;

    public static void setProtocol(Protocol protocol) {
        Client.protocol = protocol;
    }

    public static Protocol getProtocol() {
        return protocol;
    }

    public static void connect(String ip, int p) throws IOException {
        switch (protocol) {
            case TCP -> TCPClient.connect(ip,p);
            case UDP -> UDPClient.connect(ip,p);
        }
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void sendString(String msg) throws IOException {
        switch (protocol) {
            case TCP -> TCPClient.sendString(msg);
            case UDP -> UDPClient.sendString(msg);
        }
    }

    public static String receiveString() throws IOException {
        return switch (protocol) {
            case TCP -> TCPClient.receiveString();
            case UDP -> UDPClient.receiveString();
        };
    }

    public static String sendCommand(String command) throws IOException {
        return switch (protocol) {
            case TCP -> TCPClient.sendCommand(command);
            case UDP -> UDPClient.sendCommand(command);
        };
    }

    public static byte[] rawdata_receive() throws IOException {
        return switch (protocol) {
            case TCP -> TCPClient.rawdata_receive();
            case UDP -> UDPClient.rawdata_receive();
        };
    }

    public static void rawdata_send(byte[] b) throws IOException {
        switch (protocol) {
            case TCP -> TCPClient.rawdata_send(b);
            case UDP -> UDPClient.rawdata_send(b);
        }
    }

    public static void logOff() throws IOException {
        switch (protocol) {
            case TCP -> TCPClient.logOff();
            case UDP -> UDPClient.logOff();
        }
    }
}
