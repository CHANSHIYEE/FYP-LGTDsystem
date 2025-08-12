package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Data;

@Data
public class LocationTypeStats {
    private String type;
    private int totalCount;
    private int backedUpCount;
    private double backupRate;
    private double geoDispersion;
}
