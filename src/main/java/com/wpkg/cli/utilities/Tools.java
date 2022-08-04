package com.wpkg.cli.utilities;

import com.wpkg.cli.networking.UDPClient;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import static com.wpkg.cli.utilities.JSONParser.getAddress;

public class Tools {
    public static JSONParser.ClientJSON clientJSON;
    public static JSONParser.CommandsJSON commandsJSON;
    public static String URL = "https://raw.githubusercontent.com/W-P-K-G/JSONFiles/master/";

    private static File tmp;

    static
    {
        try
        {
            File tmp = Files.createTempDirectory("wpkgcli").toFile();
            tmp.deleteOnExit();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String readStringFromURL(String requestURL)
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            String result = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void refreshClientList(DefaultListModel<String> ClientListModel)
    {
        ClientListModel.clear();
        clientJSON = JSONParser.getClientList(UDPClient.sendCommand("/rat-list"));
        for(int i = 0; i < clientJSON.clients.length; i++){
            ClientListModel.add(i,"\uD83D\uDDA5️     \uD83D\uDCB3 ID: "+ clientJSON.clients[i].id + "        "
                    +" \uD83D\uDCD6 NAME: "+ clientJSON.clients[i].name);
        }
    }

    public static void refreshCommandsList(DefaultListModel<String> ClientListModel){
        UDPClient.sendString("command-list");
        ClientListModel.clear();
        commandsJSON = JSONParser.getCommandsList(UDPClient.receiveString());
        for (int i = 0; i < commandsJSON.commands.length; i++){
            ClientListModel.add(i, commandsJSON.commands[i].name); // TODO: Finish refreshCommandslist()
        }
    }

    public static void refreshServerList(JComboBox IPField){
        JSONParser.AddressJSON address = getAddress(Tools.readStringFromURL(URL+"Addreses.json"));
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < address.uAddresses.length; i++)
            list.add(address.uAddresses[i].ip+":"+address.uAddresses[i].port);
        IPField.setModel(new DefaultComboBoxModel(list.toArray()));
    }

    public static double roundTo2DecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static void sleep(long milis)
    {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getTmpDir()
    {
        return tmp;
    }
}
