package me.wpkg.cli.gui;

import me.wpkg.cli.commands.error.ErrorHandler;
import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Globals;
import me.wpkg.cli.utils.JSONParser;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CryptoManagerGPU {
    public JPanel CryptoPanelGPU;
    private JButton refreshButton;
    private JTextArea minerLog;
    public JComboBox<CryptoCurrencies> cryptoComboBox;
    private JLabel statusLabel;
    private JComboBox<String> poolComboBox;
    private JComboBox<String> walletComboBox;
    private JButton RUNButton;
    private JButton backButton;
    private JTextField workerField;
    private JComboBox<String> algorithmField;
    private JButton STOPButton;
    private final ErrorHandler errorHandler = new ErrorHandler();
    public enum CryptoCurrencies
    {
        ETC(new String[] {"etc.2miners.com:1010"}),
        ETH(new String[]{}),
        UNMINEABLE(new String[] {"etchash.unmineable.com", "kp.unmineable.com", "autolykos.unmineable.com"});
        public final String[] pool;
        public final ArrayList<String> wallets = new ArrayList<>();
        public DefaultComboBoxModel<String> walletComboBoxModel;
        public final DefaultComboBoxModel<String> poolComboBoxModel;
        public static final CryptoCurrencies defaultCrypto = ETC;
        CryptoCurrencies(String[] pool)
        {
            this.pool = pool;
            poolComboBoxModel = new DefaultComboBoxModel<>(pool);
        }
    }
    public String[] algorithms = {"ethash", "etchash", "rvn", "kawpow", "ergo", "cortex", "beamhash"};
    public CryptoManagerGPU()
    {
        cryptoComboBox.setModel(new DefaultComboBoxModel<>(CryptoManagerGPU.CryptoCurrencies.values()));
        poolComboBox.setModel(new DefaultComboBoxModel<>(CryptoCurrencies.defaultCrypto.pool));
        algorithmField.setModel(new DefaultComboBoxModel<>(algorithms));

        cryptoComboBox.addActionListener(actionEvent -> updatePoolComboBox());
        RUNButton.addActionListener(actionEvent -> runAction());
        STOPButton.addActionListener(actionEvent -> stopAction());
        backButton.addActionListener(actionEvent -> backAction());

        //default action if session expired when joined
        errorHandler.setSessionExpiredEvent(() -> {
            JOptionPane.showMessageDialog(Main.frame,"Client was disconnected. Session expired","Client Disconnected",JOptionPane.INFORMATION_MESSAGE);
            StateManager.changeState(State.CLIENT_LIST);
        });
        //default action if password expired
        errorHandler.setNotAuthorizedEvent(() -> {
            JOptionPane.showMessageDialog(Main.frame,"Admin authorization expired","Expired",JOptionPane.INFORMATION_MESSAGE);
            StateManager.changeState(State.LOGON_UI);
        });

        refreshWallets(walletComboBox);

    }

    public void refreshWallets(JComboBox<String> walletComboBox)
    {
        JSONParser.walletJSON[] walletJSONS = JSONParser.getWallet(Tools.readStringFromURL(Globals.jsonURL + "Wallets.json"));

        for (CryptoCurrencies crypto : CryptoCurrencies.values())
            crypto.wallets.clear();

        for (JSONParser.walletJSON i : walletJSONS)
            CryptoCurrencies.valueOf(i.coin).wallets.add(i.id);

        for (CryptoCurrencies crypto : CryptoCurrencies.values())
        {
            crypto.walletComboBoxModel = new DefaultComboBoxModel<>(crypto.wallets.toArray(String[]::new));
        }

        walletComboBox.setModel(CryptoCurrencies.defaultCrypto.walletComboBoxModel);
    }

    private void backAction()
    {
        StateManager.changeState(State.CLIENT_MANAGER);
    }
    private void updatePoolComboBox()
    {
        CryptoCurrencies crypto = (CryptoCurrencies) Objects.requireNonNull(cryptoComboBox.getSelectedItem());

        poolComboBox.setModel(crypto.poolComboBoxModel);
        walletComboBox.setModel(crypto.walletComboBoxModel);
    }
    private void runAction()
    {
        try
        {
            JComboBox<?>[] comboBoxes = { algorithmField, poolComboBox, walletComboBox };
            for (JComboBox<?> combo : comboBoxes)
            {
                if (combo.getSelectedItem() == null || combo.getSelectedItem() == "")
                {
                    failDialog("Missing Information");
                    return;
                }
            }
            if (workerField.getText() == null || workerField.getText().equals(""))
            {
                failDialog("Missing Workername");
                return;
            }
            errorHandler.check(Client.sendCommand("startminer "
                    + algorithmField.getSelectedItem()+" "+poolComboBox.getSelectedItem() + " "
                    + Objects.requireNonNull(walletComboBox.getSelectedItem()).toString().replace("workername", workerField.getText())));

            if (errorHandler.error())
                failDialog("Crypto error: " + errorHandler.msg());
        }
        catch (IOException e)
        {
            Tools.sendError(e);
        }
    }
    private void stopAction()
    {
        try
        {
            errorHandler.check(Client.sendCommand("stopminer"));

            if (errorHandler.error())
                failDialog("Error stopping miner: " + errorHandler.msg());
        }
        catch (IOException e)
        {
            Tools.sendError(e);
        }
    }

    private void failDialog(String message)
    {
        JOptionPane.showMessageDialog(Main.frame,message,"ERROR",JOptionPane.ERROR_MESSAGE);
    }
}
