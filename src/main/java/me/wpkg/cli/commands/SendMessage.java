package me.wpkg.cli.commands;

import me.wpkg.cli.main.Main;

import javax.swing.*;

public class SendMessage extends Command
{
    public SendMessage(DefaultListModel<String> clientModel)
    {
        super("msg", "Send message", clientModel);
    }

    @Override
    public void execute()
    {
        String mess = JOptionPane.showInputDialog(Main.frame,"Message","Enter message to send",JOptionPane.QUESTION_MESSAGE);

        sendCommand("msg " + mess);
    }
}
