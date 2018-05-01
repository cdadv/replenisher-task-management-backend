package com.walmart.labs.repository;

import com.walmart.labs.domain.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {
  Corporation findByName(String name);
}
