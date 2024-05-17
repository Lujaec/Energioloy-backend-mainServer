package com.example.backendmainserver.myroom.domain;

import com.example.backendmainserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyRoomRepository extends JpaRepository<MyRoom, Long> {
    List<MyRoom> findAllByUser(User user);
}
