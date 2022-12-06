package com.dcagon.decapay.pojos.user;
import lombok.Data;

@Data
public class AuthRequest {
    private String emailAddress;
    private String password;
}
