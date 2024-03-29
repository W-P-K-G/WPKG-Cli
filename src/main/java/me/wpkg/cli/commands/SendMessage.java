package me.wpkg.cli.commands;

import java.io.IOException;
import javax.swing.*;
import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;

public class SendMessage extends Command {
    public SendMessage(DefaultListModel<String> clientModel) {
        super("msg", "Send message", clientModel);
    }

    @Override
    public void execute(ErrorHandler errorHandler) throws IOException {
        String mess = JOptionPane.showInputDialog(Main.frame, "Message", "Enter message to send",
                JOptionPane.QUESTION_MESSAGE);

        errorHandler.check(sendCommand("msg " + mess));

        if (errorHandler.error())
            failDialog("WPKG send message error: " + errorHandler.msg());
    }
}
