package shiyee_FYP.fullstack_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shiyee_FYP.fullstack_backend.Service.GlobalMapService;
import shiyee_FYP.fullstack_backend.Service.GlobalSupplyChainService;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;

import java.util.List;

@RestController
@RequestMapping("/api/global/locations")
public class GlobalLocationController {
    private final GlobalSupplyChainService globalSupplyChainService;
    private final GlobalMapService globalMapService;

    @Autowired
    public GlobalLocationController(GlobalSupplyChainService globalSupplyChainService,
                                    GlobalMapService globalMapService) {
        this.globalSupplyChainService = globalSupplyChainService;
        this.globalMapService = globalMapService;
    }

    @GetMapping("/{id}/relations")
    public List<GlobalLocationRelation> getGlobalLocationRelations(@PathVariable Integer id) {
        return globalSupplyChainService.getGlobalRelationsBySourceId(id);
    }

}
