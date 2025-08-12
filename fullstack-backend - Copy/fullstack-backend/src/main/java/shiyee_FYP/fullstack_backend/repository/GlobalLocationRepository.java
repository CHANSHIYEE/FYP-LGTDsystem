package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;

import java.util.List;

@Repository
public interface GlobalLocationRepository extends JpaRepository<GlobalLocation, Long> {
    // 根据公司ID查找所有位置
    List<GlobalLocation> findByGlobalCompanyId(Long globalCompanyId);

    // 根据公司ID和地点类型查找位置
    List<GlobalLocation> findByGlobalCompanyIdAndType(Long globalCompanyId, GlobalLocation.GlobalLocationType type);

    // 根据国家查找位置
    List<GlobalLocation> findByCountry(String country);




}
