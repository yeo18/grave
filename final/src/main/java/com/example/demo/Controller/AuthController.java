package com.example.demo.Controller;
import com.example.demo.Dto.ReponseAuthentification;
import com.example.demo.Dto.Connexion;
import com.example.demo.Dto.Inscription;
import com.example.demo.Dto.UserMeDto;
import com.example.demo.Entity.Utilisateur;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription, connexion et gestion du profil")
public class AuthController {
    private final UtilisateurService utilisateurService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<?> register(@Valid @RequestBody Inscription inscription) {
        try {
            utilisateurService.inscrire(inscription);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur créé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Authentifie l'utilisateur et retourne un token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody Connexion connexion) {
        try {
            authService.authentifier(connexion.getEmail(), connexion.getPassword());
            String token = jwtUtil.generateToken(connexion.getEmail());
            ReponseAuthentification reponse = new ReponseAuthentification();
            reponse.setToken(token);
            return ResponseEntity.ok(reponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email ou mot de passe incorrect"));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Profil connecté", description = "Retourne les informations de l'utilisateur connecté")
    public ResponseEntity<UserMeDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserMeDto dto = utilisateurService.getCurrentUserDto(email);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Modifier mon profil", description = "Met à jour les informations personnelles de l'utilisateur connecté")
    public ResponseEntity<UserMeDto> updateCurrentUser(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        Utilisateur currentUser = utilisateurService.trouverParEmail(currentEmail);
        utilisateurService.modifierMonProfil(currentUser.getId(), nom, prenom, email, telephone, password);
        UserMeDto dto = utilisateurService.getCurrentUserDto(
                email != null ? email : currentEmail);
        return ResponseEntity.ok(dto);
    }

}
