package shiyee_FYP.fullstack_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shiyee_FYP.fullstack_backend.Service.ReputationService;
import shiyee_FYP.fullstack_backend.Service.ResilienceService;
import shiyee_FYP.fullstack_backend.model.ApiResponse;
import shiyee_FYP.fullstack_backend.model.CompanyReputationScore;
import shiyee_FYP.fullstack_backend.model.DTO.ResilienceDetailDTO;
import shiyee_FYP.fullstack_backend.model.ResilienceScoreHistory;

import java.util.List;
@RestController
@RequestMapping("/api/resilience")
@RequiredArgsConstructor
public class ResilienceController {
    private final ResilienceService resilienceService;

    private final ReputationService reputationService;

    @GetMapping("/history/{companyId}")
    public ResponseEntity<ApiResponse<List<CompanyReputationScore>>> getReputationHistory(@PathVariable Long companyId) {
        List<CompanyReputationScore> history = reputationService.getReputationHistory(companyId);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @PostMapping("/calculate-all")
    public ResponseEntity<ApiResponse<String>> calculateAll() {
        resilienceService.calculateAndSaveAllResilienceScores();
        return ResponseEntity.ok(ApiResponse.success("All resilience scores calculated and saved successfully"));
    }

    @GetMapping("/score/{locationId}")
    public ResponseEntity<ApiResponse<ResilienceScoreHistory>> getLatestResilienceScore(@PathVariable Integer locationId) {
        ResilienceScoreHistory score = resilienceService.getLatestResilienceScore(locationId);
        return ResponseEntity.ok(ApiResponse.success(score));
    }

    @GetMapping("/detail/{locationId}")
    public ResponseEntity<ApiResponse<ResilienceDetailDTO>> getResilienceDetail(@PathVariable Integer locationId) {
        ResilienceDetailDTO detail = resilienceService.getResilienceDetail(locationId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

//    @GetMapping("/history/{locationId}")
//    public ResponseEntity<ApiResponse<List<ResilienceScoreHistory>>> getResilienceHistory(@PathVariable Integer locationId) {
//        List<ResilienceScoreHistory> history = resilienceService.getResilienceHistory(locationId);
//        return ResponseEntity.ok(ApiResponse.success(history));
//
//
//    }


}
