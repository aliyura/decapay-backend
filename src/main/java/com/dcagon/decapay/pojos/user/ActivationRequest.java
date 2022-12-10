package com.dcagon.decapay.pojos.user;
import lombok.Data;

@Data
public class ActivationRequest {
    private String emailAddress;
    private String token;
}
