package shiyee_FYP.fullstack_backend.model;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "shortest_path")
public class ShortestPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "origin_id")
    private Integer originId;

    @Column(name = "destination_id")
    private Integer destinationId;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "path_coordinates", columnDefinition = "LONGTEXT")
    private String pathCoordinates;
    // 添加解析方法
    public List<Coordinate> parsePathCoordinates() {
        if (StringUtils.isEmpty(pathCoordinates)) {
            return Collections.emptyList();
        }

        return Arrays.stream(pathCoordinates.split(";"))
                .map(coord -> {
                    String[] parts = coord.split(",");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid coordinate format: " + coord);
                    }
                    return new Coordinate(
                            Double.parseDouble(parts[0]),
                            Double.parseDouble(parts[1])
                    );
                })
                .collect(Collectors.toList());
    }

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public ShortestPath(Integer id, Integer originId, Integer destinationId, Double distance, Double duration, String pathCoordinates, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.originId = originId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.duration = duration;
        this.pathCoordinates = pathCoordinates;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ShortestPath() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPathCoordinates() {
        return pathCoordinates;
    }

    public void setPathCoordinates(String pathCoordinates) {
        this.pathCoordinates = pathCoordinates;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
