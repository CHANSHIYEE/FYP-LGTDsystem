package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "shipping_lanes")
public class ShippingLane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer fid;
    private Integer objectId;
    private String type;

    @Column(columnDefinition = "GEOMETRY")
    private Geometry geom;

    private Double distance;
    private Integer estimatedTime;

    @ManyToOne
    @JoinColumn(name = "start_port_id")
    private Port startPort;

    @ManyToOne
    @JoinColumn(name = "end_port_id")
    private Port endPort;
    private String viaPorts; // 中转港口ID，用逗号分隔，如 "123,456"

    public String getViaPorts() {
        return viaPorts;
    }

    public void setViaPorts(String viaPorts) {
        this.viaPorts = viaPorts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Port getStartPort() {
        return startPort;
    }

    public void setStartPort(Port startPort) {
        this.startPort = startPort;
    }

    public Port getEndPort() {
        return endPort;
    }

    public void setEndPort(Port endPort) {
        this.endPort = endPort;
    }
}