package me.wpkg.cli.gui;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

public class ProgressDialog extends JDialog
{
    private JPanel panel;
    private JLabel label;

    private CompletableFuture<Void> future;

    public interface Target
    {
        void target(JDialog dialog);
    }

    public ProgressDialog(String title)
    {
        setSize(400,100);
        setAlwaysOnTop(true);
        setFocusable(true);
        setTitle(title);
        setContentPane(panel);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        label.setText(title);
    }

    public void await()
    {
        future.join();
    }

    public ProgressDialog start(Target target)
    {
        future = CompletableFuture.runAsync(() -> {
            target.target(this);
            SwingUtilities.invokeLater(this::dispose);
        });
        setVisible(true);
        return this;
    }
}
