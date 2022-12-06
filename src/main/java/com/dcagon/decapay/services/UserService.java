package com.dcagon.decapay.services;

import com.dcagon.decapay.entities.User;
import com.dcagon.decapay.enums.Status;
import com.dcagon.decapay.exception.AuthenticationException;
import com.dcagon.decapay.exception.ValidationException;
import com.dcagon.decapay.pojos.user.AuthRequest;
import com.dcagon.decapay.pojos.user.AuthResponse;
import com.dcagon.decapay.pojos.user.SignupRequest;
import com.dcagon.decapay.respositories.UserRepository;
import com.dcagon.decapay.security.JwtUtil;
import com.dcagon.decapay.utils.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {


    private final AppUtil app;

    private final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

    private final AuthenticationManager auth;

    private final JwtUtil jwtUtil;

    public User createUser(SignupRequest request) {

        if(!app.validEmail(request.getEmailAddress()))
            throw new ValidationException("Invalid email address");

        boolean userExist = userRepository.existsByEmailAddress(request.getEmailAddress());
        if(userExist)
            throw new ValidationException("User already exist");

        User newUser = app.getMapper().convertValue(request, User.class);
        newUser.setUuid(app.generateSerialNumber("usr"));
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setStatus(Status.INACTIVE);

        return userRepository.save(newUser);
    }

    public AuthResponse signIn(AuthRequest loginRequest) {
        try {
            Authentication authentication = auth.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmailAddress(loginRequest.getEmailAddress()).orElse(null);
                if (user != null) {
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
            throw new AuthenticationException("Invalid Username or Password");
        }
    }
}
