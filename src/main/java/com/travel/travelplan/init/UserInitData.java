package com.travel.travelplan.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInitData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setNickName("admin");
        user.setIsVerify(true);
        user.setRole("ROLE_ADMIN");
        user.setLoginYn(true);
        
        if(!userRepository.existsByUsername(user.getUsername())) {
            userRepository.save(user);
        }
    }

}
