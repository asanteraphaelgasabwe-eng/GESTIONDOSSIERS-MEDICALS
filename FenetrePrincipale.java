/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author SHAURI
 */

import controllers.PatientController;
import controllers.MedecinController;
import controllers.ConsultationController;
import controllers.MedicamentController;
import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame {

    public FenetrePrincipale() {
        setTitle("🏥 Hôpital Général - Système de Gestion Médicale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialisation des contrôleurs
        PatientController patientController = new PatientController();
        MedecinController medecinController = new MedecinController();
        ConsultationController consultationController = new ConsultationController();
        MedicamentController medicamentController = new MedicamentController();

        // Création du panel avec onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));


        // Ajout des onglets
       tabbedPane.addTab("🏠 ACCUEIL", new Accueil(this));
       tabbedPane.addTab("👥 PATIENTS", new PanneauPatients(patientController));
       tabbedPane.addTab("👨‍⚕️ MÉDECINS", new PanneauMedecins(medecinController));
       tabbedPane.addTab("📋 CONSULTATIONS", new PanneauConsultations(consultationController));
       tabbedPane.addTab("📊 RAPPORTS", new PanneauRapports(patientController, medecinController, consultationController));
       tabbedPane.addTab("📈 DASHBOARD", new Dashboard(patientController, medecinController, consultationController));
       tabbedPane.addTab("💊 MÉDICAMENTS", new PaneauMedicaments(medicamentController));
        // Personnalisation des couleurs des onglets
        Color orangeColor = new Color(255, 200, 100);
        Color roseColor = new Color(255, 182, 193);

        tabbedPane.setBackgroundAt(0, orangeColor);
        tabbedPane.setForegroundAt(0, new Color(100, 60, 0));
        tabbedPane.setBackgroundAt(1, roseColor);
        tabbedPane.setForegroundAt(1, new Color(180, 0, 80));
        tabbedPane.setBackgroundAt(2, roseColor);
        tabbedPane.setForegroundAt(2, new Color(180, 0, 80));
        tabbedPane.setBackgroundAt(3, roseColor);
        tabbedPane.setForegroundAt(3, new Color(180, 0, 80));
        tabbedPane.setBackgroundAt(4, orangeColor);
        tabbedPane.setForegroundAt(4, new Color(100, 60, 0));
        tabbedPane.setBackgroundAt(5, new Color(100, 150, 200));
        tabbedPane.setForegroundAt(5, Color.magenta);

        add(tabbedPane, BorderLayout.CENTER);

        // Barre de statut
        JLabel statusBar = new JLabel("✅ Connecté à MySQL | Base : dossier_medical | Prêt");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipale());
    }
}