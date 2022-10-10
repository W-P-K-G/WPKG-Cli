package me.wpkg.cli.gui;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.JSONParser;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class WPKGManager
{
    public JPanel wpkgManager;
    private JButton selectButton;
    private JButton killButton;
    private JButton refreshButton;
    private JButton logOffButton;
    public JTable clientTable;

    public TableModel tableModel;

    ErrorHandler errorHandler = new ErrorHandler();

    // Buttons Actions
    public WPKGManager()
    {
        logOffButton.addActionListener(ActionEvent -> logOffAction());
        refreshButton.addActionListener(ActionEvent -> refreshAction());
        killButton.addActionListener(ActionEvent -> killAction());
        selectButton.addActionListener(ActionEvent -> selectAction());

        tableModel = new TableModel();
        clientTable.setModel(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        clientTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        //default action if password expired
        errorHandler.setNotAuthorizedEvent(() -> {
            JOptionPane.showMessageDialog(Main.frame,"Admin authorization expired","Expired",JOptionPane.INFORMATION_MESSAGE);
            StateManager.changeState(State.LOGON_UI);
        });
    }

    public JSONParser.ClientJSON clientJSON;

    public void refreshClientList(DefaultTableModel tableModel)
    {
        try
        {
            tableModel.setRowCount(0);
            String message = errorHandler.check(Client.sendCommand("/rat-list"));

            if (errorHandler.ok())
            {
                clientJSON = JSONParser.getClientList(message);
                for (var client : clientJSON.clients)
                    tableModel.addRow(new Object[]{client.id, client.name, client.joined, client.version});
            }
        }
        catch (IOException e)
        {
            Tools.receiveError(e);
        }
    }

    public void logOffAction()
    {
        try {
            Client.logOff();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StateManager.changeState(State.LOGON_UI);
    }
    public void refreshAction()
    {
        refreshClientList(tableModel);
    }
    public void killAction()
    {
        try
        {
            errorHandler.check(Client.sendCommand("/close " + clientJSON.clients[clientTable.getSelectedRow()].id));
            if (errorHandler.ok())
                refreshClientList(tableModel);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Client not selected","Error",JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e)
        {
            Tools.sendError(e);
        }
    }
    public void selectAction()
    {
        try
        {
            if (clientJSON.clients[clientTable.getSelectedRow()].joined)
            {
                JOptionPane.showMessageDialog(Main.frame,"Can't connect to client: Client already joined","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            int index = clientJSON.clients[clientTable.getSelectedRow()].id;
            Main.ClientManager.join(index);
            StateManager.changeState(State.CLIENT_MANAGER);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            JOptionPane.showMessageDialog(Main.frame,"Client not selected","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class TableModel extends DefaultTableModel
    {
        public TableModel() {
            super(new String[] {"ID","Name","Joined","Version"}, 0);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            return switch (columnIndex) {
                case 0 -> Integer.class;
                case 1,3 -> String.class;
                case 2 -> Boolean.class;
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

}

