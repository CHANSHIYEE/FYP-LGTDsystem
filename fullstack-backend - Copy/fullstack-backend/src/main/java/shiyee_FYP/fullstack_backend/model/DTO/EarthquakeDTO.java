package shiyee_FYP.fullstack_backend.model.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

public class EarthquakeDTO {
    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("properties")
    private Properties properties;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
// Getters and Setters

    public static class Geometry {
        @JsonProperty("coordinates")
        private double[] coordinates;

        // Getters and Setters

        public double getLongitude() {
            return coordinates[0];
        }

        public double getLatitude() {
            return coordinates[1];
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class Properties {
        @JsonProperty("mag")
        private double magnitude;

        public double getMagnitude() {
            return 0;
        }

        public void setMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }
// Getters and Setters
    }

    // 便捷方法
    public double getLatitude() {
        return geometry.getLatitude();
    }

    public double getLongitude() {
        return geometry.getLongitude();
    }

    public double getMagnitude() {
        return properties.getMagnitude();
    }
}