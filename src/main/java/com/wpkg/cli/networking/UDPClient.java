package com.wpkg.cli.networking;

import java.io.IOException;
import java.net.*;

import com.wpkg.cli.main.main;


@SuppressWarnings("FieldMayBeFinal")
public class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;

    private int port;

    public UDPClient(String ip,int port) throws SocketException, UnknownHostException
    {
        /* Getting IP Address */
        this.address = InetAddress.getByName(ip);
        this.port = port;

        /* Creating Socket */
        socket = new DatagramSocket();

        /* Setting Timeout */
        socket.setSoTimeout(2000);
    }

    public void sendRegisterPing() throws IOException
    {
        //this method don't using receiveString and sendString method because using IOException to properly error handling
        byte[] buf = "register".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        socket.send(packet);

        byte[] buf2 = new byte[65536];
        packet = new DatagramPacket(buf2, buf2.length);

        socket.receive(packet);
    }

    public void sendString(String msg){
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        /* Sending Packet */
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String receiveString(){
        byte[] buf = new byte[65536];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

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
// TODO: dialogi w try catch