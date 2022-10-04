package me.wpkg.cli.commands;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.gui.ProgressDialog;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Screenshot extends Command
{
    public Screenshot(DefaultListModel<String> clientModel)
    {
        super("screenshot", "Get Screenshot", clientModel);
    }
    @Override
    public void execute(ErrorHandler errorHandler)
    {
        ProgressDialog progressDialog = new ProgressDialog("Waiting for screenshot...");
        progressDialog.start((dialog) -> {
            try
            {
                String url = errorHandler.check(sendCommand("screenshot"));

                System.out.println(url);

                if (errorHandler.ok())
                {
                    if (Desktop.isDesktopSupported())
                    {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException | URISyntaxException e) {
                            JOptionPane.showMessageDialog(Main.frame, "Error:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else JOptionPane.showMessageDialog(Main.frame, "System don't support java.awt.Desktop", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (errorHandler.error())
                    JOptionPane.showMessageDialog(Main.frame, "Failed to capture screenshot by WPKG", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Tools.receiveError(e);
            }
        });
    }
}
