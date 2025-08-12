package shiyee_FYP.fullstack_backend.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.ChinaCompany;
import shiyee_FYP.fullstack_backend.model.DTO.LocationDTO;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.repository.ChinaCompanyRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final GraphService graphService;
    private final ChinaCompanyRepository companyRepository;

    // 使用Integer作为键的Company缓存
    private Map<Integer, String> companyNameMap;

    @PostConstruct
    public void initCompanyMap() {
        companyNameMap = companyRepository.findAll().stream()
                .collect(Collectors.toMap(
                        company -> company.getId().intValue(),
                        ChinaCompany::getName,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    public Map<Location, Double> calculateBetweennessCentrality() {
        Graph<Location, DefaultWeightedEdge> graph = graphService.buildFullSupplyChainGraph();
        BetweennessCentrality<Location, DefaultWeightedEdge> centrality;
        try {
            centrality = new BetweennessCentrality<>(graph, true);
        } catch (NoSuchMethodError e) {
            centrality = new BetweennessCentrality<>(graph);
        }
        return graph.vertexSet().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        centrality::getVertexScore,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    public List<LocationDTO> getCriticalNodes() {
        Map<Location, Double> centrality = calculateBetweennessCentrality();

        return centrality.entrySet().stream()
                .sorted(Map.Entry.<Location, Double>comparingByValue().reversed())
                .limit((int) Math.ceil(centrality.size() * 0.1))
                .map(entry -> convertToDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private LocationDTO convertToDTO(Location location, Double centralityScore) {
        return new LocationDTO(
                location.getId(),
                location.getName(),
                location.getType().toString(),
                location.getLatitude(),
                location.getLongitude(),
                location.getCompanyId() != null ?
                        companyNameMap.get(location.getCompanyId()) : "External",
                centralityScore
        );
    }



}