package com.example.backendmainserver.myroom.domain;

import com.example.backendmainserver.room.domain.Room;
import com.example.backendmainserver.user.domain.User;
import jakarta.persistence.*;

@Entity
public class MyRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
