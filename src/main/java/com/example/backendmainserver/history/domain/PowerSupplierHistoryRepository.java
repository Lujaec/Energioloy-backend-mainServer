package com.example.backendmainserver.history.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerSupplierHistoryRepository extends JpaRepository<PowerSupplierHistory, Long> {
    @Query("SELECT psh FROM PowerSupplierHistory psh JOIN psh.port p WHERE p.room.id = :roomId")
    List<PowerSupplierHistory> findAllByRoomId(Long roomId);
}
