package com.wpkg.cli.gui;

import javax.swing.*;
import java.util.Objects;

public class CryptoManager {
    public JPanel CryptoPanel;
    private JButton refreshButton;
    private JTextField textField1;
    public JComboBox<CryptoCurrencies> cryptoComboBox;
    private JLabel statusLabel;
    private JComboBox<Pools> poolComboBox;

    public enum CryptoCurrencies {
        ETC,
        ETH,
        TRX
    }
    public enum Pools{
        test
    }
    public CryptoManager() {
        cryptoComboBox.addActionListener(actionEvent -> updateCryptoManager());
    }
    void updateCryptoManager(){
        switch((CryptoCurrencies) Objects.requireNonNull(cryptoComboBox.getSelectedItem())){
            case ETC -> poolComboBox.setModel(new DefaultComboBoxModel<>(Pools.values()));
        }
    }
}
