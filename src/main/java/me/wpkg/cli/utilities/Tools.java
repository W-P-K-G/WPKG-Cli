package me.wpkg.cli.utilities;

import me.wpkg.cli.main.Main;
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

public class Tools
{
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
            JOptionPane.showMessageDialog(Main.frame,"Error: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public static String readStringFromURL(String requestURL)
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8))
        {
            scanner.useDelimiter("\\A");
            String result = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return result;
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Error: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    public static double roundTo2DecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static void sleep(long millis)
    {
        try {
            Thread.sleep(millis);
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
