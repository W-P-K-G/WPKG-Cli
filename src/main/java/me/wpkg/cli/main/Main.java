package me.wpkg.cli.main;

import com.formdev.flatlaf.FlatDarkLaf;

import me.wpkg.cli.gui.ClientManager;
import me.wpkg.cli.gui.CryptoManagerGPU;
import me.wpkg.cli.gui.LogonUI;
import me.wpkg.cli.gui.WPKGManager;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Globals;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static JFrame frame = new JFrame("WPKG-CLI");
    public static LogonUI LogonUI;
    public static WPKGManager WPKGManager;
    public static ClientManager ClientManager;
    public static CryptoManagerGPU CryptoManager;


    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        UIManager.put("ProgressBar.repaintInterval", 5);

        if (!Globals.workDir.exists()) {
            boolean ignored = Globals.workDir.mkdirs();
        }

        try
        {
            Globals.passwordFile = Paths.get(Globals.workDir.getPath(),"password").toFile();

            if (!Globals.passwordFile.createNewFile() && !Globals.passwordFile.exists())
                throw new IOException("password file not created");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(frame,"Error creating file:" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }

        // Dark-mode on
        FlatDarkLaf.setup();
        // Declare Windows
        WPKGManager = new WPKGManager();
        LogonUI = new LogonUI();
        ClientManager = new ClientManager();
        CryptoManager = new CryptoManagerGPU();

        // Disconnect on close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!Client.isConnected()) return;

            try {
                Client.logOff();
            } catch (IOException ignored) {}
        }, "Shutdown-thread"));

        // Setting frame settings
        frame.setSize(765, 445);

        StateManager.changeState(State.LOGON_UI);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
