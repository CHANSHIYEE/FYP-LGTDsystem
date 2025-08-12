package shiyee_FYP.fullstack_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shiyee_FYP.fullstack_backend.Service.GlobalSupplyChainService;
import shiyee_FYP.fullstack_backend.model.GlobalCompany;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;

import java.util.List;

@RestController
@RequestMapping("/api/global/companies")
public class GlobalCompanyController {
    private final GlobalSupplyChainService globalSupplyChainService;

    @Autowired
    public GlobalCompanyController(GlobalSupplyChainService globalSupplyChainService) {
        this.globalSupplyChainService = globalSupplyChainService;
    }

    @GetMapping
    public List<GlobalCompany> getAllGlobalCompanies() {
        return globalSupplyChainService.getAllGlobalCompanies();
    }

    @GetMapping("/{id}/locations")
    public List<GlobalLocation> getGlobalCompanyLocations(@PathVariable Long id) {
        return globalSupplyChainService.getGlobalLocationsByCompanyId(id);
    }
}