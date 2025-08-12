package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;

import java.util.List;

@Repository
public interface GlobalLocationRelationRepository extends JpaRepository<GlobalLocationRelation, Integer> {
    // 根据源位置ID查找所有关系
    List<GlobalLocationRelation> findBySourceId(Integer sourceId);
    List<GlobalLocationRelation> findAll();

    // 根据目标位置ID查找所有关系
    List<GlobalLocationRelation> findByTargetId(Integer targetId);

    // 根据运输方式查找关系
    List<GlobalLocationRelation> findByTransportMode(GlobalLocationRelation.GlobalTransportMode transportMode);

    // 根据源位置ID和目标位置ID查找关系
    List<GlobalLocationRelation> findBySourceIdAndTargetId(Integer sourceId, Long targetId);

}