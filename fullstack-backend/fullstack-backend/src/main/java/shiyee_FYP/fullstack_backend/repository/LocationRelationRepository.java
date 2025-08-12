package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.LocRelation;
import shiyee_FYP.fullstack_backend.model.Location;

import java.util.List;

public interface LocationRelationRepository extends JpaRepository<LocRelation, Integer> {

    List<LocRelation> findBySourceOrTarget(Location source, Location target);

    List<LocRelation> findBySourceAndRelationTypeOrTargetAndRelationType(
            Location source, LocRelation.RelationType sourceType,
            Location target, LocRelation.RelationType targetType);

        // 自定义查询方法：根据源地点查找关系
        List<LocRelation> findBySource(Location source);

        // 自定义查询方法：根据目标地点查找关系
        List<LocRelation> findByTarget(Location target);

        // 查找两个地点之间的所有关系
        List<LocRelation> findBySourceAndTarget(Location source, Location target);


    List<LocRelation> findBySourceIdOrTargetId(Integer sourceId, Integer targetId);

}