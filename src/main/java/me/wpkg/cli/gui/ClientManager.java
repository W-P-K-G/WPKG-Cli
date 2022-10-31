package me.wpkg.cli.gui;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import me.wpkg.cli.commands.Command;
import me.wpkg.cli.commands.RunProcess;
import me.wpkg.cli.commands.Screenshot;
import me.wpkg.cli.commands.SendMessage;
import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Tools;

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

    public boolean commandWorks = false, joined = false;

    ErrorHandler errorHandler = new ErrorHandler();

    public ClientManager() {
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

        // default action if session expired when joined
        errorHandler.setSessionExpiredEvent(() -> {
            JOptionPane.showMessageDialog(Main.frame, "Client was disconnected. Session expired", "Client Disconnected",
                    JOptionPane.INFORMATION_MESSAGE);
            StateManager.changeState(State.CLIENT_LIST);
        });
        // default action if password expired
        errorHandler.setNotAuthorizedEvent(() -> {
            JOptionPane.showMessageDialog(Main.frame, "Admin authorization expired", "Expired",
                    JOptionPane.INFORMATION_MESSAGE);
            StateManager.changeState(State.LOGON_UI);
        });
    }

    public void refreshAction() {
        ProgressDialog progressDialog = new ProgressDialog("Refreshing...");
        progressDialog.start((dialog) -> refreshStats());
    }

    public void cryptoAction() {
        StateManager.changeState(State.CRYPTO_MANAGER);
    }

    public void executeAction() {
        try {
            commandWorks = true;

            commands.get(commandList.getSelectedIndex()).execute(errorHandler);

            errorHandler.clear();

            commandWorks = false;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(Main.frame, "Command not selected", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            Tools.sendError(e);
            joined = false;
            StateManager.changeState(State.CLIENT_LIST);
        }
    }

    public void statsThread() {
        statsThread = new Thread(() -> {
            while (true) {
                if (!joined)
                    return;

                if (!commandWorks && StateManager.getState() == State.CLIENT_MANAGER)
                    refreshStats();

                Tools.sleep(10000);
            }
        });
        statsThread.start();
    }

    public void join(int id) {
        try {
            Client.sendCommand("/join " + id);
            refreshAction();
            statsThread();
            joined = true;
        } catch (IOException e) {
            Tools.receiveError(e);
        }
    }

    public void unjoinAction() {
        try {
            joined = false;

            while (statsRefreshing)
                Tools.sleep(1);

            Client.sendCommand("/unjoin");
            StateManager.changeState(State.CLIENT_LIST);
        } catch (IOException e) {
            Tools.sendError(e);
        }
    }

    public void refreshStats() {
        try {
            statsRefreshing = true;

            String[] mess = Client.sendCommand("stat").split(" ");

            int cpu = (int) Math.floor(Float.parseFloat(mess[0]));

            double memfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[1]) / 1024 / 1024 / 1024);
            double memtotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[2]) / 1024 / 1024 / 1024);

            double swapfree = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[3]) / 1024 / 1024 / 1024);
            double swaptotal = Tools.roundTo2DecimalPlace(Double.parseDouble(mess[4]) / 1024 / 1024 / 1024);

            SwingUtilities.invokeLater(() -> {
                cpuBar.setValue(cpu);
                cpuBar.setString(cpu + "%");

                ramBar.setString(memfree + "GB/" + memtotal + "GB");
                ramBar.setValue((int) (memfree * 100 / memtotal));

                swapBar.setString(swapfree + "GB/" + swaptotal + "GB");
                swapBar.setValue((int) (swapfree * 100 / swaptotal));
            });
            statsRefreshing = false;
        } catch (IOException e) {
            Tools.sendError(e);
            joined = false;
            StateManager.changeState(State.CLIENT_LIST);
        }
    }
}
