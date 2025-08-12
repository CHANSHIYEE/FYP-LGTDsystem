package shiyee_FYP.fullstack_backend.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.exception.ResourceNotFoundException;
import shiyee_FYP.fullstack_backend.model.CompanyReputationScore;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ResilienceDetailDTO;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.model.ResilienceScoreHistory;
import shiyee_FYP.fullstack_backend.repository.CompanyReputationScoreRepository;
import shiyee_FYP.fullstack_backend.repository.LocationRepository;
import shiyee_FYP.fullstack_backend.repository.ResilienceScoreHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResilienceService {
    private final LocationRepository locationRepo;
    private final ResilienceScoreHistoryRepository historyRepo;
    private final AnalysisService analysisService;
    private final LocationRedundancyService redundancyService;
    private final ReputationService reputationService;



    @Transactional
    public void calculateAndSaveAllResilienceScores() {
        List<Location> locations = locationRepo.findAll();
        Map<Location, Double> centralityScores = analysisService.calculateBetweennessCentrality();
        double maxCentrality = centralityScores.values().stream().max(Double::compare).orElse(1.0);

        locations.forEach(location -> {
            // 获取各项指标
            double centrality = centralityScores.getOrDefault(location, 0.0) / maxCentrality;
            Double redundancy = redundancyService.getRedundancyScore(location.getId());
            double vulnerability = location.getVulnerabilityScore();

            // 修复类型问题：将Integer companyId转换为Long
            Long companyId = location.getCompanyId() != null ?
                    location.getCompanyId().longValue() : null;

            Double reputation = companyId != null ?
                    reputationService.getReputationScore(companyId) : null;

            // 获取声誉详情（同样修复类型问题）
            ReputationMetricsDTO reputationMetrics = companyId != null ?
                    reputationService.calculateReputationMetrics(companyId) : null;

            // 计算韧性评分
            double resilienceScore = 0.25 * centrality
                    + 0.25 * (redundancy != null ? redundancy : 0)
                    + 0.25 * (1 - vulnerability)
                    + 0.25 * (reputation != null ? reputation : 0);

            // 保存到历史记录
            ResilienceScoreHistory history = new ResilienceScoreHistory();
            history.setLocationId(location.getId());
            history.setBetweennessCentralityScore(centrality);
            history.setRedundancyScore(redundancy);
            history.setVulnerabilityScore(vulnerability);
            history.setReputationScore(reputation);

            if(reputationMetrics != null) {
                history.setComplaintRate(reputationMetrics.getComplaintRate() / 100.0);
                history.setQualityRate(reputationMetrics.getQualityRate() / 100.0);
                history.setFulfillmentRate(reputationMetrics.getFulfillmentRate() / 100.0);
            }

            history.setResilienceScore(resilienceScore * 100); // 转换为百分比
            history.setCalculationDate(LocalDateTime.now());

            historyRepo.save(history);
        });
    }

    public List<ResilienceScoreHistory> getResilienceHistory(Integer locationId) {
        return historyRepo.findByLocationIdOrderByCalculationDateDesc(locationId);
    }

    public ResilienceScoreHistory getLatestResilienceScore(Integer locationId) {
        return historyRepo.findTopByLocationIdOrderByCalculationDateDesc(locationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resilience score not found for location: " + locationId));
    }

    public ResilienceDetailDTO getResilienceDetail(Integer locationId) {
        ResilienceScoreHistory history = getLatestResilienceScore(locationId);
        Location location = locationRepo.findById(locationId).orElseThrow();

        return ResilienceDetailDTO.builder()
                .locationId(locationId)
                .locationName(location.getName())
                .resilienceScore(history.getResilienceScore())
                .betweennessCentralityScore(history.getBetweennessCentralityScore() * 100)
                .redundancyScore(history.getRedundancyScore() != null ?
                        history.getRedundancyScore() * 100 : null)
                .vulnerabilityScore(history.getVulnerabilityScore() * 100)
                .complaintRate(history.getComplaintRate() * 100)
                .qualityRate(history.getQualityRate() * 100)
                .fulfillmentRate(history.getFulfillmentRate() * 100)
                .reputationScore(history.getReputationScore() != null ?
                        history.getReputationScore() * 100 : null)
                .calculationDate(history.getCalculationDate())
                .build();
    }



}