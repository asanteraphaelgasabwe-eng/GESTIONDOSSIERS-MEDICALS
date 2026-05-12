package views;

import controllers.PatientController;
import controllers.MedecinController;
import controllers.ConsultationController;
import models.Patient;
import models.Medecin;
import models.Consultation;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Imports OpenPDF
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PanneauRapports extends JPanel {

    private final PatientController patientController;
    private final MedecinController medecinController;
    private final ConsultationController consultationController;
    private JTextArea txtRapport;
    private String dernierRapportType = "";

    public PanneauRapports(PatientController pc, MedecinController mc, ConsultationController cc) {
        this.patientController = pc;
        this.medecinController = mc;
        this.consultationController = cc;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel northPanel = new JPanel(new BorderLayout(0, 0));
        northPanel.setOpaque(false);

        // TITRE
        JLabel title = new JLabel("GESTION DES RAPPORTS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLUE);
        northPanel.add(title, BorderLayout.NORTH);

        // BOUTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Sélectionner un rapport"));

        JButton btnPatients = createButton("Rapport des patients", new Color(40, 180, 120));
        JButton btnMedecins = createButton("Rapport des médecins", new Color(60, 140, 210));
        JButton btnConsultations = createButton("Rapport des consultations", new Color(250, 160, 40));
        JButton btnGlobal = createButton("Rapport global", new Color(210, 60, 50));
        JButton btnPDF = createButton("Exporter PDF", new Color(150, 60, 180));

        btnPatients.addActionListener(e -> { afficherPatients(); dernierRapportType = "patients"; });
        btnMedecins.addActionListener(e -> { afficherMedecins(); dernierRapportType = "medecins"; });
        btnConsultations.addActionListener(e -> { afficherConsultations(); dernierRapportType = "consultations"; });
        btnGlobal.addActionListener(e -> { afficherGlobal(); dernierRapportType = "global"; });
        btnPDF.addActionListener(e -> exporterPDF());

        buttonPanel.add(btnPatients);
        buttonPanel.add(btnMedecins);
        buttonPanel.add(btnConsultations);
        buttonPanel.add(btnGlobal);
        buttonPanel.add(btnPDF);

        northPanel.add(buttonPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // ZONE D'AFFICHAGE
        txtRapport = new JTextArea();
        txtRapport.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtRapport.setEditable(false);
        txtRapport.setBackground(Color.white);
        JScrollPane scroll = new JScrollPane(txtRapport);
        scroll.setBorder(BorderFactory.createTitledBorder("Aperçu du rapport"));
        add(scroll, BorderLayout.CENTER);

        txtRapport.setText("BIENVENUE DANS LE MODULE DE RAPPORTS\n\n" +
                "Cliquez sur un bouton ci-dessus pour générer un rapport.\n\n" +
                "Date : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        final Color original = bgColor;
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(btn.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    // --- AFFICHAGE DES RAPPORTS ---
    private void afficherPatients() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT DES PATIENTS\n");
        sb.append("==================================================\n\n");
        List<Patient> list = patientController.listerPatients();
        if (list.isEmpty()) {
            sb.append("Aucun patient enregistré.\n");
        } else {
            for (Patient p : list) {
                sb.append("ID: ").append(p.getId()).append("\n");
                sb.append("Nom: ").append(p.getNom()).append(" ").append(p.getPrenom()).append("\n");
                sb.append("Tél: ").append(p.getTelephone()).append("\n");
                sb.append("Email: ").append(p.getEmail()).append("\n");
                sb.append("----------------------------------------\n");
            }
            sb.append("\nTOTAL PATIENTS : ").append(list.size()).append("\n");
        }
        txtRapport.setText(sb.toString());
    }

    private void afficherMedecins() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT DES MÉDECINS\n");
        sb.append("==================================================\n\n");
        List<Medecin> list = medecinController.listerMedecins();
        if (list.isEmpty()) {
            sb.append("Aucun médecin enregistré.\n");
        } else {
            for (Medecin m : list) {
                sb.append("ID: ").append(m.getId()).append("\n");
                sb.append("Nom: ").append(m.getNom()).append(" ").append(m.getPrenom()).append("\n");
                sb.append("Spécialité: ").append(m.getSpecialite()).append("\n");
                sb.append("----------------------------------------\n");
            }
            sb.append("\nTOTAL MÉDECINS : ").append(list.size()).append("\n");
        }
        txtRapport.setText(sb.toString());
    }

    private void afficherConsultations() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT DES CONSULTATIONS\n");
        sb.append("==================================================\n\n");
        List<Consultation> list = consultationController.listerConsultations();
        if (list.isEmpty()) {
            sb.append("Aucune consultation enregistrée.\n");
        } else {
            for (Consultation c : list) {
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Date: ").append(c.getDateConsultation()).append("\n");
                sb.append("Motif: ").append(c.getMotif()).append("\n");
                sb.append("----------------------------------------\n");
            }
            sb.append("\nTOTAL CONSULTATIONS : ").append(list.size()).append("\n");
        }
        txtRapport.setText(sb.toString());
    }

    private void afficherGlobal() {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPPORT GLOBAL\n");
        sb.append("==================================================\n\n");
        int nbPatients = patientController.listerPatients().size();
        int nbMedecins = medecinController.listerMedecins().size();
        int nbConsultations = consultationController.listerConsultations().size();

        sb.append("STATISTIQUES\n");
        sb.append("- Patients : ").append(nbPatients).append("\n");
        sb.append("- Médecins : ").append(nbMedecins).append("\n");
        sb.append("- Consultations : ").append(nbConsultations).append("\n");
        if (nbPatients > 0) {
            sb.append("- Taux consultations/patient : ").append(String.format("%.2f", (double) nbConsultations / nbPatients)).append("\n");
        }
        sb.append("\nHOPITAL GENERAL DE REFERENCE\n");
        sb.append("Systeme de Gestion des Dossiers Medicaux\n");
        txtRapport.setText(sb.toString());
    }

    // --- EXPORT PDF (avec LocalDateTime pour l'heure) ---
    private void exporterPDF() {
        if (dernierRapportType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Générez d'abord un rapport.", "Aucun rapport", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le PDF");
            fileChooser.setSelectedFile(new java.io.File("Rapport_" + dernierRapportType + "_" + LocalDate.now() + ".pdf"));
            int choix = fileChooser.showSaveDialog(this);
            if (choix != JFileChooser.APPROVE_OPTION) return;

            String chemin = fileChooser.getSelectedFile().getAbsolutePath();
            if (!chemin.endsWith(".pdf")) chemin += ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(chemin));
            document.open();

            com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);

            Paragraph hopital = new Paragraph("HOPITAL GENERAL DE REFERENCE", titleFont);
            hopital.setAlignment(Element.ALIGN_CENTER);
            document.add(hopital);

            document.add(new Paragraph("Systeme de Gestion des Dossiers Medicaux", normalFont));
            document.add(new Paragraph(" "));

            Paragraph rapport = new Paragraph(getTitreRapport(), titleFont);
            rapport.setAlignment(Element.ALIGN_CENTER);
            document.add(rapport);

            // CORRECTION : utiliser LocalDateTime pour avoir l'heure
            Paragraph date = new Paragraph("Genere le : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);
            document.add(new Paragraph(" "));

            switch (dernierRapportType) {
                case "patients":
                    ajouterTableauPatientsPDF(document);
                    break;
                case "medecins":
                    ajouterTableauMedecinsPDF(document);
                    break;
                case "consultations":
                    ajouterTableauConsultationsPDF(document);
                    break;
                case "global":
                    ajouterRapportGlobalPDF(document);
                    break;
            }

            document.close();
            JOptionPane.showMessageDialog(this, "PDF créé avec succès !\n" + chemin, "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String getTitreRapport() {
        switch (dernierRapportType) {
            case "patients": return "RAPPORT DES PATIENTS";
            case "medecins": return "RAPPORT DES MEDECINS";
            case "consultations": return "RAPPORT DES CONSULTATIONS";
            case "global": return "RAPPORT GLOBAL";
            default: return "RAPPORT";
        }
    }

    private void ajouterTableauPatientsPDF(Document document) throws Exception {
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        String[] headers = {"ID", "Nom complet", "Telephone", "Email"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            table.addCell(cell);
        }

        for (Patient p : patientController.listerPatients()) {
            table.addCell(new Phrase(String.valueOf(p.getId()), normalFont));
            table.addCell(new Phrase(p.getNom() + " " + p.getPrenom(), normalFont));
            table.addCell(new Phrase(p.getTelephone() != null ? p.getTelephone() : "N/A", normalFont));
            table.addCell(new Phrase(p.getEmail() != null ? p.getEmail() : "N/A", normalFont));
        }
        document.add(table);
        document.add(new Paragraph("\nTotal patients : " + patientController.listerPatients().size(), normalFont));
    }

    private void ajouterTableauMedecinsPDF(Document document) throws Exception {
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        String[] headers = {"ID", "Nom complet", "Specialite", "Telephone"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            table.addCell(cell);
        }

        for (Medecin m : medecinController.listerMedecins()) {
            table.addCell(new Phrase(String.valueOf(m.getId()), normalFont));
            table.addCell(new Phrase(m.getNom() + " " + m.getPrenom(), normalFont));
            table.addCell(new Phrase(m.getSpecialite() != null ? m.getSpecialite() : "N/A", normalFont));
            table.addCell(new Phrase(m.getTelephone() != null ? m.getTelephone() : "N/A", normalFont));
        }
        document.add(table);
        document.add(new Paragraph("\nTotal medecins : " + medecinController.listerMedecins().size(), normalFont));
    }

    private void ajouterTableauConsultationsPDF(Document document) throws Exception {
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        String[] headers = {"ID", "Date", "Motif"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            table.addCell(cell);
        }

        for (Consultation c : consultationController.listerConsultations()) {
            table.addCell(new Phrase(String.valueOf(c.getId()), normalFont));
            table.addCell(new Phrase(c.getDateConsultation().toString(), normalFont));
            table.addCell(new Phrase(c.getMotif() != null ? c.getMotif() : "N/A", normalFont));
        }
        document.add(table);
        document.add(new Paragraph("\nTotal consultations : " + consultationController.listerConsultations().size(), normalFont));
    }

    private void ajouterRapportGlobalPDF(Document document) throws Exception {
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font boldFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);

        int nbPatients = patientController.listerPatients().size();
        int nbMedecins = medecinController.listerMedecins().size();
        int nbConsultations = consultationController.listerConsultations().size();

        document.add(new Paragraph("STATISTIQUES GLOBALES", boldFont));
        document.add(new Paragraph("- Patients : " + nbPatients, normalFont));
        document.add(new Paragraph("- Medecins : " + nbMedecins, normalFont));
        document.add(new Paragraph("- Consultations : " + nbConsultations, normalFont));
        if (nbPatients > 0) {
            double taux = (double) nbConsultations / nbPatients;
            document.add(new Paragraph("- Taux consultations/patient : " + String.format("%.2f", taux), normalFont));
        }
    }
}