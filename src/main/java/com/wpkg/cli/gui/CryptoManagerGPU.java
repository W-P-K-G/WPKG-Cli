package com.wpkg.cli.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpkg.cli.networking.UDPClient;
import com.wpkg.cli.utilities.JSONParser;
import com.wpkg.cli.utilities.Tools;

import javax.swing.*;
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
    static ArrayList<ArrayList<String>> Wallets = new ArrayList<>();

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
    public ArrayList<String> ETHPools = new ArrayList<>(Arrays.asList("ETHPOOL"));
    public ArrayList<String> ETCPools = new ArrayList<>(Arrays.asList("ETCPOOL"));
    public ArrayList<String> unMineablePools = new ArrayList<>(Arrays.asList("UNMINEABLEPOOL"));
    public CryptoManagerGPU()
    {
        cryptoComboBox.setModel(new DefaultComboBoxModel(CryptoManagerGPU.CryptoCurrencies.values()));
        poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.toArray()));
        cryptoComboBox.addActionListener(actionEvent -> updatePoolComboBox());
        RUNButton.addActionListener(actionEvent -> runAction());
        refreshWallets(walletComboBox);

    }
    void updatePoolComboBox(){
        switch(cryptoComboBox.getSelectedItem().toString()){
            case "ETC" -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(ETCPools.toArray()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.ETC.ordinal()).toArray()));
            }
            case "ETH" -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(ETHPools.toArray()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.ETH.ordinal()).toArray()));
            }
            case "TRX" -> {
                poolComboBox.setModel(new DefaultComboBoxModel<>(unMineablePools.toArray()));
                walletComboBox.setModel(new DefaultComboBoxModel(Wallets.get(CryptoCurrencies.TRX.ordinal()).toArray()));
            }
        }
    }
    void runAction(){
        JSONParser.CryptoJSON c = new JSONParser.CryptoJSON(
                unMineablePools.contains(poolComboBox.getSelectedItem().toString()) ?
                cryptoComboBox.getSelectedItem().toString()+":" : ""

                , walletComboBox.getSelectedItem().toString()

                , unMineablePools.contains(poolComboBox.getSelectedItem().toString()) ?
                "#test" : ""

                , poolComboBox.getSelectedItem().toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(json);
/*        UDPClient.sendString("mining-runc");
        UDPClient.receiveString();
        UDPClient.sendString(json);*/
    }
}
