package shiyee_FYP.fullstack_backend.repository.CAL;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.QualityCheck;

import java.time.LocalDate;


@Repository
public interface QualityCheckRepository extends JpaRepository<QualityCheck, Long> {

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId AND q.passed = true")
    long countPassedChecks(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId")
    long countTotalChecks(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId " +
            "AND q.passed = true " +
            "AND (:startDate IS NULL OR q.checkDate >= :startDate) " +
            "AND (:endDate IS NULL OR q.checkDate <= :endDate)")
    long countPassedChecks(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId " +
            "AND (:startDate IS NULL OR q.checkDate >= :startDate) " +
            "AND (:endDate IS NULL OR q.checkDate <= :endDate)")
    long countTotalChecks(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId AND q.passed = true")
    long countPassedChecksByCompany(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(q) FROM QualityCheck q WHERE q.product.company.id = :companyId")
    long countTotalChecksByCompany(@Param("companyId") Long companyId);

}