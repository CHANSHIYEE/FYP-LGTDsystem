package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_reputation_score")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyReputationScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "default_rate")
    private Double defaultRate;

    @Column(name = "quality_rate")
    private Double qualityRate;

    @Column(name = "complaint_rate")
    private Double complaintRate;

    @Column(name = "fulfillment_rate")
    private Double fulfillmentRate;

    @Column(name = "reputation_score")
    private Double reputationScore;

    @Column(name = "vulnerability_score")
    private Double vulnerabilityScore;

    @Column(name = "critical_node_score")
    private Double criticalNodeScore;

    @Column(name = "redundancy_score")
    private Double redundancyScore;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "calculation_time")
    private LocalDateTime calculationTime;
}

