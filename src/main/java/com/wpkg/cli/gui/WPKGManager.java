package com.wpkg.cli.gui;

import com.wpkg.cli.main.Main;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.state.State;
import com.wpkg.cli.state.StateManager;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;

@SuppressWarnings("unused")
public class WPKGManager {
    public JPanel wpkgManager;
    public JList<String> ClientList;
    private JPanel Buttons;
    private JButton selectButton;
    private JButton infoButton;
    private JButton killButton;
    private JButton refreshButton;
    private JButton logOffButton;
    private JLabel WPKGLabel;
    public DefaultListModel<String> clientModel = new DefaultListModel<>();

    // Buttons Actions
    public WPKGManager()
    {
        logOffButton.addActionListener(ActionEvent -> logOffAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
        killButton.addActionListener(ActionEvent -> killAction());
        selectButton.addActionListener(ActionEvent -> selectAction());
    }

    public void logOffAction()
    {
        UDPClient.logOff();

        StateManager.changeState(State.LOGON_UI);
    }
    public void refreshAction()
    {
        Tools.refreshClientList(clientModel);
    }
    public void killAction()
    {
        UDPClient.sendCommand("/close "+ Tools.clientJSON.clients[ClientList.getSelectedIndex()].id);

        Tools.refreshClientList(clientModel);
    }
    public void selectAction()
    {
        try
        {
            int index = Tools.clientJSON.clients[ClientList.getSelectedIndex()].id;
            Main.ClientManager.join(index);
            StateManager.changeState(State.CLIENT_MANAGER);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Client not selected","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
