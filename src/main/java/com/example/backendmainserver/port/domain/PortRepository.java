package com.example.backendmainserver.port.domain;

import com.example.backendmainserver.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortRepository extends JpaRepository<Port, Long> {
    List<Port> getAllByRoom(Room room);
}
