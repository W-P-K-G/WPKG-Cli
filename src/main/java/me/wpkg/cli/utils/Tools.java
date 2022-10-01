package me.wpkg.cli.utils;

import me.wpkg.cli.main.Main;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

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

    public static void receiveError(Exception e)
    {
        JOptionPane.showMessageDialog(null, "Can't receive message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void sendError(Exception e)
    {
        JOptionPane.showMessageDialog(null, "Can't receive message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static String getWorkDir()
    {
        String osname = System.getProperty("os.name");
        if (osname.startsWith("Windows"))
            return Paths.get(System.getenv("APPDATA"),"WPKG CLI").toString();
        else if (osname.contains("nux") || osname.contains("freebsd"))
            return Paths.get(System.getProperty("user.home"),".config","WPKG CLI").toString();
        else if (osname.contains("mac") || osname.contains("darwin"))
            return Paths.get(System.getProperty("user.home"),"Library","Application Support","WPKG CLI").toString();
        return "";
    }

    public static File getTmp()
    {
        return tmp;
    }

    public static void setTmp(File tmp) {
        Tools.tmp = tmp;
    }
}