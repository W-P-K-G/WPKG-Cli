package me.wpkg.cli.state;

import me.wpkg.cli.main.Main;

import java.awt.*;

public class StateManager {
    public static State state;

    public static State getState() {
        return state;
    }

    public static void changeState(State state) {
        CardLayout cardLayout = (CardLayout) Main.mainPanel.getLayout();
        switch (state) {
            case LOGON_UI -> cardLayout.show(Main.mainPanel,"Logon UI");
            case CLIENT_LIST -> cardLayout.show(Main.mainPanel,"Client List");
            case CLIENT_MANAGER -> cardLayout.show(Main.mainPanel,"Client Manager");
            case CRYPTO_MANAGER -> cardLayout.show(Main.mainPanel,"Crypto Manager");
        }

        StateManager.state = state;
    }
}
