package com.dcagon.decapay.pojos.wallet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletCreditRequest {

    private String walletId;
    private BigDecimal amount;
    private  String paymentRef;
}
