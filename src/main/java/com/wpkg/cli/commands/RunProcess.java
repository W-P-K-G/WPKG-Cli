package com.wpkg.cli.commands;

import com.wpkg.cli.main.Main;

import javax.swing.*;

public class RunProcess extends Command
{
    public RunProcess(DefaultListModel<String> clientModel)
    {
        super("run", "Run process", clientModel);
    }

    @Override
    public void execute()
    {
        JTextField command = new JTextField();
        JTextField args = new JTextField();
        JComponent[] comp = {
                new JLabel("Enter process name:"),
                command,
                new JLabel("Enter args:"),
                args
        };

        if (JOptionPane.showConfirmDialog(Main.frame,comp,"Process run",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE) != 0)
            return;

        sendCommand("run " + command.getText() + " " + args.getText());
    }
}
