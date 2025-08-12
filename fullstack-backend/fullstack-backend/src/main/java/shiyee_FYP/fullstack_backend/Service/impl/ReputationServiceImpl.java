package shiyee_FYP.fullstack_backend.Service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.Service.AnalysisService;
import shiyee_FYP.fullstack_backend.model.DTO.ComplaintDTO;
import shiyee_FYP.fullstack_backend.model.DTO.LocationDTO;
import shiyee_FYP.fullstack_backend.model.DTO.OrderDTO;
import shiyee_FYP.fullstack_backend.Service.ReputationService;
import shiyee_FYP.fullstack_backend.exception.ResourceNotFoundException;
import shiyee_FYP.fullstack_backend.model.ChinaCompany;
import shiyee_FYP.fullstack_backend.model.Complaint;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;
import shiyee_FYP.fullstack_backend.model.Order;
import shiyee_FYP.fullstack_backend.repository.CAL.CompanyRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.ComplaintRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.OrderRepository;
import shiyee_FYP.fullstack_backend.repository.CAL.QualityCheckRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import shiyee_FYP.fullstack_backend.model.*;
import shiyee_FYP.fullstack_backend.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReputationServiceImpl implements ReputationService {
    private final OrderRepository orderRepository;
    private final QualityCheckRepository qualityCheckRepository;
    private final ComplaintRepository complaintRepository;
    private final CompanyRepository companyRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final CompanyReputationScoreRepository scoreRepository;

    private static final Map<Location.LocationType, Double> NODE_WEIGHTS = Map.of(
            Location.LocationType.RESEARCH, 0.8,
            Location.LocationType.WAREHOUSE, 0.6,
            Location.LocationType.FACTORY, 0.9,
            Location.LocationType.SUPPLIER, 0.85,
            Location.LocationType.PROTOTYPE, 0.75,
            Location.LocationType.DISTRIBUTION, 0.7,
            Location.LocationType.MANUFACTURER, 0.8,
            Location.LocationType.TESTING, 0.65
    );
    @Autowired
    private AnalysisService analysisService;

    private Set<Integer> getCriticalNodeIds() {
        return analysisService.getCriticalNodes().stream()
                .map(LocationDTO::getId)
                .collect(Collectors.toSet());
    }

    private final CompanyReputationScoreRepository companyReputationScoreRepository;

    @Override
    public List<CompanyReputationScore> getReputationHistory(Long companyId) {
        return companyReputationScoreRepository.findAllByCompanyIdOrderByCalculationTime(companyId);
    }

    @Override
    public List<CompanyReputationScore> getReputationScoreHistory(Long companyId) {
        return scoreRepository.findByCompanyIdOrderByCalculationTimeDesc(companyId);
    }


    @Override
    public ReputationMetricsDTO calculateReputationMetrics(Long companyId) {
        ChinaCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        // === 基础指标 ===

        long delayedOrders = orderRepository.countDelayedOrdersByCompany(companyId);
        long totalOrders = orderRepository.countTotalOrdersByCompany(companyId);
        double defaultRate = totalOrders == 0 ? 0 : (double) delayedOrders / totalOrders;
        double fulfillmentRate = 1 - defaultRate;

        long passedChecks = qualityCheckRepository.countPassedChecksByCompany(companyId);
        long totalChecks = qualityCheckRepository.countTotalChecksByCompany(companyId);
        double qualityRate = totalChecks == 0 ? 0 : (double) passedChecks / totalChecks;

        long complaints = complaintRepository.countByCompany(companyId);
        double complaintRate = totalOrders == 0 ? 0 : (double) complaints / totalOrders;

        double reputationScore = 0.3 * (1 - complaintRate) + 0.4 * qualityRate + 0.3 * fulfillmentRate;

        // === 节点信息 ===

        List<Location> locations = locationRepository.findByCompanyId(companyId);
        int total = locations.size();
        if (total == 0) {
            return ReputationMetricsDTO.builder()
                    .companyId(companyId)
                    .companyName(company.getName())
                    .defaultRate(0.0)
                    .qualityRate(0.0)
                    .complaintRate(0.0)
                    .fulfillmentRate(0.0)
                    .reputationScore(0.0)
                    .resilienceScore(0.0)
                    .overallScore(0.0)
                    .calculationTime(LocalDateTime.now())
                    .build();
        }

        // === 脆弱性得分 ===
        long vulnerable = locations.stream()
                .filter(loc -> loc.getVulnerabilityScore() != null && loc.getVulnerabilityScore() < 0.5)
                .count();
        double vulnerabilityScore = 1.0 - (double) vulnerable / total;

        // === 关键节点得分 ===
        Set<Integer> criticalNodeIds = getCriticalNodeIds(); // 引用 AnalysisService
        long criticalCount = locations.stream()
                .filter(loc -> criticalNodeIds.contains(loc.getId()))
                .count();
        double criticalNodeScore = 1.0 - (double) criticalCount / total;

        // === 冗余度得分 ===
        Map<Location.LocationType, Long> typeCounts = locations.stream()
                .collect(Collectors.groupingBy(Location::getType, Collectors.counting()));
        double redundancyScore = typeCounts.values().stream()
                .mapToDouble(count -> count > 1 ? 1.0 : 0.0)
                .average().orElse(0.0);

        // === 韧性整合评分 ===
        double resilienceScore = 0.33 * vulnerabilityScore + 0.33 * criticalNodeScore + 0.34 * redundancyScore;

        // === 总评分：四项等权重 ===
        double overallScore = 0.25 * reputationScore +
                0.25 * vulnerabilityScore +
                0.25 * criticalNodeScore +
                0.25 * redundancyScore;

        CompanyReputationScore scoreEntity = CompanyReputationScore.builder()
                .companyId(companyId)
                .defaultRate(defaultRate)
                .qualityRate(qualityRate)
                .complaintRate(complaintRate)
                .fulfillmentRate(fulfillmentRate)
                .reputationScore(reputationScore)
                .vulnerabilityScore(vulnerabilityScore)
                .criticalNodeScore(criticalNodeScore)
                .redundancyScore(redundancyScore)
                .overallScore(overallScore)
                .calculationTime(LocalDateTime.now())
                .build();

        scoreRepository.save(scoreEntity);

        return ReputationMetricsDTO.builder()
                .companyId(companyId)
                .companyName(company.getName())
                .defaultRate(roundTwoDecimals(defaultRate * 100))
                .qualityRate(roundTwoDecimals(qualityRate * 100))
                .complaintRate(roundTwoDecimals(complaintRate * 100))
                .fulfillmentRate(roundTwoDecimals(fulfillmentRate * 100))
                .reputationScore(roundTwoDecimals(reputationScore * 100))
                .resilienceScore(roundTwoDecimals((vulnerabilityScore + criticalNodeScore + redundancyScore) / 3 * 100))
                .overallScore(roundTwoDecimals(overallScore * 100))
                .calculationTime(LocalDateTime.now())
                .build();
    }



    private double calculateResilienceScore(List<Location> locations) {
        if (locations == null || locations.isEmpty()) return 0.0;

        double coveredWeight = locations.stream()
                .map(Location::getType)
                .filter(NODE_WEIGHTS::containsKey)
                .mapToDouble(NODE_WEIGHTS::get)
                .sum();

        double totalWeight = NODE_WEIGHTS.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        return totalWeight == 0 ? 0.0 : coveredWeight / totalWeight;
    }

    @Override
    public List<ComplaintDTO> getCompanyComplaints(Long companyId) {
        List<Complaint> complaints = complaintRepository.findByOrderProductCompanyId(companyId);
        return complaints.stream()
                .map(complaint -> modelMapper.map(complaint, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders(Long companyId) {
        List<Order> orders = orderRepository.findByProductCompanyId(companyId);
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
    public Double getReputationScore(Long companyId) {
        return calculateReputationMetrics(companyId).getReputationScore() / 100.0;
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
