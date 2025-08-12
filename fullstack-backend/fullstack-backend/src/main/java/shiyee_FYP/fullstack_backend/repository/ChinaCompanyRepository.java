package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.ChinaCompany;

public interface ChinaCompanyRepository extends JpaRepository<ChinaCompany, Integer> {
    @Query("SELECT MAX(c.employeeCount) FROM ChinaCompany c")
    Integer findMaxEmployeeCount();

    @Query("SELECT c.employeeCount FROM ChinaCompany c WHERE c.id = :id")
    Integer findEmployeeCountById(@Param("id") Integer id);
}
