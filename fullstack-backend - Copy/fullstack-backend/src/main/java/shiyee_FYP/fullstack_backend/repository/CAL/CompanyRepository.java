package shiyee_FYP.fullstack_backend.repository.CAL;


import shiyee_FYP.fullstack_backend.model.ChinaCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<ChinaCompany, Long> {
}