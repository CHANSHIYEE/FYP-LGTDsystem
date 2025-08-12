package shiyee_FYP.fullstack_backend.Service;

import shiyee_FYP.fullstack_backend.model.CompanyReputationScore;
import shiyee_FYP.fullstack_backend.model.DTO.ComplaintDTO;
import shiyee_FYP.fullstack_backend.model.DTO.OrderDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReputationService {
    List<CompanyReputationScore> getReputationScoreHistory(Long companyId);

    //    ReputationMetricsDTO calculateReputationMetrics(Long companyId, LocalDate startDate, LocalDate endDate);
//    List<OrderDTO> getAllOrders(Long companyId, LocalDate startDate, LocalDate endDate);
//    List<ComplaintDTO> getCompanyComplaints(Long companyId);
    ReputationMetricsDTO calculateReputationMetrics(Long companyId); // 移除了LocalDate参数
    List<OrderDTO> getAllOrders(Long companyId);
    List<ComplaintDTO> getCompanyComplaints(Long companyId);
//    Double getReputationScore(Long companyId);

    // 添加新方法
    Double getReputationScore(Long companyId);
    List<CompanyReputationScore> getReputationHistory(Long companyId);

}