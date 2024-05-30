package com.example.backendmainserver.history.application;

import com.example.backendmainserver.history.application.dto.CreatePowerSupplierHistoryDto;
import com.example.backendmainserver.history.domain.PowerSupplierHistory;
import com.example.backendmainserver.history.domain.PowerSupplierHistoryRepository;
import com.example.backendmainserver.port.application.PortService;
import com.example.backendmainserver.port.domain.Port;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PowerSupplierHistoryService {
    private final PowerSupplierHistoryRepository powerSupplierHistoryRepository;
    private final PortService portService;

    @Transactional
    public void createHistory(CreatePowerSupplierHistoryDto dto) {
        Port port = portService.getPortById(dto.portId());

        PowerSupplierHistory history = PowerSupplierHistory.builder()
                .time(dto.time())
                .port(port)
                .beforePowerSupplier(dto.beforePowerSupplier())
                .afterPowerSupplier(dto.afterPowerSupplier())
                .description(dto.description())
                .build();

        powerSupplierHistoryRepository.save(history);
    }

    public List<PowerSupplierHistory> getHistoriesByRoomId(Long roomId) {
        List<PowerSupplierHistory> histories = powerSupplierHistoryRepository.findAllByRoomId(roomId,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return histories;
    }

    public List<PowerSupplierHistory> getAllHistories() {
        List<PowerSupplierHistory> histories = powerSupplierHistoryRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return histories;
    }
}
