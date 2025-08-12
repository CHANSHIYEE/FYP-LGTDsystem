package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.Port;
import shiyee_FYP.fullstack_backend.model.ShippingLane;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingLane, Long> {

    // 通过港口ID来查询 ShippingLane
    Optional<ShippingLane> findByStartPortIdAndEndPortId(Long startPortId, Long endPortId);


    // 根据起始港口ID查找所有的航线
    List<ShippingLane> findByStartPortId(Long startPortId);

    // 根据目标港口ID查找所有的航线
    List<ShippingLane> findByEndPortId(Long endPortId);

}



