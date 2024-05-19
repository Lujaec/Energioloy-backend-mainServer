package com.example.backendmainserver.port.application;

import com.example.backendmainserver.port.domain.Port;
import com.example.backendmainserver.port.domain.PortRepository;
import com.example.backendmainserver.room.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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

    public Port getPortById(Long portId){
        Optional<Port> optionalPort = portRepository.findById(portId);

        if (optionalPort.isEmpty())
            throw new IllegalArgumentException("유효하지 않은 포트 식별자 입니다. portId = " + portId);

        return optionalPort.get();
    }
}
