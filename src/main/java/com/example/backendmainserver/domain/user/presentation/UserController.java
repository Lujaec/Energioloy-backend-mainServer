package com.example.backendmainserver.domain.user.presentation;

import com.example.backendmainserver.domain.user.application.UserService;
import com.example.backendmainserver.domain.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.domain.user.application.dto.request.LoginRequest;
import com.example.backendmainserver.domain.user.application.dto.response.JoinResponse;
import com.example.backendmainserver.domain.user.application.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입 api")
    @PostMapping("")
    public JoinResponse join(@RequestBody JoinRequest req) {
        return userService.join(req);
    }

    @Operation(summary = "로그인 api")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return userService.login(req);
    }
}
