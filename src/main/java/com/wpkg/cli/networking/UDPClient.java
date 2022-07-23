package com.wpkg.cli.networking;

import java.io.IOException;
import java.net.*;

import com.wpkg.cli.main.main;


@SuppressWarnings("FieldMayBeFinal")
public class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;
    private String[] portAddress;
    private DatagramPacket packet;
    private byte[] buf;

    public UDPClient(){
        /* Creating Socket */
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        /* Setting Timeout */
        try {
            socket.setSoTimeout(10000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        /* Getting IP Address */
        portAddress = main.LogonUI.IPField.getText().split(":");
        try {
            address = InetAddress.getByName(portAddress[0]);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendString(String msg){
        buf = msg.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(portAddress[1]));

        /* Sending Packet */
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String receiveString(){
        buf = new byte[65536];
        packet = new DatagramPacket(buf, buf.length);

        /* Receiving Packet */
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(packet.getData(), 0, packet.getLength());
    }
    public void logOff(){
        if(socket.isClosed()) return;
        sendString("/disconnect");
        socket.close();
    }
}
