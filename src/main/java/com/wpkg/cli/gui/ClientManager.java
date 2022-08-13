package com.wpkg.cli.gui;

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

public class ClientManager {
    public JPanel clientManager;
    private JButton unjoinButton;
    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar swapBar;
    private JButton cryptoManager;
    public JList<String> commandList;
    private JButton executeButton;
    private JButton refreshButton;

    private final ArrayList<Command> commands = new ArrayList<>();

    Thread statsThread;

    boolean statsRefreshing;

    public boolean commandWorks = false,joined = false;

    public ClientManager()
    {
        unjoinButton.addActionListener(ActionEvent -> unjoinAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
        cryptoManager.addActionListener(actionEvent -> cryptoAction());
        executeButton.addActionListener(actionEvent -> executeAction());

        DefaultListModel<String> commandsModel = new DefaultListModel<>();
        commandList.setModel(commandsModel);

        commands.add(new SendMessage(commandsModel));
        commands.add(new RunProcess(commandsModel));
        commands.add(new Screenshot(commandsModel));

        cpuBar.setStringPainted(true);
        ramBar.setStringPainted(true);
        swapBar.setStringPainted(true);


    }
    public void refreshAction()
    {
        ProgressDialog progressDialog = new ProgressDialog("Refreshing...");
        progressDialog.start((dialog) -> refreshStats());
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
        statsThread = new Thread(() -> {
            while (true)
            {
                if (!joined)
                    return;

                if (!commandWorks && StateManager.getState() == State.CLIENT_MANAGER)
                    refreshStats();

                Tools.sleep(10000);
            }
        });
        statsThread.start();
    }

    public void join(int id)
    {
        UDPClient.sendCommand("/join "+ id);
        refreshAction();
        statsThread();
        joined = true;
    }

    public void unjoinAction()
    {
        joined = false;

        while (statsRefreshing)
            Tools.sleep(1);

        UDPClient.sendCommand("/unjoin");
        StateManager.changeState(State.CLIENT_LIST);
    }

    public void refreshStats()
    {
        statsRefreshing = true;

        String[] mess = UDPClient.sendCommand("stat").split(" ");

        int cpu = (int)Math.floor(Float.parseFloat(mess[0]));

        double memfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[1]) / 1024 / 1024 / 1024);
        double memtotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[2]) / 1024 / 1024 / 1024);

        double swapfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[3]) / 1024 / 1024 / 1024);
        double swaptotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[4]) / 1024 / 1024 / 1024);

        SwingUtilities.invokeLater(() -> {
            cpuBar.setValue(cpu);
            cpuBar.setString(cpu + "%");

            ramBar.setString(memfree + "GB/" + memtotal + "GB");
            ramBar.setValue((int)(memfree * 100 / memtotal));

            swapBar.setString(swapfree + "GB/" + swaptotal + "GB");
            swapBar.setValue((int)(swapfree * 100 / swaptotal));
        });
        statsRefreshing = false;

    }
}