package shiyee_FYP.fullstack_backend.model;

import lombok.Data;
import java.util.List;

@Data
public class AmapRouteResponse {
    private int status;       // 0=失败，1=成功
    private String info;      // 状态信息

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    private Route route;     // 路径数据

    @Data
    public static class Route {
        private String origin;       // 起点坐标
        private String destination; // 终点坐标

        public List<Path> getPaths() {
            return paths;
        }

        public void setPaths(List<Path> paths) {
            this.paths = paths;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        private List<Path> paths;   // 路径方案列表
    }

    @Data
    public static class Path {
        private Double distance;    // 总距离(米)
        private Double duration;    // 预计时间(秒)
        private String strategy;    // 策略类型

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }

        public String getStrategy() {
            return strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }

        private List<Step> steps;   // 路径步骤
    }

    @Data
    public static class Step {
        private String instruction; // 行驶指示

        public String getPolyline() {
            return polyline;
        }

        public void setPolyline(String polyline) {
            this.polyline = polyline;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        private String polyline;    // 轨迹点串(格式: "经度,纬度;经度,纬度")

    }
}
