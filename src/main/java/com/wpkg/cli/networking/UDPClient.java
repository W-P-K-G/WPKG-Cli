package com.wpkg.cli.networking;

import com.wpkg.cli.commands.SendMessage;
import com.wpkg.cli.main.Main;
import com.wpkg.cli.utilities.Tools;

import java.io.IOException;
import java.net.*;

import javax.swing.*;


public class UDPClient {
    public static DatagramSocket socket;
    public static InetAddress address;

    public static int port;

    public static boolean connected;

    public static void connect(String ip, int p) throws SocketException, UnknownHostException {
        /* Getting IP Address */
        address = InetAddress.getByName(ip);
        port = p;

        /* Creating Socket */
        socket = new DatagramSocket();

        /* Setting Timeout */
        socket.setSoTimeout(60 * 1000);

        connected = true;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void sendRegisterPing() throws IOException
    {
        //this method don't using receiveString and sendString method because using IOException to properly error handling
        byte[] buf = "register".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        socket.send(packet);

        byte[] buf2 = new byte[65536];
        packet = new DatagramPacket(buf2, buf2.length);

        socket.receive(packet);
    }

    public static void sendString(String msg)
    {
        System.out.println("Sended: " + msg);
        try {
            byte[] buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

            /* Sending Packet */
            socket.send(packet);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't send message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public static String receiveString() {
        try {
            byte[] buf = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            /* Receiving Packet */
            socket.receive(packet);

            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.println("Received: " + msg);
            return msg;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't receive message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public static String sendCommand(String command)
    {
        sendString(command);
        return receiveString();
    }

    public static byte[] rawdata_receive()
    {
        try
        {
            byte[] buf = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            /* Receiving Packet */
            socket.receive(packet);

            int len = packet.getLength();
            byte[] ret = new byte[len];

            for (int i = 0;i < len;i++)
                ret[i] = buf[i];

            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void rawdata_send(byte[] b)
    {
        DatagramPacket p = new DatagramPacket(b, b.length, address, port);
        try
        {
            socket.send(p);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void logOff()
    {
        if (socket.isClosed()) return;
        sendString("/disconnect");
        socket.close();
    }
}
