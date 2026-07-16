package com.example.demo.Controller;

import com.example.demo.Entity.Tache;
import com.example.demo.Service.TacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Tâches (Utilisateur)", description = "Consultation personnelle des tâches par l'utilisateur")
public class UserTacheController {
    private final TacheService tacheService;

    @GetMapping("/mes-taches")
    @Operation(summary = "Mes tâches", description = "Retourne uniquement les tâches assignées à l'utilisateur ou à son équipe")
    public ResponseEntity<List<Tache>> mesTaches() {
        return ResponseEntity.ok(tacheService.mesTaches());
    }

    @GetMapping("/mes-taches/chantier/{chantierId}")
    @Operation(summary = "Mes tâches par chantier", description = "Filtre mes tâches par chantier")
    public ResponseEntity<List<Tache>> mesTachesParChantier(@PathVariable Long chantierId) {
        return ResponseEntity.ok(tacheService.mesTachesParChantier(chantierId));
    }

    @GetMapping("/total")
    @PreAuthorize("@securityEvaluator.hasPermission('TACHE_VOIR')")
    @Operation(summary = "Total des tâches", description = "Retourne le nombre total de tâches dans le système")
    public ResponseEntity<Long> totalTaches() {
        return ResponseEntity.ok(tacheService.totalTaches());
    }

    @GetMapping("/mes-stats")
    @Operation(summary = "Mes statistiques", description = "Statistiques personnelles de l'utilisateur connecté")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStats() {
        return ResponseEntity.ok(tacheService.mesStats());
    }

    @GetMapping("/mes-stats/par-dates")
    @Operation(summary = "Mes stats par dates", description = "Statistiques filtrées par période")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParDates(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(tacheService.mesStatsParDates(java.time.LocalDate.parse(start), java.time.LocalDate.parse(end)));
    }

    @GetMapping("/mes-stats/chantier/{chantierId}")
    @Operation(summary = "Mes stats par chantier", description = "Statistiques filtrées par chantier")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParChantier(@PathVariable Long chantierId) {
        return ResponseEntity.ok(tacheService.mesStatsParChantier(chantierId));
    }

    @GetMapping("/mes-stats/chantier/{chantierId}/par-dates")
    @Operation(summary = "Mes stats chantier + dates", description = "Statistiques filtrées par chantier et période")
    public ResponseEntity<com.example.demo.Dto.MesStatsDto> mesStatsParChantierAvecDates(
            @PathVariable Long chantierId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(tacheService.mesStatsParChantierAvecDates(chantierId, java.time.LocalDate.parse(start), java.time.LocalDate.parse(end)));
    }
}
