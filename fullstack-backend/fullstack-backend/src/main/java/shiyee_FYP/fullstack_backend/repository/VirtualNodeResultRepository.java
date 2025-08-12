package shiyee_FYP.fullstack_backend.repository;

import shiyee_FYP.fullstack_backend.model.VirtualNodeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VirtualNodeResultRepository
        extends JpaRepository<VirtualNodeResult, Integer> {

    @Query("SELECT v FROM VirtualNodeResult v ORDER BY v.betweenness DESC")
    List<VirtualNodeResult> findAllByOrderByBetweennessDesc();


    List<VirtualNodeResult> findByIsVirtual(Boolean isVirtual);
}