package com.wpkg.cli.commands;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;

public class Screenshot extends Command{

    static String tmpdir;

    static {
        try {
            File tmp = Files.createTempDirectory("wpkgcli").toFile();
            tmp.deleteOnExit();
            tmpdir = tmp.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Screenshot(DefaultListModel<String> clientModel)
    {
        super("screenshot", "Get Screenshot", clientModel);
    }
    @Override
    public void execute()
    {
        String url = sendCommand("screenshot");

        if (Desktop.isDesktopSupported())
        {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
