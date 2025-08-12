package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "betweenness_centrality")
public class BetweennessCentrality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location_id", nullable = false, unique = true)
    private Integer locationId;

    @Column(name = "centrality_value", nullable = false)
    private Double centralityValue;

    @Column(name = "normalized_value")
    private Double normalizedValue;

    private Integer rank;

    @Column(name = "calculation_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date calculationTime;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Double getCentralityValue() {
        return centralityValue;
    }

    public void setCentralityValue(Double centralityValue) {
        this.centralityValue = centralityValue;
    }

    public Double getNormalizedValue() {
        return normalizedValue;
    }

    public void setNormalizedValue(Double normalizedValue) {
        this.normalizedValue = normalizedValue;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Date getCalculationTime() {
        return calculationTime;
    }

    public void setCalculationTime(Date calculationTime) {
        this.calculationTime = calculationTime;
    }
}
