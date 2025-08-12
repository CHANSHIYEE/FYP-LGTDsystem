package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "virtual_node_results")
@Data
@AllArgsConstructor
public class VirtualNodeResult {
    @Id
    @Column(name = "node_id")
    private Integer nodeId;

    private Double longitude;
    private Double latitude;
    private Double betweenness;

    @Column(name = "is_virtual")
    private Boolean isVirtual;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    public VirtualNodeResult() {

    }

    public VirtualNodeResult(
            Integer nodeId,
            Double longitude,
            Double latitude,
            Double betweenness,
            boolean isVirtual
    ) {
        this.nodeId = nodeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.betweenness = betweenness;
        this.isVirtual = isVirtual;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getBetweenness() {
        return betweenness;
    }

    public void setBetweenness(Double betweenness) {
        this.betweenness = betweenness;
    }

    public Boolean getVirtual() {
        return isVirtual;
    }

    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}