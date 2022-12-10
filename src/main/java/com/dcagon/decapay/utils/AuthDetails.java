package com.dcagon.decapay.utils;
import com.dcagon.decapay.entities.User;
import com.dcagon.decapay.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthDetails {

    private final UserRepository userRepository;

    public User getAuthorizedUser(Principal principal){
        if(principal!=null) {
            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            return userRepository.findByEmailAddress(currentUser.getUsername()).orElse(null);
        }
        else{
            return  null;
        }
    }
}
