package com.example.backendmainserver.user.domain;

import com.example.backendmainserver.room.domain.Room;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserVO {
    private Long id;

    private String loginId;

    private String password;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Room room;

    private Role role;

    private String fcmToken;

    public static UserVO buildUserVO(User user){
        return UserVO.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .room(user.getRoom())
                .role(user.getRole())
                .fcmToken(user.getFcmToken())
                .build();
    }

}
