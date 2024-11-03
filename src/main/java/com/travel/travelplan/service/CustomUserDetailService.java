package com.travel.travelplan.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userData = userRepository.findByUsername(username);

        if (userData != null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
