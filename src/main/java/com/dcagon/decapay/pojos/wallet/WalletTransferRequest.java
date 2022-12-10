package com.dcagon.decapay.pojos.wallet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletTransferRequest {

    private String sourceWalletId;

    private String destWalletId;
    private BigDecimal amount;
}
