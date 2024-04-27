package com.example.backendmainserver.domain.user.application;

import com.example.backendmainserver.domain.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.domain.user.application.dto.request.LoginRequest;
import com.example.backendmainserver.domain.user.application.dto.response.JoinResponse;
import com.example.backendmainserver.domain.user.application.dto.response.LoginResponse;
import com.example.backendmainserver.domain.user.domain.User;
import com.example.backendmainserver.domain.user.domain.UserRepository;
import com.example.backendmainserver.global.exception.DuplicateUserException;
import com.example.backendmainserver.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JoinResponse join(JoinRequest req) {
        if (userRepository.findByLoginId(req.loginId()).isPresent()) {
                throw new DuplicateUserException(ErrorCode.DUPLICATE_USER_NAME);
    //            throw new IllegalStateException("이미 존재하는 회원입니다!");
        }
        User user = User.builder()
                .loginId(req.loginId())
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

    public LoginResponse login(LoginRequest req) {


        User user = userRepository.findByLoginId(req.loginId())
                .orElseThrow(() -> new IllegalStateException("유저업슴"));
        if (passwordEncoder.matches(req.password(), user.getPassword())==false) {
            throw new IllegalStateException("비번틀림");
        }
//
            return LoginResponse.builder()
                    .userId(user.getId())
                    .build();

    }

//    @Override
//    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
//        User user = userRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalStateException("유저업슴"));
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getLoginId())
//                .password(user.getPassword())
//                .roles("ROLE??")
//                .build();
//
//    }
}
