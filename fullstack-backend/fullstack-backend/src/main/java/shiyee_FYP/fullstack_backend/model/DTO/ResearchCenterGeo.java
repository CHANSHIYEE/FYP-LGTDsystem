package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Data;

@Data
public class ResearchCenterGeo {
    private String name;
    private double latitude;
    private double longitude;

    public ResearchCenterGeo(String name, double latitude, double longitude) {
    }
}