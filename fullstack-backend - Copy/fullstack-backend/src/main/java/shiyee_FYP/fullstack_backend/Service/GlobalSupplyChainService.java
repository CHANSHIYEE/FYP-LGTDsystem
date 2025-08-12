package shiyee_FYP.fullstack_backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.GlobalCompany;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;
import shiyee_FYP.fullstack_backend.repository.GlobalCompanyRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;

import java.util.List;

@Service
public class GlobalSupplyChainService {
    private final GlobalCompanyRepository globalCompanyRepository;
    private final GlobalLocationRepository globalLocationRepository;
    private final GlobalLocationRelationRepository globalLocationRelationRepository;

    @Autowired
    public GlobalSupplyChainService(GlobalCompanyRepository globalCompanyRepository,
                                    GlobalLocationRepository globalLocationRepository,
                                    GlobalLocationRelationRepository globalLocationRelationRepository) {
        this.globalCompanyRepository = globalCompanyRepository;
        this.globalLocationRepository = globalLocationRepository;
        this.globalLocationRelationRepository = globalLocationRelationRepository;
    }

    public List<GlobalCompany> getAllGlobalCompanies() {
        return globalCompanyRepository.findAll();
    }

    public List<GlobalLocation> getGlobalLocationsByCompanyId(Long globalCompanyId) {
        return globalLocationRepository.findByGlobalCompanyId(globalCompanyId);
    }

    public List<GlobalLocationRelation> getGlobalRelationsBySourceId(Integer globalSourceId) {
        return globalLocationRelationRepository.findBySourceId(globalSourceId);
    }
}
