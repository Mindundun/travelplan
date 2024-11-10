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
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setNickName("admin");
        admin.setIsVerify(true);
        admin.setRole("ROLE_ADMIN");
        admin.setLoginYn(true);
        
        userRepository.findByUsername(admin.getUsername()).ifPresentOrElse(
            (existUser) -> {/* nothing */},
            () -> {
                userRepository.save(admin);
            }
        );

        User user = new User();
        user.setUsername("tjdtlr12349@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setNickName("user");
        user.setIsVerify(true);
        user.setRole("ROLE_USER");
        user.setLoginYn(true);

        userRepository.findByUsername(user.getUsername()).ifPresentOrElse(
            (existUser) -> {/* nothing */},
            () -> {
                userRepository.save(user);
            }
        );
    }

}
