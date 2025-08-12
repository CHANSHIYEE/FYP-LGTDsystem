package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReputationMetricsDTO {
    private Long companyId;
    private String companyName;
    private double defaultRate;    // 违约率(%)
    private double qualityRate;   // 合格率(%)
    private double complaintRate; // 投诉率(%)
    private LocalDate startDate;
    private LocalDate endDate;
}