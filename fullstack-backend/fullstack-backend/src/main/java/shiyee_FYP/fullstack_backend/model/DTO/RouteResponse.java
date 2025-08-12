package shiyee_FYP.fullstack_backend.model.DTO;

public class RouteResponse {
    private Long relationId;
    private RouteSegment landRouteToPort;
    private SeaRouteSegment seaRoute;
    private RouteSegment landRouteFromPort;
    private String status;
    // getters and setters

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public RouteSegment getLandRouteToPort() {
        return landRouteToPort;
    }

    public void setLandRouteToPort(RouteSegment landRouteToPort) {
        this.landRouteToPort = landRouteToPort;
    }

    public SeaRouteSegment getSeaRoute() {
        return seaRoute;
    }

    public void setSeaRoute(SeaRouteSegment seaRoute) {
        this.seaRoute = seaRoute;
    }

    public RouteSegment getLandRouteFromPort() {
        return landRouteFromPort;
    }

    public void setLandRouteFromPort(RouteSegment landRouteFromPort) {
        this.landRouteFromPort = landRouteFromPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}