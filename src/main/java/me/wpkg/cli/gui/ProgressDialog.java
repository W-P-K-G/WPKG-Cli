package me.wpkg.cli.gui;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import javax.swing.*;

public class ProgressDialog extends JDialog {
    private JPanel panel;
    private JLabel label;
    private JProgressBar progressBar;

    @FunctionalInterface
    public interface Target {
        void target(JDialog dialog);
    }

    public JProgressBar getProgressBar()
    {
        return progressBar;
    }

    public ProgressDialog(String title) {
        setSize(400, 100);
        setFocusable(true);
        setContentPane(panel);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        label.setText(title);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public ProgressDialog start(Target target) {
        CompletableFuture.runAsync(() -> {
            target.target(this);
            SwingUtilities.invokeLater(this::dispose);
        });

        setVisible(true);
        return this;
    }
}
