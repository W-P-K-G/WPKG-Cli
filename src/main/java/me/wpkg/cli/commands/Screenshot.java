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

                switch (errorHandler.get())
                {
                    case OK -> {
                        if (Desktop.isDesktopSupported())
                        {
                            try
                            {
                                Desktop.getDesktop().browse(new URI(url));
                            }
                            catch (IOException | URISyntaxException e)
                            {
                                failDialog("Error:" + e.getMessage());
                            }
                        }
                        else failDialog("System don't support java.awt.Desktop");
                    }
                    case ERROR -> failDialog("WPKG screenshot error: " + errorHandler.msg());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Tools.receiveError(e);
            }
        });
    }
}
