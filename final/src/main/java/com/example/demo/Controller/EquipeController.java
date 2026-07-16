package com.example.demo.Controller;

import com.example.demo.Dto.EquipeDto;
import com.example.demo.Entity.Equipe;
import com.example.demo.Service.EquipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/equipe")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Équipes", description = "Gestion des équipes")
public class EquipeController {
    private final EquipeService equipeService;

    @GetMapping
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_VOIR')")
    @Operation(summary = "Liste des équipes", description = "Retourne toutes les équipes")
    public ResponseEntity<List<Equipe>> listerEquipes() {
        return ResponseEntity.ok(equipeService.listerToutes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_VOIR')")
    @Operation(summary = "Détail d'une équipe", description = "Retourne une équipe par son ID")
    public ResponseEntity<Equipe> EquipeUnique(@PathVariable Long id) {
        return ResponseEntity.ok(equipeService.EquipeUnique(id));
    }

    @PostMapping
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_CREER')")
    @Operation(summary = "Créer une équipe", description = "Crée une nouvelle équipe")
    public ResponseEntity<?> creerEquipe(@Valid @RequestBody EquipeDto dto) {
        Equipe equipe = equipeService.creerEquipe(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Équipe créée avec succès", "data", equipe));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_MODIFIER')")
    @Operation(summary = "Modifier une équipe", description = "Met à jour une équipe existante")
    public ResponseEntity<?> modifierEquipe(@PathVariable Long id, @Valid @RequestBody EquipeDto dto) {
        Equipe equipe = equipeService.modifierEquipe(id, dto);
        return ResponseEntity.ok(Map.of("message", "Équipe modifiée avec succès", "data", equipe));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_SUPPRIMER')")
    @Operation(summary = "Supprimer une équipe", description = "Supprime une équipe")
    public ResponseEntity<?> supprimerEquipe(@PathVariable Long id) {
        equipeService.supprimerEquipe(id);
        return ResponseEntity.ok(Map.of("message", "Équipe supprimée avec succès"));
    }

    @PostMapping("/{equipeId}/membres/{utilisateurId}")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_GERER_MEMBRES')")
    @Operation(summary = "Ajouter un membre", description = "Ajoute un utilisateur à une équipe")
    public ResponseEntity<?> ajouterMembre(@PathVariable Long equipeId, @PathVariable Long utilisateurId) {
        equipeService.ajouterMembre(equipeId, utilisateurId);
        return ResponseEntity.ok(Map.of("message", "Membre ajouté à l'équipe avec succès"));
    }

    @DeleteMapping("/{equipeId}/membres/{utilisateurId}")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_GERER_MEMBRES')")
    @Operation(summary = "Retirer un membre", description = "Retire un utilisateur d'une équipe")
    public ResponseEntity<?> retirerMembre(@PathVariable Long equipeId, @PathVariable Long utilisateurId) {
        equipeService.retirerMembre(equipeId, utilisateurId);
        return ResponseEntity.ok(Map.of("message", "Membre retiré de l'équipe avec succès"));
    }

    @GetMapping("/{id}/nb-membres")
    @PreAuthorize("@securityEvaluator.hasPermission('EQUIPE_VOIR')")
    @Operation(summary = "Nombre de membres", description = "Retourne le nombre de membres d'une équipe")
    public ResponseEntity<Integer> getNombreMembres(@PathVariable Long id) {
        int nb = equipeService.calculerNombreMembres(id);
        return ResponseEntity.ok(nb);
    }

    @GetMapping("/mon-equipe")
    @Operation(summary = "Mon équipe", description = "Retourne l'équipe de l'utilisateur connecté")
    public ResponseEntity<Equipe> monEquipe() {
        return ResponseEntity.ok(equipeService.monEquipe());
    }
}
