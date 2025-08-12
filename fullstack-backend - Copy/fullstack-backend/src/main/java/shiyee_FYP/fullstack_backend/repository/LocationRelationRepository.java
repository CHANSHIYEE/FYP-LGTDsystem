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

}