package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechRedundancyAssessment {
    private String metric;
    private double score;
    private String status; // "优"/"良"/"中"/"差"
    private String description;
}