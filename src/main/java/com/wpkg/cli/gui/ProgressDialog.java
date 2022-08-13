package com.wpkg.cli.gui;

import javax.swing.*;

public class ProgressDialog extends JDialog
{
    private JPanel panel;
    private JLabel label;

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

    public void startAndJoin(Target target)
    {
        try {
            start(target).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Thread start(Target target)
    {
        Thread t = new Thread(() -> {
            target.target(this);
            SwingUtilities.invokeLater(this::dispose);
        });
        t.start();
        setVisible(true);
        return t;
    }
}
