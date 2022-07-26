package com.wpkg.cli.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.JSONParser;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
import java.util.ArrayList;
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
    static ArrayList<ArrayList<String>> Wallets = new ArrayList<>();
    static ArrayList<String> referrals;
    public static void refreshWallets(JComboBox walletComboBox){
        Wallets.clear();
        JSONParser.walletJSON[] walletJSONS = JSONParser.getWallet(Tools.readStringFromURL(Tools.URL+"Wallets.json"));
        for(int x = 0; x < CryptoCurrencies.values().length;x++){
            Wallets.add(new ArrayList<>());
        }
        for(JSONParser.walletJSON i : walletJSONS){
            switch (i.coin)
            {
                case "ETH" -> Wallets.get(CryptoCurrencies.ETH.ordinal()).add(i.id);
                case "ETC" -> Wallets.get(CryptoCurrencies.ETC.ordinal()).add(i.id);
                case "TRX" -> Wallets.get(CryptoCurrencies.TRX.ordinal()).add(i.id);
            }
        }

        //referrals.add(walletJSONS[0].referral);
        walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(0).toArray()));
    }
    public enum CryptoCurrencies {
        ETC,
        ETH,
        TRX
    }
    public enum ETCPools{
        testetc
    }
    public enum ETHPools{
        testeth
    }
    public  enum TRXPools{
        unMineable("https://unmineable.com/coins");

        public String name;
        TRXPools(String name) {
            this.name = name;
        }
        public String getName(){
            return name;
        }
    }
    public CryptoManagerGPU() {
        poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.values()));
        cryptoComboBox.addActionListener(actionEvent -> updatePoolComboBox());
        RUNButton.addActionListener(actionEvent -> runAction());
        refreshWallets(walletComboBox);

    }
    void updatePoolComboBox(){
        switch((CryptoCurrencies) Objects.requireNonNull(cryptoComboBox.getSelectedItem())){
            case ETC -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.values()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.ETC.ordinal()).toArray()));
            }
            case ETH -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(ETHPools.values()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.ETH.ordinal()).toArray()));
            }
            case TRX -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(TRXPools.values()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.TRX.ordinal()).toArray()));
            }
        }
    }
    void runAction(){
        JSONParser.CryptoJSON c = new JSONParser.CryptoJSON(cryptoComboBox.getSelectedItem().toString()
                , walletComboBox.getSelectedItem().toString()
                , referrals.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        UDPClient.sendString("mining-runc");
        UDPClient.receiveString();
        UDPClient.sendString(json);
    }
}
