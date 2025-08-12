package shiyee_FYP.fullstack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.Port;
import shiyee_FYP.fullstack_backend.model.ShippingLane;

import java.util.List;
import java.util.Optional;

public interface PortRepository extends JpaRepository<Port, Long> {
    @Query(value = "SELECT p.id, p.name, p.country, p.latitude, p.longitude, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance " +
            "FROM ports p " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.latitude)))) < :maxDistance " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Object[]> findNearbyPortsWithDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("maxDistance") double maxDistance);
}



