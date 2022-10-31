package me.wpkg.cli.commands;

import java.io.IOException;
import javax.swing.*;
import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;

public abstract class Command {
    public String command, name;

    public Command(String command, String name, DefaultListModel<String> clientModel) {
        this.command = command;
        this.name = name;

        clientModel.addElement(name);
    }

    protected void failDialog(String message) {
        JOptionPane.showMessageDialog(Main.frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public abstract void execute(ErrorHandler errorHandler) throws IOException;

    protected String sendCommand(String command) throws IOException {
        send(command);
        return receive();
    }

    protected String receive() throws IOException {
        return Client.receiveString();
    }

    protected byte[] receiveRawdata() {
        return Client.rawdata_receive();
    };

    protected void sendRawdata(byte[] b) {
        Client.rawdata_send(b);
    }

    protected void send(String command) throws IOException {
        Client.sendString(command);
    }
}
