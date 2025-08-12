package shiyee_FYP.fullstack_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shiyee_FYP.fullstack_backend.Service.ShippingRouteService;
import shiyee_FYP.fullstack_backend.model.DTO.PortDistanceDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ShippingRoute;
import shiyee_FYP.fullstack_backend.model.Port;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShippingRouteController {

    @Autowired
    private ShippingRouteService shippingRouteService;

    // 根据起点和终点获取最近的港口信息
    @GetMapping("/nearest")
    public ResponseEntity<List<PortDistanceDTO>> getNearestPorts(
            @RequestParam Long locationId,
            @RequestParam(required = false) Double maxDistance) {

        List<PortDistanceDTO> result = shippingRouteService
                .getNearestPortsByLocationId(locationId, maxDistance);

        return ResponseEntity.ok(result);
    }

    // 获取完整的海运路线
    @GetMapping("/shippingroute")
    public ResponseEntity<ShippingRoute> getShippingRoute(
            @RequestParam Long sourceLocationId,
            @RequestParam Long targetLocationId) {
        return ResponseEntity.ok(
                shippingRouteService.getShippingRoute(sourceLocationId, targetLocationId)
        );
    }
}
