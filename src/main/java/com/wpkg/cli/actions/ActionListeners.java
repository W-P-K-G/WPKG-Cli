package com.wpkg.cli.actions;

import com.wpkg.cli.main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionListeners {
    public void acceptAction(ActionEvent actionEvent) {
        LogonUI.setVisible(false);
        WPKGManager.setVisible(true);
        DefaultListModel<String> ClientListModel = new DefaultListModel<String>();
        ClientList.setModel(ClientListModel);
    }
}
