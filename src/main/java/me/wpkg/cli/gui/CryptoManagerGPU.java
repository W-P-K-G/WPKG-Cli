package me.wpkg.cli.gui;

import me.wpkg.cli.main.Main;
import me.wpkg.cli.net.Client;
import me.wpkg.cli.state.State;
import me.wpkg.cli.state.StateManager;
import me.wpkg.cli.utils.Globals;
import me.wpkg.cli.utils.JSONParser;
import me.wpkg.cli.utils.Tools;

import javax.swing.*;
import javax.tools.Tool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CryptoManagerGPU {
    public JPanel CryptoPanelGPU;
    private JButton refreshButton;
    private JTextArea minerLog;
    public JComboBox<CryptoCurrencies> cryptoComboBox;
    private JLabel statusLabel;
    private JComboBox poolComboBox;
    private JComboBox walletComboBox;
    private JButton RUNButton;
    private JButton backButton;
    private JTextField workerField;
    private JComboBox algorithmField;
    private JButton STOPButton;
    static ArrayList<ArrayList<String>> Wallets = new ArrayList<>();

    public static void refreshWallets(JComboBox walletComboBox){
        Wallets.clear();
        JSONParser.walletJSON[] walletJSONS = JSONParser.getWallet(Tools.readStringFromURL(Globals.jsonURL +"Wallets.json"));
        for(var value : CryptoCurrencies.values())
        {
            Wallets.add(new ArrayList<>());
        }
        for(JSONParser.walletJSON i : walletJSONS)
        {
            switch (i.coin)
            {
                case "ETC" -> Wallets.get(CryptoCurrencies.ETC.ordinal()).add(i.id);
                case "UNMINEABLE" -> Wallets.get(CryptoCurrencies.UNMINEABLE.ordinal()).add(i.id);
            }
        }

        //referrals.add(walletJSONS[0].referral);
        walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(0).toArray()));
    }
    public enum CryptoCurrencies
    {
        ETC,
        UNMINEABLE
    }
    public ArrayList<String> ETCPools = new ArrayList<>(Arrays.asList("etc.2miners.com:1010"));
    public ArrayList<String> unMineablePools = new ArrayList<>(Arrays.asList("etchash.unmineable.com", "kp.unmineable.com", "autolykos.unmineable.com"));
    public ArrayList<String> Algorithms = new ArrayList<>(Arrays.asList("ethash", "etchash", "rvn", "kawpow", "ergo", "cortex", "beamhash"));
    public CryptoManagerGPU()
    {
        cryptoComboBox.setModel(new DefaultComboBoxModel(CryptoManagerGPU.CryptoCurrencies.values()));
        poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.toArray()));
        algorithmField.setModel(new DefaultComboBoxModel(Algorithms.toArray()));
        cryptoComboBox.addActionListener(actionEvent -> updatePoolComboBox());
        RUNButton.addActionListener(actionEvent -> runAction());
        STOPButton.addActionListener(actionEvent -> stopAction());
        backButton.addActionListener(actionEvent -> backAction());

        refreshWallets(walletComboBox);

    }

    private void backAction()
    {
        StateManager.changeState(State.CLIENT_MANAGER);
    }
    private void updatePoolComboBox()
    {
        switch(cryptoComboBox.getSelectedItem().toString()){
            case "ETC" -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.toArray()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.ETC.ordinal()).toArray()));
            }
            case "UNMINEABLE" -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(unMineablePools.toArray()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.UNMINEABLE.ordinal()).toArray()));
            }
        }
    }
    private void runAction() {
        try {
            JComboBox[] Arrays = {algorithmField, poolComboBox, walletComboBox};
            for (JComboBox combo: Arrays){
                if (combo.getSelectedItem() == null || combo.getSelectedItem() == "") {
                    JOptionPane.showMessageDialog(Main.frame,"Missing Information","ERROR",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (workerField.getText() == null || Objects.equals(workerField.getText(), "")) {
                JOptionPane.showMessageDialog(Main.frame,"Missing Workername","ERROR",JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.print("test:"+workerField.getText()+"end");
            Client.sendCommand("startminer "+algorithmField.getSelectedItem()+" "+poolComboBox.getSelectedItem()+" "
                    +walletComboBox.getSelectedItem().toString().replace("workername", workerField.getText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void stopAction() {
        try {
            Client.sendCommand("stopminer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
