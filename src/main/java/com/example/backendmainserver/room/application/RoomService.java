package com.example.backendmainserver.room.application;

import com.example.backendmainserver.global.exception.GlobalException;
import com.example.backendmainserver.global.response.ErrorCode;
import com.example.backendmainserver.room.domain.Room;
import com.example.backendmainserver.room.domain.RoomRepository;
import com.example.backendmainserver.user.domain.Role;
import com.example.backendmainserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    /*
    * Admin -> 모든 방에서 원하는 방 선택 가능
    * User -> 자기가 속하는 방만 지정 가능 (0으로 설정하면 자기 자신에 대한 방)
    * Admin 계정은 -> 모든 포트를 갖고 있는 방을 갖고 있어야함
    * */
    public Room getRoomOfUser(final User user, final Long roomId){
        List<Room> rooms = roomRepository.findAll();

        if(roomId == 0)
            return user.getRoom();
        else {
            if(user.getRole().equals(Role.ADMIN)){
                Room room = getRoomById(rooms, roomId);
                return room;
            }
            else {
                Room room = user.getRoom();
                if (!room.getId().equals(roomId))
                    throw new GlobalException(ErrorCode.USER_NOT_HAVE_ROOM);
                return user.getRoom();
            }
        }
    }

    private Room getRoomById(List<Room> rooms, Long roomId) {
        return rooms.stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(ErrorCode.ROOM_NOT_FOUND));
    }
}
