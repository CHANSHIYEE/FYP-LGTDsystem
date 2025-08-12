package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "global_locations")
public class GlobalLocation {
    public enum GlobalLocationType { RESEARCH, WAREHOUSE, FACTORY }
    public enum GlobalOperationalStatus { ACTIVE, INACTIVE, PLANNED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private GlobalLocationType type;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private GlobalCompany globalCompany;

    private Double latitude;
    private Double longitude;
    private String address;
    private String country;

    @Enumerated(EnumType.STRING)
    private GlobalOperationalStatus operationalStatus;

    private LocalDateTime createdAt;
    private String city;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GlobalLocationType getType() {
        return type;
    }

    public void setType(GlobalLocationType type) {
        this.type = type;
    }

    public GlobalCompany getGlobalCompany() {
        return globalCompany;
    }

    public void setGlobalCompany(GlobalCompany globalCompany) {
        this.globalCompany = globalCompany;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GlobalOperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(GlobalOperationalStatus operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}