package me.wpkg.cli.commands;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;

import javax.swing.*;
import java.io.IOException;

public class SendMessage extends Command
{
    public SendMessage(DefaultListModel<String> clientModel)
    {
        super("msg", "Send message", clientModel);
    }

    @Override
    public void execute(ErrorHandler errorHandler) throws IOException
    {
        String mess = JOptionPane.showInputDialog(Main.frame,"Message","Enter message to send",JOptionPane.QUESTION_MESSAGE);

        sendCommand("msg " + mess);
    }
}
