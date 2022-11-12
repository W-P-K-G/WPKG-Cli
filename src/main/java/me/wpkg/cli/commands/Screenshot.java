package me.wpkg.cli.commands;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.gui.ProgressDialog;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Screenshot extends Command {
    public Screenshot(DefaultListModel<String> clientModel) {
        super("screenshot", "Get Screenshot", clientModel);
    }

    @Override
    public void execute(ErrorHandler errorHandler) {
        ProgressDialog progressDialog = new ProgressDialog("Waiting for screenshot...");
        progressDialog.start((dialog) -> {
            try {
                int size = Integer.parseInt(errorHandler.check(sendCommand("screenshot")));
                switch (errorHandler.get()) {

                    case OK -> {
                        progressDialog.getProgressBar().setIndeterminate(false);
                        progressDialog.getProgressBar().setStringPainted(true);
                        progressDialog.setText("Receiving screenshot...");

                        double bandwidth = 0;
                        String bandwidthtext = "% (" + bandwidth + "KB/s)";

                        File imageFile = new File(Tools.getTmp(),"screenshot" + System.currentTimeMillis() + ".png");
                        imageFile.deleteOnExit();

                        long timeprev = 0;
                        int i = 0;
                        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                            send("OK");

                            while (i < size) {
                                int percent = (int) (((float)i / (float)size) * 100);

                                byte[] buffer = receiveRawdata();
                                long timenow = System.nanoTime();

                                bandwidth = Tools.roundTo2DecimalPlace((double)(buffer.length / 1024) / ((double)(timenow - timeprev) / 1000000000));

                                if (i % 1000 == 0)
                                    bandwidthtext = "(" + bandwidth + "KB/s)";

                                progressDialog.getProgressBar().setValue(percent);
                                progressDialog.getProgressBar().setString(percent + "% " + bandwidthtext);

                                fos.write(buffer);
                                i += buffer.length;

                                timeprev = timenow;
                            }
                        }

                        Desktop.getDesktop().open(imageFile);
                    }
                    case ERROR -> failDialog("WPKG screenshot error: " + errorHandler.msg());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Tools.receiveError(e);
            }
        });
    }
}
