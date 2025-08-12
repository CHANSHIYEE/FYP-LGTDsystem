package shiyee_FYP.fullstack_backend.repository;

import shiyee_FYP.fullstack_backend.model.BetweennessCentrality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetweennessCentralityRepository extends JpaRepository<BetweennessCentrality, Integer> {
    List<BetweennessCentrality> findTop10ByOrderByRankAsc();
}
