package com.dcagon.decapay.entities;
import com.dcagon.decapay.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Document("wallet_logs")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class WalletLog implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    private String id;
    @Indexed(unique = true)
    private String uuid;
    @Indexed(unique = true)
    private String walletId;
    @Indexed(unique = true)
    private String sourceWalletId;

    private String narration;
    @Column(nullable = false, length = 50)
    private BigDecimal amount;
    @Column(nullable = false, length = 10)
    private TransactionType transactionType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public WalletLog(){
        this.createdDate= new Date();
        this.updatedDate = new Date();
    }
    @PrePersist
    private void setCreatedAt() {
        createdDate = new Date();
    }
    @PreUpdate
    private void setUpdatedAt() {
        updatedDate = new Date();
    }

}
