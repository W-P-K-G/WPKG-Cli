package com.wpkg.cli.networking;

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
        socket.setSoTimeout(2000);

        connected = true;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void sendRegisterPing() throws IOException {
        //this method don't using receiveString and sendString method because using IOException to properly error handling
        byte[] buf = "register".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        socket.send(packet);

        byte[] buf2 = new byte[65536];
        packet = new DatagramPacket(buf2, buf2.length);

        socket.receive(packet);
    }

    public static void sendString(String msg) {
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

            return new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't receive message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public static void logOff() {
        if (socket.isClosed()) return;
        sendString("/disconnect");
        socket.close();
    }
}
// TODO: dialogi w try catch
