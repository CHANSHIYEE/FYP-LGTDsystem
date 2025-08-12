package shiyee_FYP.fullstack_backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resilience_score_history")
public class ResilienceScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "betweenness_centrality_score")
    private Double betweennessCentralityScore;

    @Column(name = "redundancy_score")
    private Double redundancyScore;

    @Column(name = "vulnerability_score")
    private Double vulnerabilityScore;

    @Column(name = "reputation_score")
    private Double reputationScore;

    @Column(name = "complaint_rate")
    private Double complaintRate;

    @Column(name = "quality_rate")
    private Double qualityRate;

    @Column(name = "fulfillment_rate")
    private Double fulfillmentRate;

    @Column(name = "resilience_score")
    private Double resilienceScore;

    @Column(name = "calculation_date")
    private LocalDateTime calculationDate;

    // 构造函数
    public ResilienceScoreHistory() {
    }

    // Getters 和 Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Double getBetweennessCentralityScore() {
        return betweennessCentralityScore;
    }

    public void setBetweennessCentralityScore(Double betweennessCentralityScore) {
        this.betweennessCentralityScore = betweennessCentralityScore;
    }

    public Double getRedundancyScore() {
        return redundancyScore;
    }

    public void setRedundancyScore(Double redundancyScore) {
        this.redundancyScore = redundancyScore;
    }

    public Double getVulnerabilityScore() {
        return vulnerabilityScore;
    }

    public void setVulnerabilityScore(Double vulnerabilityScore) {
        this.vulnerabilityScore = vulnerabilityScore;
    }

    public Double getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(Double reputationScore) {
        this.reputationScore = reputationScore;
    }

    public Double getComplaintRate() {
        return complaintRate;
    }

    public void setComplaintRate(Double complaintRate) {
        this.complaintRate = complaintRate;
    }

    public Double getQualityRate() {
        return qualityRate;
    }

    public void setQualityRate(Double qualityRate) {
        this.qualityRate = qualityRate;
    }

    public Double getFulfillmentRate() {
        return fulfillmentRate;
    }

    public void setFulfillmentRate(Double fulfillmentRate) {
        this.fulfillmentRate = fulfillmentRate;
    }

    public Double getResilienceScore() {
        return resilienceScore;
    }

    public void setResilienceScore(Double resilienceScore) {
        this.resilienceScore = resilienceScore;
    }

    public LocalDateTime getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDateTime calculationDate) {
        this.calculationDate = calculationDate;
    }

    // toString() 方法
    @Override
    public String toString() {
        return "ResilienceScoreHistory{" +
                "id=" + id +
                ", locationId=" + locationId +
                ", betweennessCentralityScore=" + betweennessCentralityScore +
                ", redundancyScore=" + redundancyScore +
                ", vulnerabilityScore=" + vulnerabilityScore +
                ", reputationScore=" + reputationScore +
                ", complaintRate=" + complaintRate +
                ", qualityRate=" + qualityRate +
                ", fulfillmentRate=" + fulfillmentRate +
                ", resilienceScore=" + resilienceScore +
                ", calculationDate=" + calculationDate +
                '}';
    }
}