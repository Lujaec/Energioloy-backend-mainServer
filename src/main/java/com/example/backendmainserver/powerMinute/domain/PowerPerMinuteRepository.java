package com.example.backendmainserver.powerMinute.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerPerMinuteRepository extends JpaRepository<PowerPerMinute, Long> {
}
