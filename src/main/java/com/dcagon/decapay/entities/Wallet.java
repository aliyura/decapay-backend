package com.dcagon.decapay.entities;

import com.dcagon.decapay.enums.Status;
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

@Document("wallets")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Wallet implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    private String id;
    @Indexed(unique = true)
    private String uuid;

    @Indexed(unique = true)
    private String walletId;
    @Column(nullable = false, length = 50)
    private BigDecimal balance;
    @Column(nullable = false, length = 10)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Wallet(){
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
