package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "globallocation_relations")
public class GlobalLocationRelation {
    public enum GlobalTransportMode { SHIPPING, AIR, ROAD, RAIL, PIPELINE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private GlobalLocation source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private GlobalLocation target;

    private String relationType;

    @Enumerated(EnumType.STRING)
    private GlobalTransportMode transportMode;

    private Double distanceKm;
    private Double estimatedDurationHours;
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GlobalLocation getSource() {
        return source;
    }

    public void setSource(GlobalLocation source) {
        this.source = source;
    }

    public GlobalLocation getTarget() {
        return target;
    }

    public void setTarget(GlobalLocation target) {
        this.target = target;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public GlobalTransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(GlobalTransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(Double estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}