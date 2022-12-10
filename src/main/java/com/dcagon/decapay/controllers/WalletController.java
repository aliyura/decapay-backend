package com.dcagon.decapay.controllers;


import com.dcagon.decapay.pojos.ApiResponse;
import com.dcagon.decapay.pojos.user.ActivationRequest;
import com.dcagon.decapay.pojos.user.AuthRequest;
import com.dcagon.decapay.pojos.user.SignupRequest;
import com.dcagon.decapay.pojos.wallet.WalletCreditRequest;
import com.dcagon.decapay.services.SchedulerService;
import com.dcagon.decapay.services.UserService;
import com.dcagon.decapay.services.WalletService;
import com.dcagon.decapay.utils.ResponseProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/wallet")
@AllArgsConstructor
public class WalletController {

    private  final WalletService walletService;
    private  final ResponseProvider responseProvider;


    @PostMapping("/credit")
    public ResponseEntity<ApiResponse<Object>>  creditWallet(Principal principal, @RequestBody WalletCreditRequest request){
        return  responseProvider.success(walletService.creditWallet(principal,request));
    }
}
