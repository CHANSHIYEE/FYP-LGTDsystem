package shiyee_FYP.fullstack_backend.model.DTO;

import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.Port;
import shiyee_FYP.fullstack_backend.model.ShippingLane;

import java.util.*;
import java.util.stream.Collectors;

public class ShippingRoute {
    private GlobalLocation sourceLocation;
    private Port sourcePort;

    private Port targetPort;
    private GlobalLocation targetLocation;
    private List<String> directions;
    private List<String> landDirectionsToSourcePort;
    private List<String> landDirectionsFromTargetPort;
    private List<Port> viaPorts; // 中转港口列表
    private String shippingDescription;
    private ShippingLane shippingLane; // 不是Optional

    public void setShippingLane(ShippingLane shippingLane) {
        this.shippingLane = shippingLane;
    }// 明确初始化

    // 全参数构造函数
    public ShippingRoute(GlobalLocation sourceLocation, Port sourcePort,
                         ShippingLane shippingLane, Port targetPort,
                         GlobalLocation targetLocation, List<String> directions,
                         List<String> landDirectionsToSourcePort,
                         List<String> landDirectionsFromTargetPort) {
        this.sourceLocation = sourceLocation;
        this.sourcePort = sourcePort;
        this.targetPort = targetPort;
        this.targetLocation = targetLocation;
        this.directions = directions;
        this.landDirectionsToSourcePort = landDirectionsToSourcePort;
        this.landDirectionsFromTargetPort = landDirectionsFromTargetPort;
    }

    // 无参构造函数
    public ShippingRoute() {
    }


    // 解析中转港口ID字符串
    private List<Port> parseViaPorts(String viaPortsStr, List<Port> allPorts) {
        if (viaPortsStr == null || viaPortsStr.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> viaPortIds = Arrays.stream(viaPortsStr.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        return allPorts.stream()
                .filter(port -> viaPortIds.contains(port.getId()))
                .collect(Collectors.toList());
    }

    // 生成航线描述
    // 修改生成描述的方法
    private String generateShippingDescription(ShippingLane lane) {
        if (lane.getViaPorts() == null || lane.getViaPorts().isEmpty()) {
            return String.format("直达航线，距离%.2f公里，预计%d天",
                    lane.getDistance(), lane.getEstimatedTime());
        } else {
            String viaNames = viaPorts.stream()
                    .map(Port::getName)
                    .collect(Collectors.joining(","));
            return String.format("经%s中转，总距离%.2f公里，预计%d天",
                    viaNames, lane.getDistance(), lane.getEstimatedTime());
        }
    }

    public GlobalLocation getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(GlobalLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public Port getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Port sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Port getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Port targetPort) {
        this.targetPort = targetPort;
    }

    public GlobalLocation getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(GlobalLocation targetLocation) {
        this.targetLocation = targetLocation;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public List<String> getLandDirectionsToSourcePort() {
        return landDirectionsToSourcePort;
    }

    public void setLandDirectionsToSourcePort(List<String> landDirectionsToSourcePort) {
        this.landDirectionsToSourcePort = landDirectionsToSourcePort;
    }

    public List<String> getLandDirectionsFromTargetPort() {
        return landDirectionsFromTargetPort;
    }

    public void setLandDirectionsFromTargetPort(List<String> landDirectionsFromTargetPort) {
        this.landDirectionsFromTargetPort = landDirectionsFromTargetPort;
    }

    public List<Port> getViaPorts() {
        return viaPorts;
    }

    public void setViaPorts(List<Port> viaPorts) {
        this.viaPorts = viaPorts;
    }

    public String getShippingDescription() {
        return shippingDescription;
    }

    public void setShippingDescription(String shippingDescription) {
        this.shippingDescription = shippingDescription;
    }

    public ShippingLane getShippingLane() {
        return shippingLane;
    }
}
