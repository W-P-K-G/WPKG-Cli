package com.wpkg.cli.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpkg.cli.commands.Command;
import com.wpkg.cli.commands.RunProcess;
import com.wpkg.cli.commands.Screenshot;
import com.wpkg.cli.commands.SendMessage;
import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.state.State;
import com.wpkg.cli.state.StateManager;
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
        commands.add(new Screenshot(commandsModel));

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
        StateManager.changeState(State.CRYPTO_MANAGER);
    }

    public void executeAction()
    {
        try
        {
            commandWorks = true;
            commands.get(commandList.getSelectedIndex()).execute();
            commandWorks = false;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Command not selected","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void statsThread()
    {
        while (true)
        {
            if (joined && !commandWorks && StateManager.getState() == State.CLIENT_MANAGER)
                refreshStats();

            Tools.sleep(10000);
        }
    }

    public void join(int id)
    {
        UDPClient.sendCommand("/join "+ id);
        refreshStats();
        joined = true;
    }

    public void unjoinAction()
    {
        joined = false;

        UDPClient.sendCommand("/unjoin");
        StateManager.changeState(State.CLIENT_LIST);
    }

    public void refreshStats()
    {
        String[] mess = UDPClient.sendCommand("stat").split(" ");

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
}