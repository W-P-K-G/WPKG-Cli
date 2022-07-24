package com.wpkg.cli.networking;

import java.io.IOException;
import java.net.*;

import javax.swing.*;


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

    public void sendString(String msg)
    {
        try
        {
            byte[] buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

            /* Sending Packet */
            socket.send(packet);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't send message: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }
    public String receiveString()
    {
        try
        {
            byte[] buf = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            /* Receiving Packet */
            socket.receive(packet);

            return new String(packet.getData(), 0, packet.getLength());
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't receive message: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }
    public void logOff(){
        if(socket.isClosed()) return;
        sendString("/disconnect");
        socket.close();
    }
}
