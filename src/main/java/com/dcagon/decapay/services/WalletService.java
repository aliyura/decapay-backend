package com.dcagon.decapay.services;

import com.dcagon.decapay.entities.User;
import com.dcagon.decapay.entities.Wallet;
import com.dcagon.decapay.entities.WalletLog;
import com.dcagon.decapay.enums.Status;
import com.dcagon.decapay.enums.TransactionType;
import com.dcagon.decapay.exception.AuthenticationException;
import com.dcagon.decapay.exception.ValidationException;
import com.dcagon.decapay.pojos.user.ActivationRequest;
import com.dcagon.decapay.pojos.user.AuthRequest;
import com.dcagon.decapay.pojos.user.AuthResponse;
import com.dcagon.decapay.pojos.user.SignupRequest;
import com.dcagon.decapay.pojos.wallet.WalletCreditRequest;
import com.dcagon.decapay.respositories.UserRepository;
import com.dcagon.decapay.respositories.WalletRepository;
import com.dcagon.decapay.security.JwtUtil;
import com.dcagon.decapay.utils.AppUtil;
import com.dcagon.decapay.utils.AuthDetails;
import com.dcagon.decapay.utils.LocalStorage;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class WalletService {


    private final AppUtil app;

    private final UserRepository userRepository;

    private  final WalletRepository walletRepository;

    private final AuthDetails authDetails;

    public void createWallet(Wallet wallet){
        walletRepository.save(wallet);
    }

    public Wallet creditWallet(Principal principal, WalletCreditRequest request) {
//        User user = authDetails.getAuthorizedUser(principal);
////        if(user==null)
////            throw new ValidationException("User not found");

        app.print(request);

        if(request.getAmount().intValue()==0)
          throw new ValidationException("Invalid amount");

        Wallet userWallet = walletRepository.findByWalletId(request.getWalletId()).orElse(null);
        if(userWallet==null)
            throw new ValidationException("Invalid wallet address");

        //verify payment ref


        //#

        BigDecimal balance= userWallet.getBalance();
        BigDecimal creditAmount= request.getAmount();
        BigDecimal newBalance= balance.add(creditAmount);

        WalletLog log = new WalletLog();
        log.setWalletId(request.getWalletId());
        log.setAmount(request.getAmount());
        log.setNarration("Wallet funding");
        log.setTransactionType(TransactionType.CREDIT);
        log.setUuid(userWallet.getUuid());

        userWallet.setBalance(newBalance);

        return walletRepository.save(userWallet);
    }
}
