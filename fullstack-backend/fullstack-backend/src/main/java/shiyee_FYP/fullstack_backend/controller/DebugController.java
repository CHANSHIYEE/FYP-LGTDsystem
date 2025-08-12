package shiyee_FYP.fullstack_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shiyee_FYP.fullstack_backend.Service.ShippingRouteService;

import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {
    @Autowired
    private ShippingRouteService shippingRouteService;

    @GetMapping("/test-cache/{relationId}")
    public String testCache(@PathVariable Long relationId) {
        Map<String, Object> result = shippingRouteService.getFullShippingRoute(relationId);
        return "执行完成，检查控制台日志和数据库";
    }
}