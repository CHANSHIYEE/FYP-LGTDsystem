package shiyee_FYP.fullstack_backend.model.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarehouseDTO {
    private Long id;
    private String name;
    private String code;
    private String country;
    private String city;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String type;
    private String capacity;
    private String contactInfo;
}