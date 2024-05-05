package com.example.backendmainserver.power.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerRepository extends JpaRepository<Power, Long> {
    // 이번 달의 전력 데이터 가져오기
    @Query("SELECT p FROM Power p WHERE MONTH(p.time) = MONTH(CURRENT_DATE) AND YEAR(p.time) = YEAR(CURRENT_DATE)")
    List<Power> findCurrentMonthPower();
}
