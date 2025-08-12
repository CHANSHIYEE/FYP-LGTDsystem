package shiyee_FYP.fullstack_backend.repository;


import shiyee_FYP.fullstack_backend.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    // 根据用户ID查询坐标
    List<Coordinate> findByUserId(Long userId);
}