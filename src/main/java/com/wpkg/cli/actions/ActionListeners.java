package com.wpkg.cli.actions;

import com.wpkg.cli.main.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionListeners {

    public static MainWindow main;

    public static void acceptAction(ActionEvent actionEvent) {
        main.LogonUI.setVisible(false);
        main.WPKGManager.setVisible(true);
        DefaultListModel<String> ClientListModel = new DefaultListModel<String>();
        main.ClientList.setModel(ClientListModel);
    }
}
