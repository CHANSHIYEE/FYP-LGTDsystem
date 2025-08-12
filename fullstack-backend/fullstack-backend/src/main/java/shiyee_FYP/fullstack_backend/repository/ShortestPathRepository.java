package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shiyee_FYP.fullstack_backend.model.ShortestPath;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShortestPathRepository extends JpaRepository<ShortestPath, Integer> {  // 改为 Integer
    boolean existsByOriginIdAndDestinationId(Integer originId, Integer destinationId);  // 改为 Integer
    Optional<ShortestPath> findByOriginIdAndDestinationId(Integer originId, Integer destinationId);

    @Query("SELECT DISTINCT sp.originId FROM ShortestPath sp")
    Set<Integer> findAllOriginIds();


    @Query("SELECT DISTINCT sp.destinationId FROM ShortestPath sp")
    Set<Integer> findAllDestinationIds();

    default Set<Integer> findAllUniqueLocationIds() {
        Set<Integer> ids = findAllOriginIds();
        ids.addAll(findAllDestinationIds());
        return ids;
    }// 改为 Integer
    // 添加这个方法以便Controller访问
    List<ShortestPath> findAll();

}