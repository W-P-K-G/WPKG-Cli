package com.wpkg.cli.commands;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class Screenshot extends Command{

    static String tmpdir;

    static {
        try {
            tmpdir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();
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
        //sending command and receiving buffer size

        sendToServer("screenshot");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int buffersize = Integer.parseInt(receiveFromServer());

        //sending receive to server
        sendToServer("received");

        String screenshotpath = tmpdir + File.separator + "screenshot" + new Date() + ".png";
        try (FileOutputStream fos = new FileOutputStream(screenshotpath))
        {
            int index = 0;
            while (index < buffersize)
            {
                byte[] buff = receiveRawdata();
                fos.write(buff);
                index += buff.length;
            }

            if (Desktop.isDesktopSupported())
            {
                Desktop.getDesktop().open(new File(screenshotpath));
            }

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
