package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.ScamReport;

@Repository
public interface ScamReportRepository extends JpaRepository<ScamReport, Long> {
    
}
