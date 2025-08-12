package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shiyee_FYP.fullstack_backend.model.ShippingLane;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShippingLaneRepository extends JpaRepository<ShippingLane, Long> {
    Optional<ShippingLane> findByStartPortIdAndEndPortId(Long startPortId, Long endPortId);

    // 查找直达航线
    @Query("SELECT sl FROM ShippingLane sl WHERE " +
            "(sl.startPort.id = :port1 AND sl.endPort.id = :port2) OR " +
            "(sl.startPort.id = :port2 AND sl.endPort.id = :port1)")
    Optional<ShippingLane> findDirectLane(@Param("port1") Long port1, @Param("port2") Long port2);

    // 查找所有从指定港口出发的航线
    List<ShippingLane> findByStartPortId(Long portId);

    // 查找所有到达指定港口的航线
    List<ShippingLane> findByEndPortId(Long portId);

//    Optional<ShippingLane> findByStartPortIdAndEndPortId(Long startPortId, Long endPortId);
//    List<ShippingLane> findByStartPortId(Long startPortId);
//    @Query(value = "SELECT id, fid, objectid, type, ST_AsText(geom) as geom, distance, estimated_time, start_port_id, end_port_id, object_id FROM shipping_lanes", nativeQuery = true)
//    List<ShippingLane> findAllWithGeomAsText();
//
//    @Query(value = "SELECT id, fid, objectid, type, ST_AsGeoJSON(geom) as geom, distance, estimated_time, start_port_id, end_port_id, object_id FROM shipping_lanes", nativeQuery = true)
//    List<ShippingLane> findAllWithGeomAsGeoJSON();
//    @Query(value = "SELECT id, ST_AsText(ST_ClosestPoint(geom, ST_GeomFromText(:point))) as nearestPoint " +
//            "FROM shipping_lanes " +
//            "ORDER BY ST_Distance(geom, ST_GeomFromText(:point)) ASC " +
//            "LIMIT 1", nativeQuery = true)
//    Map<String, Object> findNearestLane(@Param("point") String pointWKT);


}