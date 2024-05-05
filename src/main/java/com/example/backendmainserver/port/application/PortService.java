package com.example.backendmainserver.port.application;

import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PortRepository;
import com.example.backendmainserver.room.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortService {
    private final PortRepository portRepository;

    public List<Long>  getPortsId(Room room){
        List<Port> ports = portRepository.getAllByRoom(room);

        List<Long> portIds = ports.stream().map((e) -> {
            return e.getId();
        }).toList();

        return portIds;
    }
}
