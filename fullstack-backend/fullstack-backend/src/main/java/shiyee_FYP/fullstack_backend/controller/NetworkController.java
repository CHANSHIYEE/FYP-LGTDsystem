package shiyee_FYP.fullstack_backend.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.http.ResponseEntity;
import shiyee_FYP.fullstack_backend.Service.AnalysisService;
import shiyee_FYP.fullstack_backend.Service.GraphService;
import shiyee_FYP.fullstack_backend.Utils.GraphUtils;
import shiyee_FYP.fullstack_backend.model.DTO.LocationDTO;
import shiyee_FYP.fullstack_backend.model.Location;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supply-chain")
public class NetworkController {

    private final GraphService graphService;
    private final AnalysisService analysisService;

    public NetworkController(GraphService graphService, AnalysisService analysisService) {
        this.graphService = graphService;
        this.analysisService = analysisService;
    }

    /**
     * 获取完整网络拓扑数据
     * @return 包含节点和边的完整网络结构
     */
    @GetMapping("/full-network")
    public ResponseEntity<Map<String, Object>> getFullNetwork() {
        Graph<Location, DefaultWeightedEdge> graph = graphService.buildFullSupplyChainGraph();

        // 构建节点数据
        List<Map<String, Object>> nodes = graph.vertexSet().stream()
                .map(this::convertLocationToMap)
                .collect(Collectors.toList());

        // 构建边数据
        List<Map<String, Object>> edges = graph.edgeSet().stream()
                .map(edge -> {
                    Map<String, Object> edgeMap = new HashMap<>();
                    edgeMap.put("source", graph.getEdgeSource(edge).getId());
                    edgeMap.put("target", graph.getEdgeTarget(edge).getId());
                    edgeMap.put("weight", graph.getEdgeWeight(edge));
                    return edgeMap;
                })
                .collect(Collectors.toList());

        // 返回统一结构
        return ResponseEntity.ok(Map.of(
                "nodes", nodes,
                "edges", edges,
                "metrics", Map.of(
                        "nodeCount", nodes.size(),
                        "edgeCount", edges.size(),
                        "density", GraphUtils.calculateDensity(graph)
                )
        ));
    }

    /**
     * 转换Location为前端友好格式
     */
    private Map<String, Object> convertLocationToMap(Location location) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", location.getId());
        node.put("name", location.getName());
        node.put("type", location.getType().toString());
        node.put("latitude", location.getLatitude());
        node.put("longitude", location.getLongitude());
        node.put("companyId", location.getCompanyId());
        return node;
    }

    /**
     * 获取所有节点的中介中心性得分（完整列表）
     */
    @GetMapping("/betweenness-centrality")
    public ResponseEntity<List<LocationCentralityDTO>> getAllBetweennessCentrality() {
        Map<Location, Double> centralityScores = analysisService.calculateBetweennessCentrality();

        List<LocationCentralityDTO> result = centralityScores.entrySet().stream()
                .sorted(Map.Entry.<Location, Double>comparingByValue().reversed())
                .map(entry -> new LocationCentralityDTO(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getType().toString(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * DTO用于返回节点ID、名称、类型和中心性得分
     */
    @Data
    @AllArgsConstructor
    public static class LocationCentralityDTO {
        private Integer locationId;
        private String locationName;
        private String locationType;
        private Double centralityScore;
    }


}