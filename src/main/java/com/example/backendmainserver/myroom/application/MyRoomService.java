package com.example.backendmainserver.myroom.application;

import com.example.backendmainserver.myroom.domain.MyRoom;
import com.example.backendmainserver.myroom.domain.MyRoomRepository;
import com.example.backendmainserver.room.domain.Room;
import com.example.backendmainserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyRoomService {
    private final MyRoomRepository myRoomRepository;

    public List<Room> getRoomListOfUser(User user){
        List<MyRoom> allByUser = myRoomRepository.findAllByUser(user);

        List<Room> ret = allByUser.stream().map(
                MyRoom::getRoom
        ).collect(Collectors.toList());

        return ret;
    }
}
