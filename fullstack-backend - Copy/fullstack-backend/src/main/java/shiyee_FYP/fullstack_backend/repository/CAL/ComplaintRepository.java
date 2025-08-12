package shiyee_FYP.fullstack_backend.repository.CAL;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.Complaint;

import java.util.List;

// ComplaintRepository.java
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.order.product.company.id = :companyId")
    long countComplaints(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Complaint c WHERE c.order.product.company.id = :companyId")
    List<Complaint> findByCompanyId(@Param("companyId") Long companyId);
}