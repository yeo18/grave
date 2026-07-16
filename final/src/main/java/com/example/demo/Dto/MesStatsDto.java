package com.example.demo.Dto;

import lombok.Data;
import java.util.List;

@Data
public class MesStatsDto {
    private long totalTaches;
    private long tachesTerminees;
    private long tachesEnCours;
    private long tachesAFaire;
    private long tachesBloquees;
    private long tachesIndividuelles;
    private long tachesEquipe;
    private double tauxAchevement;
    private List<StatsParChantier> parChantier;

    @Data
    public static class StatsParChantier {
        private Long chantierId;
        private String chantierNom;
        private long total;
        private long individuelles;
        private long equipe;
        private long terminees;
        private long enCours;
        private long aFaire;
        private long bloquees;
        private double tauxAchevement;
    }
}
