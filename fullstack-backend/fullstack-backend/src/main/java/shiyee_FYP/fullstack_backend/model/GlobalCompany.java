package shiyee_FYP.fullstack_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Table(name = "global_companies")
@Entity
public class GlobalCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String industry;
    private int foundedYear;
    private String headquarters;
    private String headquartersCountry;
    private int employeeCount;
    private double revenue;
    private String description;
    private boolean isMultinational;

    @OneToMany(mappedBy = "company")
    private List<GlobalLocation> locations;

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getHeadquartersCountry() {
        return headquartersCountry;
    }

    public void setHeadquartersCountry(String headquartersCountry) {
        this.headquartersCountry = headquartersCountry;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMultinational() {
        return isMultinational;
    }

    public void setMultinational(boolean multinational) {
        isMultinational = multinational;
    }

    public List<GlobalLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GlobalLocation> locations) {
        this.locations = locations;
    }

    // Getters and setters
}

