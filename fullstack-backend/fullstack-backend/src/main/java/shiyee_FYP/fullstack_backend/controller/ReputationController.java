package shiyee_FYP.fullstack_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shiyee_FYP.fullstack_backend.model.DTO.ComplaintDTO;
import shiyee_FYP.fullstack_backend.model.DTO.OrderDTO;
import shiyee_FYP.fullstack_backend.Service.ReputationService;
import shiyee_FYP.fullstack_backend.model.DTO.ReputationMetricsDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reputation")
@RequiredArgsConstructor
public class ReputationController {

    private final ReputationService reputationService;

    @GetMapping("/metrics/{companyId}")
    public ResponseEntity<ReputationMetricsDTO> getReputationMetrics(
            @PathVariable Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ReputationMetricsDTO metrics = reputationService.calculateReputationMetrics(companyId);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/orders/{companyId}")
    public ResponseEntity<List<OrderDTO>> getCompanyOrders(
            @PathVariable Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<OrderDTO> orders = reputationService.getAllOrders(companyId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/complaints/{companyId}")
    public ResponseEntity<List<ComplaintDTO>> getCompanyComplaints(@PathVariable Long companyId) {
        List<ComplaintDTO> complaints = reputationService.getCompanyComplaints(companyId);
        return ResponseEntity.ok(complaints);
    }
}