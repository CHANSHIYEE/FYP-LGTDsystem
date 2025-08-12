package shiyee_FYP.fullstack_backend.Service;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.model.ShortestPath;
import shiyee_FYP.fullstack_backend.repository.LocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.LocationRepository;
import shiyee_FYP.fullstack_backend.repository.ShortestPathRepository;

import java.util.HashSet;
import java.util.Set;


@Service
public class GraphService {

    private final LocationRelationRepository relationRepository;
    private final ShortestPathRepository shortestPathRepository;

    public GraphService(LocationRelationRepository relationRepository,
                        ShortestPathRepository shortestPathRepository) {
        this.relationRepository = relationRepository;
        this.shortestPathRepository = shortestPathRepository;
    }

    /**
     * 构建完整供应链网络图
     */
    public Graph<Location, DefaultWeightedEdge> buildFullSupplyChainGraph() {
        Graph<Location, DefaultWeightedEdge> graph =
                new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        Set<Location> allNodes = new HashSet<>();
        relationRepository.findAll().forEach(relation -> {
            allNodes.add(relation.getSource());
            allNodes.add(relation.getTarget());
        });
        allNodes.forEach(graph::addVertex);

        relationRepository.findAll().forEach(relation -> {
            Location source = relation.getSource();
            Location target = relation.getTarget();

            if (graph.containsVertex(source) && graph.containsVertex(target)) {
                DefaultWeightedEdge edge = graph.addEdge(source, target);

                shortestPathRepository.findByOriginIdAndDestinationId(
                        source.getId(), target.getId()
                ).ifPresent(path -> {
                    double weight = calculateCombinedWeight(path);
                    graph.setEdgeWeight(edge, weight);
                });
            }
        });

        return graph;
    }
    /**
     * 估算运输成本（基于距离和时间）
     * @param distanceKm 距离（公里）
     * @param timeHours 时间（小时）
     * @return 模拟运费（单位：元）
     */
    private double estimateTransportCost(double distanceKm, double timeHours) {
        // 公路运输基础参数（可配置化）
        double baseRatePerKm = 3.0;      // 元/公里
        double timeCostPerHour = 50.0;    // 元/小时（时间附加费）
        double minCost = 30.0;           // 最低起步价

        // 计算成本（距离成本 + 时间成本加权）
        double cost = (distanceKm * baseRatePerKm) + (timeHours * timeCostPerHour * 0.3);
        return Math.max(cost, minCost);  // 确保不低于最低运费
    }
    private double calculateCombinedWeight(ShortestPath path) {
        // 原始数据提取
        double distanceKm = path.getDistance() / 1000.0;  // 米转公里
        double timeHours = path.getDuration() / 3600.0;   // 秒转小时

        // 新增运费估算
        double estimatedCost = estimateTransportCost(distanceKm, timeHours);

        // 归一化处理（所有指标压缩到[0,1]范围）
        double normDist = 1 - Math.exp(-distanceKm / 500.0);     // 距离归一化
        double normTime = 1 - Math.exp(-timeHours / 10.0);       // 时间归一化
        double normCost = 1 - Math.exp(-estimatedCost / 1000.0); // 成本归一化（假设1000元为上限）

        // 调整权重分配（距离40% + 时间30% + 成本30%）
        return (0.4 * normDist + 0.3 * normTime + 0.3 * normCost) * 100;
    }




}
