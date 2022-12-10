package com.dcagon.decapay.services;

import com.dcagon.decapay.entities.User;
import com.dcagon.decapay.entities.Wallet;
import com.dcagon.decapay.enums.Status;
import com.dcagon.decapay.exception.AuthenticationException;
import com.dcagon.decapay.exception.ValidationException;
import com.dcagon.decapay.pojos.user.ActivationRequest;
import com.dcagon.decapay.pojos.user.AuthRequest;
import com.dcagon.decapay.pojos.user.AuthResponse;
import com.dcagon.decapay.pojos.user.SignupRequest;
import com.dcagon.decapay.respositories.UserRepository;
import com.dcagon.decapay.security.JwtUtil;
import com.dcagon.decapay.utils.AppUtil;
import com.dcagon.decapay.utils.LocalStorage;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {


    private final AppUtil app;

    private final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

    private final AuthenticationManager auth;

    private final JwtUtil jwtUtil;

    private final LocalStorage localStorage;

    private  final  WalletService walletService;

    public User createUser(SignupRequest request) {

        if(!app.validEmail(request.getEmailAddress()))
            throw new ValidationException("Invalid email address");

        boolean userExist = userRepository.existsByEmailAddress(request.getEmailAddress());
        if(userExist)
            throw new ValidationException("User already exist");


        String userId=app.generateSerialNumber("usr");

        User newUser = app.getMapper().convertValue(request, User.class);
        newUser.setUuid(userId);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setStatus(Status.INACTIVE);

        //generate a activation or OTP for the user
        String token = app.generateSerialNumber("v");
        localStorage.save(request.getEmailAddress(), token, 0);

        //nest thing is to send the OTP or the token to the user email or Phone
            app.print("Token:");
            app.print(token);
        //end of email

        Wallet newWallet = new Wallet();
        newWallet.setBalance(BigDecimal.ZERO);
        newWallet.setWalletId(app.generateSerialNumber("0"));
        newWallet.setUuid(userId);

        newUser.setWalletId(newWallet.getWalletId());
        walletService.createWallet(newWallet);
        return userRepository.save(newUser);
    }


    public  User activateUser(ActivationRequest request){
        if(!app.validEmail(request.getEmailAddress()))
            throw new ValidationException("Invalid email address");

        User existingUser = userRepository.findByEmailAddress(request.getEmailAddress()).orElse(null);
        if(existingUser==null)
            throw new ValidationException("User not found");


        String systemToken = localStorage.getValueByKey(request.getEmailAddress());
        if(systemToken==null)
            throw new ValidationException("Token expired");

        if(!systemToken.equalsIgnoreCase(request.getToken()))
            throw new ValidationException("Invalid token");

        existingUser.setStatus(Status.ACTIVE);
        existingUser.setUpdatedDate( new Date());

       return  userRepository.save(existingUser);
    }

    public String resendToken(String email){
        if(!app.validEmail(email))
            throw new ValidationException("Invalid email address");

        User existingUser = userRepository.findByEmailAddress(email).orElse(null);
        if(existingUser==null)
            throw new ValidationException("User not found");

        //generate a activation or OTP for the user
        String token = app.generateSerialNumber("v");
        localStorage.save(email, token, 0);

        //nest thing is to send the OTP or the token to the user email or Phone
        app.print("Token:");
        app.print(token);
        //end of email

        return "Token has been sent successfully";
    }

    public AuthResponse signIn(AuthRequest loginRequest) {
        try {
            Authentication authentication = auth.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmailAddress(loginRequest.getEmailAddress()).orElse(null);
                if (user != null) {

                    if(user.getStatus().equals(Status.INACTIVE))
                        throw new ValidationException("User not active");

                        user.setLastLoginDate(new Date());
                        userRepository.save(user);

                        app.print(loginRequest);

                     String accessToken=jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                                    loginRequest.getEmailAddress(), loginRequest.getPassword(), new ArrayList<>()));


                        AuthResponse loginResponse =new AuthResponse();
                        user.setPassword("*****************");
                        loginResponse.setUser(user);
                        loginResponse.setToken(accessToken);
                        return loginResponse;
                } else {
                     throw new AuthenticationException("Invalid Login Credentials");
                }
            } else {
                throw new AuthenticationException("Invalid Username or Password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        }
    }
}
