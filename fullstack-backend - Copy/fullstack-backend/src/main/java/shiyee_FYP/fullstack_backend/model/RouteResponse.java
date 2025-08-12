package shiyee_FYP.fullstack_backend.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RouteResponse {
    private List<ShippingLane> path;
    private double totalDistance;
    private int totalTime;


    public List<ShippingLane> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getTotalTime() {
        return totalTime;
    }


}