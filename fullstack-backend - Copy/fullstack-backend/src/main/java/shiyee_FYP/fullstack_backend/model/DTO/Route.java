package shiyee_FYP.fullstack_backend.model.DTO;

import shiyee_FYP.fullstack_backend.model.Coordinate;

import java.util.List;

public class Route {
    private double distance;
    private double duration;
    private List<Coordinate> path;
    private String transportMode;

    public Route(double distance, double duration, List<Coordinate> path, String transportMode) {
        this.distance = distance;
        this.duration = duration;
        this.path = path;
        this.transportMode = transportMode;
    }

    public Route() {

    }

    // Getters
    public double getDistance() { return distance; }
    public double getDuration() { return duration; }
    public List<Coordinate> getPath() { return path; }
    public String getTransportMode() { return transportMode; }
}