package shiyee_FYP.fullstack_backend.Service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.DTO.ComplaintDTO;
import shiyee_FYP.fullstack_backend.model.DTO.OrderDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;
import shiyee_FYP.fullstack_backend.Service.ReputationService;
import shiyee_FYP.fullstack_backend.exception.ResourceNotFoundException;
import shiyee_FYP.fullstack_backend.model.ChinaCompany;
import shiyee_FYP.fullstack_backend.model.Complaint;
import shiyee_FYP.fullstack_backend.model.Order;
import shiyee_FYP.fullstack_backend.repository.CAL.CompanyRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.ComplaintRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.OrderRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.QualityCheckRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ReputationServiceImpl implements ReputationService {

    private final OrderRepository orderRepository;
    private final QualityCheckRepository qualityCheckRepository;
    private final ComplaintRepository complaintRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    @Override

    public ReputationMetricsDTO calculateReputationMetrics(Long companyId, LocalDate startDate, LocalDate endDate) {
        ChinaCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        // 计算违约率
        long delayedOrders = orderRepository.countDelayedOrders(companyId);
        long totalOrders = orderRepository.countTotalOrders(companyId);
        double defaultRate = totalOrders == 0 ? 0 : (double) delayedOrders / totalOrders * 100;

        // 计算合格率
        long passedChecks = qualityCheckRepository.countPassedChecks(companyId);
        long totalChecks = qualityCheckRepository.countTotalChecks(companyId);
        double qualityRate = totalChecks == 0 ? 0 : (double) passedChecks / totalChecks * 100;

        // 计算投诉率
        long complaints = complaintRepository.countComplaints(companyId);
        double complaintRate = totalOrders == 0 ? 0 : (double) complaints / totalOrders * 100;

        return ReputationMetricsDTO.builder()
                .companyId(companyId)
                .companyName(company.getName())
                .defaultRate(Math.round(defaultRate * 100.0) / 100.0)
                .qualityRate(Math.round(qualityRate * 100.0) / 100.0)
                .complaintRate(Math.round(complaintRate * 100.0) / 100.0)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public List<OrderDTO> getAllOrders(Long companyId, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByCompanyIdAndDateRange(companyId, startDate, endDate);
        return orders.stream().map(order -> {
            OrderDTO dto = modelMapper.map(order, OrderDTO.class);
            dto.setProductName(order.getProduct().getName());
            dto.setSourceLocation(order.getSourceLocation().getName());
            dto.setTargetLocation(order.getTargetLocation().getName());
            dto.setHasComplaint(!order.getComplaints().isEmpty());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getCompanyComplaints(Long companyId) {
        List<Complaint> complaints = complaintRepository.findByCompanyId(companyId);
        return complaints.stream()
                .map(complaint -> modelMapper.map(complaint, ComplaintDTO.class))
                .collect(Collectors.toList());
    }
}