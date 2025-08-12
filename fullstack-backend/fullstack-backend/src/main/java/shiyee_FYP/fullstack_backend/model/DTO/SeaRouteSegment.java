package shiyee_FYP.fullstack_backend.model.DTO;
public class SeaRouteSegment {
    private String originPort;
    private String destinationPort;
    private String originLocode;
    private String destinationLocode;
    private String routeGeometry;
    private double distance; // in nautical miles
    private double duration; // in hours
    // getters and setters

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getOriginLocode() {
        return originLocode;
    }

    public void setOriginLocode(String originLocode) {
        this.originLocode = originLocode;
    }

    public String getDestinationLocode() {
        return destinationLocode;
    }

    public void setDestinationLocode(String destinationLocode) {
        this.destinationLocode = destinationLocode;
    }

    public String getRouteGeometry() {
        return routeGeometry;
    }

    public void setRouteGeometry(String routeGeometry) {
        this.routeGeometry = routeGeometry;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}