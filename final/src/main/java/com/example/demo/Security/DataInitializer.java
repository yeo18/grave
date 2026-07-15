package com.example.demo.Security;

import com.example.demo.Entity.Permission;
import com.example.demo.Entity.Profil;
import com.example.demo.Entity.Utilisateur;
import com.example.demo.Repository.PermissionRepository;
import com.example.demo.Repository.ProfilRepository;
import com.example.demo.Repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final ProfilRepository profilRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository,
                           ProfilRepository profilRepository,
                           UtilisateurRepository utilisateurRepository,
                           PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.profilRepository = profilRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // ==========================================
        // 1. CRÉATION / RESTAURATION DE TOUTES LES PERMISSIONS
        // ==========================================
        Map<String, String> permissionsAvecDescription = Map.ofEntries(
                Map.entry("CHANTIER_CREER", "Créer un nouveau chantier"),
                Map.entry("CHANTIER_MODIFIER", "Modifier les informations d'un chantier"),
                Map.entry("CHANTIER_SUPPRIMER", "Supprimer définitivement un chantier"),
                Map.entry("CHANTIER_VOIR", "Consulter la liste et le détail des chantiers"),
                Map.entry("CHANTIER_VOIR_STATS", "Accéder aux statistiques et indicateurs d'un chantier"),
                Map.entry("TACHE_CREER", "Créer une nouvelle tâche"),
                Map.entry("TACHE_MODIFIER", "Modifier une tâche (titre, dates, description, statut)"),
                Map.entry("TACHE_SUPPRIMER", "Supprimer définitivement une tâche"),
                Map.entry("TACHE_VOIR", "Consulter la liste et le détail des tâches"),
                Map.entry("TACHE_VALIDER", "Marquer une tâche comme terminée"),
                Map.entry("TACHE_ASSIGNER_EQUIPE", "Assigner une tâche à une équipe"),
                Map.entry("TACHE_ASSIGNER_UTILISATEUR", "Assigner une tâche à un utilisateur"),
                Map.entry("EQUIPE_CREER", "Créer une nouvelle équipe"),
                Map.entry("EQUIPE_MODIFIER", "Modifier les informations d'une équipe"),
                Map.entry("EQUIPE_SUPPRIMER", "Supprimer définitivement une équipe"),
                Map.entry("EQUIPE_VOIR", "Consulter la liste et le détail des équipes"),
                Map.entry("EQUIPE_GERER_MEMBRES", "Ajouter ou retirer des membres dans une équipe"),
                Map.entry("UTILISATEUR_VOIR", "Consulter la liste des utilisateurs"),
                Map.entry("UTILISATEUR_CREER", "Créer un nouvel utilisateur"),
                Map.entry("UTILISATEUR_MODIFIER", "Modifier les informations d'un utilisateur"),
                Map.entry("UTILISATEUR_SUPPRIMER", "Supprimer définitivement un utilisateur"),
                Map.entry("GERER_HABILITATIONS", "Gérer les profils, les permissions et les accès")
        );

        for (Map.Entry<String, String> entry : permissionsAvecDescription.entrySet()) {
            String nom = entry.getKey();
            String description = entry.getValue();
            Permission perm = permissionRepository.findByNom(nom).orElse(null);
            if (perm == null) {
                perm = newPermission(nom);
                perm.setDescription(description);
                permissionRepository.save(perm);
                System.out.println("✅ Permission créée : " + nom);
            } else if (perm.getDescription() == null || !description.equals(perm.getDescription())) {
                perm.setDescription(description);
                permissionRepository.save(perm);
                System.out.println("📝 Description mise à jour : " + nom);
            }
        }

        // ==========================================
        // 2. CRÉATION DES PROFILS
        // ==========================================
        Profil adminProfil = profilRepository.findByNom("ADMIN").orElseGet(() -> {
            Profil p = new Profil();
            p.setNom("ADMIN");
            p.setDateCreation(LocalDateTime.now());
            p.setDateModification(LocalDateTime.now());
            return profilRepository.save(p);
        });

        Profil userProfil = profilRepository.findByNom("USER").orElseGet(() -> {
            Profil p = new Profil();
            p.setNom("USER");
            p.setDateCreation(LocalDateTime.now());
            p.setDateModification(LocalDateTime.now());
            return profilRepository.save(p);
        });

        // ==========================================
        // 3. ATTRIBUTION DE TOUTES LES PERMISSIONS À L'ADMIN
        // ==========================================
        permissionRepository.findAll().forEach(permission -> {
            if (!adminProfil.getPermissions().contains(permission)) {
                adminProfil.getPermissions().add(permission);
            }
        });
        profilRepository.save(adminProfil);

        // ==========================================
        // 4. ATTRIBUTION DES PERMISSIONS AU PROFIL USER
        // ==========================================
        List.of("CHANTIER_VOIR", "TACHE_VOIR", "TACHE_VALIDER", "EQUIPE_VOIR").forEach(nom -> {
            permissionRepository.findByNom(nom).ifPresent(perm -> {
                if (!userProfil.getPermissions().contains(perm)) {
                    userProfil.getPermissions().add(perm);
                }
            });
        });
        profilRepository.save(userProfil);

        // ==========================================
        // 5. CRÉATION DE L'ADMIN PAR DÉFAUT
        // ==========================================
        if (utilisateurRepository.findByEmail("admin@example.com").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setTelephone("0102030405");
            admin.setProfil(adminProfil);
            admin.setDatecreation(LocalDateTime.now());
            admin.setDateModification(LocalDateTime.now());
            utilisateurRepository.save(admin);
            System.out.println("✅ Admin créé : admin@example.com / admin123");
        }
    }

    private Permission newPermission(String nom) {
        Permission p = new Permission();
        p.setNom(nom);
        return p;
    }
}