package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
    private Long id;
    private String productName;
    private String sourceLocation;
    private String targetLocation;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private LocalDate actualDeliveryDate;
    private String status;
    private boolean hasComplaint;
}