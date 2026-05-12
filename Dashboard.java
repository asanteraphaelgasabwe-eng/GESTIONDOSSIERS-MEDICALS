package views;

import controllers.PatientController;
import controllers.MedecinController;
import controllers.ConsultationController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Dashboard extends JPanel {

    private final PatientController patientController;
    private final MedecinController medecinController;
    private final ConsultationController consultationController;

    public Dashboard(PatientController pc, MedecinController mc, ConsultationController cc) {
        this.patientController = pc;
        this.medecinController = mc;
        this.consultationController = cc;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titre
        JLabel title = new JLabel("📊 TABLEAU DE BORD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);

        // Panel des graphiques
        JPanel chartPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartPanel.setOpaque(false);

        // Graphique 1 : Répartition des consultations par mois
        chartPanel.add(creerGraphiqueConsultationsParMois());

        // Graphique 2 : Répartition des patients (statut mutuelle)
        chartPanel.add(creerGraphiquePatientsMutuelle());

        add(chartPanel, BorderLayout.CENTER);

        // Panel des statistiques en bas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int nbPatients = patientController.listerPatients().size();
        int nbMedecins = medecinController.listerMedecins().size();
        int nbConsultations = consultationController.listerConsultations().size();

        statsPanel.add(createStatCard("👥", "PATIENTS", String.valueOf(nbPatients), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("👨‍⚕️", "MÉDECINS", String.valueOf(nbMedecins), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("📋", "CONSULTATIONS", String.valueOf(nbConsultations), new Color(241, 196, 15)));

        add(statsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.BLUE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 30));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    private ChartPanel creerGraphiqueConsultationsParMois() {
        // Compter les consultations par mois
        Map<String, Integer> consultationsParMois = new HashMap<>();
        
        // Initialiser les 12 derniers mois
        LocalDate aujourdhui = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            LocalDate date = aujourdhui.minusMonths(i);
            String moisAnnee = date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
            consultationsParMois.put(moisAnnee, 0);
        }
        
        // Compter les consultations réelles
        consultationController.listerConsultations().forEach(c -> {
            String moisAnnee = c.getDateConsultation().format(DateTimeFormatter.ofPattern("MMM yyyy"));
            consultationsParMois.put(moisAnnee, consultationsParMois.getOrDefault(moisAnnee, 0) + 1);
        });
        
        // Créer le dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        consultationsParMois.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByKey().reversed())
            .forEach(entry -> dataset.addValue(entry.getValue(), "Consultations", entry.getKey()));
        
        // Créer le graphique
        JFreeChart chart = ChartFactory.createLineChart(
            "Consultations par mois",  // Titre
            "Mois",                    // Axe X
            "Nombre",                  // Axe Y
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Évolution des consultations"));
        return chartPanel;
    }

    private ChartPanel creerGraphiquePatientsMutuelle() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Compter les patients avec et sans mutuelle
        long avecMutuelle = patientController.listerPatients().stream()
            .filter(p -> p.getMutuelle() != null && !p.getMutuelle().isEmpty())
            .count();
        long sansMutuelle = patientController.listerPatients().size() - avecMutuelle;
        
        dataset.setValue("Avec mutuelle", avecMutuelle);
        dataset.setValue("Sans mutuelle", sansMutuelle);
        
        // Créer le graphique circulaire
        JFreeChart chart = ChartFactory.createPieChart(
            "Patients par mutuelle",   // Titre
            dataset,                   // Données
            true,                      // Légende
            true,                      // Tooltips
            false                      // URLs
        );
        
        // Récupérer le plot et personnaliser
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Avec mutuelle", new Color(46, 204, 113));
        plot.setSectionPaint("Sans mutuelle", new Color(231, 76, 60));
        
        // Ajouter des labels
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
        plot.setSimpleLabels(true);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Couverture mutuelle"));
        return chartPanel;
    }
}