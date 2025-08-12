package shiyee_FYP.fullstack_backend.model.DTO;


public class ShippingLaneRequest {
    private Long originPortId;
    private Long destPortId;

    // Getters and Setters
    public Long getOriginPortId() {
        return originPortId;
    }

    public void setOriginPortId(Long originPortId) {
        this.originPortId = originPortId;
    }

    public Long getDestPortId() {
        return destPortId;
    }

    public void setDestPortId(Long destPortId) {
        this.destPortId = destPortId;
    }
}
