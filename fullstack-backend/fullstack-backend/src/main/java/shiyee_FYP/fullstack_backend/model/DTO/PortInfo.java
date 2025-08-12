package shiyee_FYP.fullstack_backend.model.DTO;
public class PortInfo {
    private String locode;
    private String name;
    private double latitude;
    private double longitude;

    // 新增全参构造函数
    public PortInfo(String locode, String name, double latitude, double longitude) {
        this.locode = locode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // 保留原有getter/setter
    public String getLocode() {
        return locode;
    }

    public void setLocode(String locode) {
        this.locode = locode;
    }

}