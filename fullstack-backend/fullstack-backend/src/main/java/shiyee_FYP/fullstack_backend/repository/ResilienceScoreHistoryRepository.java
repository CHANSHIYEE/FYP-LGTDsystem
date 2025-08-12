package shiyee_FYP.fullstack_backend.repository;

import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.ResilienceScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ResilienceScoreHistoryRepository extends JpaRepository<ResilienceScoreHistory, Long> {

    // 按位置ID查找并按计算日期降序排序
    List<ResilienceScoreHistory> findByLocationIdOrderByCalculationDateDesc(Integer locationId);

    // 获取指定位置的最新记录
    Optional<ResilienceScoreHistory> findTopByLocationIdOrderByCalculationDateDesc(Integer locationId);

}