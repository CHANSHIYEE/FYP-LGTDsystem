package shiyee_FYP.fullstack_backend.model;

public class RouteCalculationResult {
    private String sourceToPortDirection;
    private String shippingRoute;
    private String portToTargetDirection;

    // Getter and Setter methods
    public String getSourceToPortDirection() {
        return sourceToPortDirection;
    }

    public void setSourceToPortDirection(String sourceToPortDirection) {
        this.sourceToPortDirection = sourceToPortDirection;
    }

    public String getShippingRoute() {
        return shippingRoute;
    }

    public void setShippingRoute(String shippingRoute) {
        this.shippingRoute = shippingRoute;
    }

    public String getPortToTargetDirection() {
        return portToTargetDirection;
    }

    public void setPortToTargetDirection(String portToTargetDirection) {
        this.portToTargetDirection = portToTargetDirection;
    }
}
