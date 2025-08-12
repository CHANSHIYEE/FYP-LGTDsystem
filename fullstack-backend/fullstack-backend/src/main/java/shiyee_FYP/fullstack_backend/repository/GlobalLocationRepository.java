package shiyee_FYP.fullstack_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;

import java.util.List;

@Repository
public interface GlobalLocationRepository extends JpaRepository<GlobalLocation, Long> {
    List<GlobalLocation> findByCompanyId(Long companyId);
    List<GlobalLocation> findAll();
    // 只查询 id, name, latitude, longitude 等字段
    @Query("SELECT g FROM GlobalLocation g WHERE g.company.id = :companyId")
    List<GlobalLocation> findLocationsByCompanyId(@Param("companyId") Long companyId);


}

