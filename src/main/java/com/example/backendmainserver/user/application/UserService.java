package com.example.backendmainserver.user.application;

import com.example.backendmainserver.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.user.application.dto.response.JoinResponse;
import com.example.backendmainserver.user.domain.User;
import com.example.backendmainserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinResponse join(JoinRequest req) {
        User user = User.builder()
                .login_id(req.loginId())
                .password(passwordEncoder.encode(req.password()))
                .nickname(req.nickname())
                .email(req.email())
                .phoneNumber(req.phoneNumber())
                .build();
        userRepository.save(user);
        return JoinResponse.builder()
                        .loginId(req.loginId())
                        .build();
    }
}
