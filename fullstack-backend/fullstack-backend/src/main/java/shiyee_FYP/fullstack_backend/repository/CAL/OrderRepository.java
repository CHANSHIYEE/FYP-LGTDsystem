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
// 按公司ID和日期范围查询订单

    List<Order> findByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 计算延迟订单数（带日期范围）
    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId " +
            "AND o.status = 'DELAYED' " +
            "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.orderDate <= :endDate)")
    long countDelayedOrders(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 计算总订单数（带日期范围）
    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId " +
            "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.orderDate <= :endDate)")
    long countTotalOrders(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId AND o.status = 'DELAYED'")
    long countDelayedOrdersByCompany(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.product.company.id = :companyId")
    long countTotalOrdersByCompany(@Param("companyId") Long companyId);

    @Query("SELECT o FROM Order o WHERE o.product.company.id = :companyId")
    List<Order> findByProductCompanyId(@Param("companyId") Long companyId);
}
