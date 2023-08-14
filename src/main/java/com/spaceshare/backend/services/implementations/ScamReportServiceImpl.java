package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.ScamReport;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.ScamReportRepository;
import com.spaceshare.backend.services.ScamReportService;

@Service
public class ScamReportServiceImpl implements ScamReportService {

    /*** Repositories ***/
    @Autowired
    ScamReportRepository scamReportRepository;

    @Autowired
    PropertyRepository propertyRepository;

    /*** Methods ***/
    @Override
    public List<ScamReport> getAllScamReports() {
        return scamReportRepository.findAll();
    }

    @Override
    public Boolean createScamReport(Long propertyId) {
        try {
            Property property = propertyRepository.findById(propertyId).orElse(null);

            if (property != null) {
                ScamReport scamReport = new ScamReport();
                scamReport.setProperty(property);
                scamReport.setCreatedAt(LocalDate.now());
                scamReportRepository.saveAndFlush(scamReport);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean approveScamReport(Long id) {
        try {
            ScamReport scamReport = scamReportRepository.findById(id).orElse(null);

            if(scamReport != null) {
                scamReport.setStatus(ApproveStatus.APPROVED);
                scamReport.setUpdatedAt(LocalDate.now());
                scamReportRepository.saveAndFlush(scamReport);
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean declineScamReport(Long id) {
        try {
            ScamReport scamReport = scamReportRepository.findById(id).orElse(null);

            if(scamReport != null) {
                scamReport.setStatus(ApproveStatus.DECLINED);
                scamReport.setUpdatedAt(LocalDate.now());
                scamReportRepository.saveAndFlush(scamReport);
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean deleteScamReport(Long id) {
        try {
            ScamReport scamReport = scamReportRepository.findById(id).orElse(null);

            if(scamReport != null) {
                scamReportRepository.delete(scamReport);
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ScamReport getScamReportsStatus() {
        try {
            Long approved = scamReportRepository.findAll().stream().filter(s -> s.getStatus() == ApproveStatus.APPROVED).count();
            Long pending = scamReportRepository.findAll().stream().filter(s -> s.getStatus() == ApproveStatus.PENDING).count();

            ScamReport scamReport = new ScamReport();
            scamReport.setApprovedCount(approved.intValue());
            scamReport.setPendingCount(pending.intValue());
            return scamReport;

        } catch (Exception e) {
            return null;
        }
    }
}
