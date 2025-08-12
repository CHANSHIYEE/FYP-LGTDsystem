package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.LocRelation;
import shiyee_FYP.fullstack_backend.model.Location;

import java.util.List;
import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findAllByOrderByIdAsc();
    List<Location> findByCompanyId(Long companyId);

    @Query("SELECT l FROM Location l WHERE l.id IN (:ids)")
    List<Location> findByIds(@Param("ids") List<Integer> ids);
    @Query("SELECT id FROM Location")
    Set<Integer> findAllLocationIds();
    @Modifying
    @Query("UPDATE Location l SET l.vulnerabilityScore = :score WHERE l.id = :id")
    void updateVulnerabilityScore(@Param("id") int id, @Param("score") double score);

    @Query("SELECT l FROM Location l WHERE l.vulnerabilityScore >= :threshold ORDER BY l.vulnerabilityScore DESC")
    List<Location> findByVulnerabilityScoreGreaterThanEqual(double threshold);
    List<Location> findByVulnerabilityScoreGreaterThanEqualOrderByVulnerabilityScoreDesc(Double threshold);

    // 或者使用@Query注解的方式
    @Query("SELECT l FROM Location l WHERE l.vulnerabilityScore >= :threshold ORDER BY l.vulnerabilityScore DESC")
    List<Location> findHighRiskLocations(@Param("threshold") Double threshold);

    List<Location> findAllByOrderByVulnerabilityScoreDesc();
}