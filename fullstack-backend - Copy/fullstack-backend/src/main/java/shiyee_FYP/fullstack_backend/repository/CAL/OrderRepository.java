package shiyee_FYP.fullstack_backend.repository.CAL;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.Order;
import shiyee_FYP.fullstack_backend.model.QualityCheck;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.product.company.id = :companyId " +
            "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.orderDate <= :endDate)")
    List<Order> findByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId AND o.status = 'DELAYED'")
    long countDelayedOrders(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId")
    long countTotalOrders(@Param("companyId") Long companyId);
}
