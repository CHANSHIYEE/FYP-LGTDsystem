package shiyee_FYP.fullstack_backend.repository;

import shiyee_FYP.fullstack_backend.model.PathCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathCacheRepository extends JpaRepository<PathCache, Long> {
    PathCache findByRelationId(Long relationId);
    boolean existsByRelationId(Long relationId);
}