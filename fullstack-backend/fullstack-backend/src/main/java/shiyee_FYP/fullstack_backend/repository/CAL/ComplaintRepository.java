package shiyee_FYP.fullstack_backend.repository.CAL;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.Complaint;

import java.time.LocalDate;
import java.util.List;

// ComplaintRepository.java
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.order.product.company.id = :companyId")
    long countComplaints(@Param("companyId") Long companyId);

//    @Query("SELECT c FROM Complaint c WHERE c.order.product.company.id = :companyId")
//    List<Complaint> findByCompanyId(@Param("companyId") Long companyId);
@Query("SELECT COUNT(c) FROM Complaint c WHERE c.order.product.company.id = :companyId " +
        "AND (:startDate IS NULL OR c.complaintDate >= :startDate) " +  // 改为使用complaintDate
        "AND (:endDate IS NULL OR c.complaintDate <= :endDate)")
long countComplaints(
        @Param("companyId") Long companyId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


    // 替换原来的findByCompanyId为以下方法
    @Query("SELECT c FROM Complaint c WHERE c.order.product.company.id = :companyId")
    List<Complaint> findByCompanyId(@Param("companyId") Long companyId);

    // 带日期范围的方法
    @Query("SELECT c FROM Complaint c WHERE c.order.product.company.id = :companyId " +
            "AND (:startDate IS NULL OR c.complaintDate >= :startDate) " +
            "AND (:endDate IS NULL OR c.complaintDate <= :endDate)")
    List<Complaint> findByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 计数方法
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.order.product.company.id = :companyId " +
            "AND (:startDate IS NULL OR c.complaintDate >= :startDate) " +
            "AND (:endDate IS NULL OR c.complaintDate <= :endDate)")
    long countByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Complaint c WHERE c.order.product.company.id = :companyId")
    List<Complaint> findByOrderProductCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.order.product.company.id = :companyId")
    long countByCompany(@Param("companyId") Long companyId);

}