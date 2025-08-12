package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shiyee_FYP.fullstack_backend.model.CompanyReputationScore;

import java.util.List;

public interface CompanyReputationScoreRepository extends JpaRepository<CompanyReputationScore, Long> {
    List<CompanyReputationScore> findByCompanyIdOrderByCalculationTimeDesc(Long companyId);
    List<CompanyReputationScore> findAllByCompanyIdOrderByCalculationTime(Long companyId);
}

