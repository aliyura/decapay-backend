package com.dcagon.decapay.services;

import com.dcagon.decapay.entities.User;
import com.dcagon.decapay.respositories.UserRepository;
import com.dcagon.decapay.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final AppUtil app;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser= userRepository.findByEmailAddress(username).orElse(null);
        return new  org.springframework.security.core.userdetails.User(appUser.getEmailAddress(), appUser.getPassword(), new ArrayList<>());
    }
}
