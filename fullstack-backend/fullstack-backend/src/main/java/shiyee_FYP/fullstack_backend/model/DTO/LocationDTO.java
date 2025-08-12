package shiyee_FYP.fullstack_backend.model.DTO;

import shiyee_FYP.fullstack_backend.model.Location;

public class LocationDTO {
    private Integer id;
    private String name;
    private String type; // LocationType转为String
    private Double latitude;
    private Double longitude;
    private String companyName; // 公司名称直接存储
    private Double centrality; // 中心性分数

    public LocationDTO(Integer id, String name, String string, Double latitude, Double longitude, String external, Double vertexScore) {
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getCentrality() {
        return centrality;
    }

    public void setCentrality(Double centrality) {
        this.centrality = centrality;
    }
}