package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Port {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String country;
        private Double latitude;
        private Double longitude;
        private Boolean hasContainer;
        private Boolean hasOilTerminal;
        private Boolean hasFuel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Boolean getHasContainer() {
        return hasContainer;
    }

    public void setHasContainer(Boolean hasContainer) {
        this.hasContainer = hasContainer;
    }

    public Boolean getHasOilTerminal() {
        return hasOilTerminal;
    }

    public void setHasOilTerminal(Boolean hasOilTerminal) {
        this.hasOilTerminal = hasOilTerminal;
    }

    public Boolean getHasFuel() {
        return hasFuel;
    }

    public void setHasFuel(Boolean hasFuel) {
        this.hasFuel = hasFuel;
    }


}