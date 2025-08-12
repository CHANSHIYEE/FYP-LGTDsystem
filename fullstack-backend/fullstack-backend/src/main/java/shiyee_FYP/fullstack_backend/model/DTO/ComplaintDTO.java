package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ComplaintDTO {
    private Long id;
    private Long orderId;
    private String complaintType;
    private LocalDate complaintDate;
    private String description;
    private String status;
}