package com.example.demo.Controller;

import com.example.demo.Entity.Tache;
import com.example.demo.Service.TacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserTacheController {
    private final TacheService tacheService;

    @GetMapping("/mes-taches")
    public ResponseEntity<List<Tache>> mesTaches() {
        return ResponseEntity.ok(tacheService.mesTaches());
    }

    @GetMapping("/mes-taches/chantier/{chantierId}")
    public ResponseEntity<List<Tache>> mesTachesParChantier(@PathVariable Long chantierId) {
        return ResponseEntity.ok(tacheService.mesTachesParChantier(chantierId));
    }

    @GetMapping("/total")
    @PreAuthorize("@securityEvaluator.hasPermission('TACHE_VOIR')")
    public ResponseEntity<Long> totalTaches() {
        return ResponseEntity.ok(tacheService.totalTaches());
    }

    @GetMapping("/mes-stats")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStats() {
        return ResponseEntity.ok(tacheService.mesStats());
    }

    @GetMapping("/mes-stats/par-dates")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParDates(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(tacheService.mesStatsParDates(java.time.LocalDate.parse(start), java.time.LocalDate.parse(end)));
    }

    @GetMapping("/mes-stats/chantier/{chantierId}")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParChantier(@PathVariable Long chantierId) {
        return ResponseEntity.ok(tacheService.mesStatsParChantier(chantierId));
    }

    @GetMapping("/mes-stats/chantier/{chantierId}/par-dates")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParChantierAvecDates(
            @PathVariable Long chantierId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(tacheService.mesStatsParChantierAvecDates(chantierId, java.time.LocalDate.parse(start), java.time.LocalDate.parse(end)));
    }
}
