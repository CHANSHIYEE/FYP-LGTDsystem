package shiyee_FYP.fullstack_backend.controller;

import org.springframework.web.bind.annotation.*;
import shiyee_FYP.fullstack_backend.Service.ShortestPathService;
import shiyee_FYP.fullstack_backend.model.ShortestPath;

@RestController
@RequestMapping("/api/shortest-paths")
public class ShortestPathController {
    private final ShortestPathService shortestPathService;

    public ShortestPathController(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @PostMapping("/calculate-all")
    public void calculateAllShortestPaths() {
        shortestPathService.calculateAllShortestPaths();
    }

    @PostMapping("/calculate-for-location/{locationId}")
    public void calculateShortestPathsForLocation(@PathVariable Integer locationId) {  // 改为 Integer
        shortestPathService.calculateShortestPathsForLocation(locationId);
    }

    @GetMapping("/{originId}/{destinationId}")
    public ShortestPath getShortestPath(
            @PathVariable Integer originId,
            @PathVariable Integer destinationId) {
        return shortestPathService.getShortestPath(originId, destinationId);
    }
}