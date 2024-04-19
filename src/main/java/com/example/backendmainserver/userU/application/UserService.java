package com.example.backendmainserver.userU.application;

import com.example.backendmainserver.userU.domain.User;
import com.example.backendmainserver.userU.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String join(String loginId, String password) {
        User user = User.builder()
                .login_id(loginId)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
        return "join!!";
    }
}
