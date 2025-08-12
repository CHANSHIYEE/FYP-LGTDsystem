package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.GlobalCompany;

@Repository
public interface GlobalCompanyRepository extends JpaRepository<GlobalCompany, Long> {
}

