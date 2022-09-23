package me.wpkg.cli.state;

import me.wpkg.cli.main.Main;

public class StateManager
{
    public static State state;

    public static State getState()
    {
        return state;
    }

    public static void changeState(State state)
    {
        switch (state)
        {
            case LOGON_UI -> {
                Main.ClientManager.clientManager.setVisible(false);
                Main.WPKGManager.wpkgManager.setVisible(false);
                Main.CryptoManager.CryptoPanelGPU.setVisible(false);

                Main.LogonUI.logonUI.setVisible(true);
                Main.frame.setContentPane(Main.LogonUI.logonUI);
            }

            case CLIENT_LIST -> {
                Main.WPKGManager.refreshClientList(Main.WPKGManager.tableModel);
                Main.LogonUI.logonUI.setVisible(false);

                Main.WPKGManager.wpkgManager.setVisible(true);
                Main.frame.setContentPane(Main.WPKGManager.wpkgManager);
            }
            case CLIENT_MANAGER -> {
                Main.ClientManager.clientManager.setVisible(true);
                Main.WPKGManager.wpkgManager.setVisible(false);
                Main.CryptoManager.CryptoPanelGPU.setVisible(false);
                Main.frame.setContentPane(Main.ClientManager.clientManager);
            }

            case CRYPTO_MANAGER -> {
                Main.ClientManager.clientManager.setVisible(false);
                Main.WPKGManager.wpkgManager.setVisible(false);
                Main.CryptoManager.CryptoPanelGPU.setVisible(true);
                Main.frame.setContentPane(Main.CryptoManager.CryptoPanelGPU);
            }
        }

        StateManager.state = state;
    }
}
