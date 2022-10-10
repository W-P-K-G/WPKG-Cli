package me.wpkg.cli.commands;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;

import javax.swing.*;
import java.io.IOException;

public class RunProcess extends Command
{
    public RunProcess(DefaultListModel<String> clientModel)
    {
        super("run", "Run process", clientModel);
    }

    @Override
    public void execute(ErrorHandler errorHandler) throws IOException
    {
        String command = JOptionPane.showInputDialog(Main.frame,"Enter command to run: ","Process run",JOptionPane.QUESTION_MESSAGE);

        if (command == null)
            return;

        errorHandler.check(sendCommand("run " + command));

        if (errorHandler.error())
            failDialog("WPKG run process error: " + errorHandler.msg());
    }
}
