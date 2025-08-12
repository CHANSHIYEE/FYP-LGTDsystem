package shiyee_FYP.fullstack_backend.model.DTO;

import shiyee_FYP.fullstack_backend.model.GlobalLocation;

public class ShippingRouteRequest {
    private GlobalLocation origin;
    private GlobalLocation destination;

    // Getters and Setters
    public GlobalLocation getOrigin() {
        return origin;
    }

    public void setOrigin(GlobalLocation origin) {
        this.origin = origin;
    }

    public GlobalLocation getDestination() {
        return destination;
    }

    public void setDestination(GlobalLocation destination) {
        this.destination = destination;
    }
}
