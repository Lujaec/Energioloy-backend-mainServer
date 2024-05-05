package com.example.backendmainserver.user.presentation;

import com.example.backendmainserver.user.application.UserService;
import com.example.backendmainserver.user.application.dto.request.JoinRequest;
import com.example.backendmainserver.user.application.dto.request.LoginRequest;
import com.example.backendmainserver.user.application.dto.response.JoinResponse;
import com.example.backendmainserver.user.application.dto.response.LoginResponse;
import com.example.backendmainserver.global.response.SuccessCode;
import com.example.backendmainserver.global.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입 api")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<JoinResponse>> join(@RequestBody JoinRequest req) {
        return SuccessResponse.of(SuccessCode.USER_CREATED, userService.join(req));
    }

    @Operation(summary = "로그인 api")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody LoginRequest req) {
        return SuccessResponse.of(SuccessCode.OK, userService.login((req)));
    }
}
