package shiyee_FYP.fullstack_backend.Service;

import shiyee_FYP.fullstack_backend.model.DTO.ComplaintDTO;
import shiyee_FYP.fullstack_backend.model.DTO.OrderDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReputationService {
    ReputationMetricsDTO calculateReputationMetrics(Long companyId, LocalDate startDate, LocalDate endDate);
    List<OrderDTO> getAllOrders(Long companyId, LocalDate startDate, LocalDate endDate);
    List<ComplaintDTO> getCompanyComplaints(Long companyId);
}