
package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//public class ReputationMetricsDTO {
//    private Long companyId;
//    private String companyName;
//    private double defaultRate;    // 违约率(%)
//    private double qualityRate;   // 合格率(%)
//    private double complaintRate; // 投诉率(%)
//    private LocalDate startDate;
//    private LocalDate endDate;
//}

public class ReputationMetricsDTO {
    private Long companyId;
    private String companyName;
    private Double defaultRate;        // 违约率(%)
    private Double qualityRate;       // 合格率(%)
    private Double complaintRate;     // 投诉率(%)
    private Double fulfillmentRate;   // 履约率(%)
    private Double reputationScore;   // 统一声誉评分(%)
    private Double resilienceScore;
    private Double overallScore;
    private LocalDateTime calculationTime; // 计算时间戳
}