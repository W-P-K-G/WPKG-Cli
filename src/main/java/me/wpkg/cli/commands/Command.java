package me.wpkg.cli.commands;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.net.Client;

import javax.swing.*;
import java.io.IOException;

public abstract class Command
{
    public String command,name;

    public Command(String command, String name, DefaultListModel<String> clientModel)
    {
        this.command = command;
        this.name = name;

        clientModel.addElement(name);
    }

    public abstract void execute(ErrorHandler errorHandler) throws IOException;

    protected String sendCommand(String command) throws IOException
    {
        sendToServer(command);
        return receiveFromServer();
    }

    protected String receiveFromServer() throws IOException
    {
        return Client.receiveString();
    }

    protected byte[] receiveRawdata()
    {
        return Client.rawdata_receive();
    };

    protected void sendRawdata(byte[] b)
    {
        Client.rawdata_send(b);
    }

    protected void sendToServer(String command) throws IOException
    {
        Client.sendString(command);
    }
}
