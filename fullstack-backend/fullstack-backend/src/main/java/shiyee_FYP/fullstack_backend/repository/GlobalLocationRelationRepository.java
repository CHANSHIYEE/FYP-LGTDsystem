package shiyee_FYP.fullstack_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalLocationRelationRepository extends JpaRepository<GlobalLocationRelation, Long> {
    List<GlobalLocationRelation> findBySourceIdAndTargetId(Long sourceId, Long targetId);
    Optional<GlobalLocationRelation> findById(Long id);

}

