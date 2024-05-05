package com.example.backendmainserver.domain.user.application;

import com.example.backendmainserver.domain.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.domain.user.application.dto.request.LoginRequest;
import com.example.backendmainserver.domain.user.application.dto.response.JoinResponse;
import com.example.backendmainserver.domain.user.application.dto.response.LoginResponse;
import com.example.backendmainserver.domain.user.domain.User;
import com.example.backendmainserver.domain.user.domain.UserRepository;
import com.example.backendmainserver.global.exception.DuplicateUserException;
import com.example.backendmainserver.global.exception.GlobalException;
import com.example.backendmainserver.global.response.ErrorCode;
//import com.example.backendmainserver.global.security.dto.UserDetailsImpl;
import com.example.backendmainserver.global.security.jwt.JwtProvider;
import com.example.backendmainserver.port.application.PortService;
import com.example.backendmainserver.room.domain.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService  {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final PortService portService;

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

        System.out.println(req.loginId());
        System.out.println(req.password());
        User user = userRepository.findByLoginId(req.loginId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        if (passwordEncoder.matches(req.password(), user.getPassword())==false) {
            throw new GlobalException(ErrorCode.WRONG_USER_PASSWORD);
        }

        log.info("로그인 1차 성공");
//        UserDetailsImpl userDetail = UserDetailsImpl.fromMember(user);

        String newAccessToken = jwtProvider.createAccessToken(user.getId());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
//        log.info("at" + newAccessToken);

            return LoginResponse.builder()
                    .userId(user.getId())
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .nickname(user.getNickname())
                    .build();

    }

    public User getUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty())
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);

        return userOptional.get();
    }

    public List<Long> getPortsId(Long userId){
        User user = getUser(userId);
        Room room = user.getRoom();

        return portService.getPortsId(room);
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
