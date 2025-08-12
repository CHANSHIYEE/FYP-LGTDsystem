package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistanceResponse {
    private Long portId1;
    private Long portId2;
    private double distanceNauticalMiles;
}