package com.wpkg.cli.commands;

import com.wpkg.cli.networking.UDPClient;

import javax.swing.*;

public abstract class Command
{
    public String command,name;

    public Command(String command, String name, DefaultListModel<String> clientModel)
    {
        this.command = command;
        this.name = name;

        clientModel.addElement(name);
    }

    public abstract void execute();

    protected String sendCommand(String command)
    {
        sendToServer(command);
        return receiveFromServer();
    }

    protected String receiveFromServer()
    {
        return UDPClient.receiveString();
    }

    protected void sendToServer(String command)
    {
        UDPClient.sendString(command);
    }
}
