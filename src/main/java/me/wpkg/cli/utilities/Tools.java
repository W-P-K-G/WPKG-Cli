package me.wpkg.cli.utilities;

import me.wpkg.cli.networking.UDPClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

import static me.wpkg.cli.utilities.JSONParser.getAddress;

public class Tools {
    public static JSONParser.ClientJSON clientJSON;
    public static String URL = "https://wpkg.me/WPKG/JSONFiles/";

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void refreshClientList(DefaultTableModel tableModel)
    {
        tableModel.setRowCount(0);
        clientJSON = JSONParser.getClientList(UDPClient.sendCommand("/rat-list"));
        for (var client : clientJSON.clients)
            tableModel.addRow(new Object[] {client.id,client.name,client.joined,client.version});
    }

    public static void refreshServerList(JComboBox<String> IPField){
        JSONParser.AddressJSON address = getAddress(Tools.readStringFromURL(URL+"Addreses.json"));

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for(var addr : address.uAddresses)
            model.addElement(addr.ip + ":" + addr.port);

        IPField.setModel(model);
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

    public static File getTmp()
    {
        return tmp;
    }

    public static void setTmp(File tmp) {
        Tools.tmp = tmp;
    }
}
