package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class ResilienceDetailDTO {
    private Integer locationId;
    private String locationName;
    private Double resilienceScore; // 百分比

    // 各项指标得分
    private Double betweennessCentralityScore;
    private Double redundancyScore;
    private Double vulnerabilityScore;

    // 声誉评分详情
    private Double complaintRate;
    private Double qualityRate;
    private Double fulfillmentRate;
    private Double reputationScore;

    // 计算时间
    private LocalDateTime calculationDate;
}