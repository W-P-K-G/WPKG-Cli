package com.wpkg.cli.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpkg.cli.commands.Command;
import com.wpkg.cli.commands.RunProcess;
import com.wpkg.cli.commands.SendMessage;
import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ClientManager {
    public JPanel clientManager;
    private JButton unjoinButton;
    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar swapBar;
    private JButton cryptoManager;
    public JList commandList;
    private JButton executeButton;
    private JButton refreshButton;

    private DefaultListModel<String> commandsModel = new DefaultListModel<>();

    private ArrayList<Command> commands = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    public boolean commandWorks = false,joined = false;

    public ClientManager()
    {
        unjoinButton.addActionListener(ActionEvent -> unjoinAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
        cryptoManager.addActionListener(actionEvent -> cryptoAction());
        executeButton.addActionListener(actionEvent -> executeAction());

        commandList.setModel(commandsModel);

        commands.add(new SendMessage(commandsModel));
        commands.add(new RunProcess(commandsModel));

        cpuBar.setStringPainted(true);
        ramBar.setStringPainted(true);
        swapBar.setStringPainted(true);

        new Thread(this::statsThread).start();

    }
    public void refreshAction()
    {
        refreshStats();
    }

    public void cryptoAction(){
        Main.ClientManager.clientManager.setVisible(false);
        Main.WPKGManager.wpkgManager.setVisible(true);
        Main.frame.setContentPane(Main.CryptoManager.CryptoPanelGPU);
    }

    public void executeAction()
    {
        commandWorks = true;
        commands.get(commandList.getSelectedIndex()).execute();
        commandWorks = false;
    }

    public void statsThread()
    {
        while (true)
        {
            if (joined && !commandWorks)
                refreshStats();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void join(int id)
    {
        UDPClient.sendString("/join "+ id);
        UDPClient.receiveString();

        refreshStats();
        joined = true;
    }

    public void unjoinAction(){
        UDPClient.sendString("/unjoin");
        Main.ClientManager.clientManager.setVisible(false);
        Main.WPKGManager.wpkgManager.setVisible(true);
        Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
        joined = false;
        UDPClient.receiveString();
    }

    public void refreshStats()
    {
        try
        {
            UDPClient.sendString("stat");
            OutputMap map = objectMapper.readValue(UDPClient.receiveString(),OutputMap.class);

            String[] mess = map.output.split(" ");

            int cpu = (int)Math.floor(Float.parseFloat(mess[0]));
            cpuBar.setValue(cpu);
            cpuBar.setString(cpu + "%");

            double memfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[1]) / 1024 / 1024 / 1024);
            double memtotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[2]) / 1024 / 1024 / 1024);

            ramBar.setString(memfree + "GB/" + memtotal + "GB");
            ramBar.setValue((int)(memfree * 100 / memtotal));

            double swapfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[3]) / 1024 / 1024 / 1024);
            double swaptotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[4]) / 1024 / 1024 / 1024);

            swapBar.setString(swapfree + "GB/" + swaptotal + "GB");
            swapBar.setValue((int)(swapfree * 100 / swaptotal));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
}

class OutputMap
{
    public String output;
}