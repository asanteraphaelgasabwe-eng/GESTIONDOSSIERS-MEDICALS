/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author SHAURI
 */

import controllers.ConsultationController;
import controllers.PatientController;
import controllers.MedecinController;
import models.Consultation;
import models.Patient;
import models.Medecin;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class PanneauConsultations extends JPanel {

    private final ConsultationController controller;
    private final PatientController patientController;
    private final MedecinController medecinController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMotif, txtDiagnostic, txtTraitement;
    private JComboBox<String> comboPatients, comboMedecins;

    private final Color HEADER_BG = new Color(20, 60, 100);
    private final Color HEADER_FG = Color.black;
    private final Color BUTTON_ADD = new Color(46, 204, 113);
    private final Color BUTTON_REFRESH = new Color(241, 196, 15);

    public PanneauConsultations(ConsultationController controller) {
        this.controller = controller;
        this.patientController = new PatientController();
        this.medecinController = new MedecinController();
        initUI();
        rafraichirTableau();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 248, 250));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("📋 GESTION DES CONSULTATIONS – HISTORIQUE MÉDICAL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(HEADER_BG);
        add(title, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Date", "Motif", "Diagnostic", "Traitement", "Patient", "Médecin"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_BG);
        header.setForeground(HEADER_FG);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_BG, 1),
            "📋 LISTE DES CONSULTATIONS",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), HEADER_BG));
        add(scroll, BorderLayout.CENTER);

        // Formulaire
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.BLUE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_BG, 1),
            "✏️ NOUVELLE CONSULTATION",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), HEADER_BG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;

        txtMotif = createField();
        txtDiagnostic = createField();
        txtTraitement = createField();
        comboPatients = new JComboBox<>();
        comboMedecins = new JComboBox<>();
        comboPatients.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboMedecins.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        int row = 0;
        addFormRow(form, gbc, row++, "Motif :", txtMotif, labelFont, HEADER_BG);
        addFormRow(form, gbc, row++, "Diagnostic :", txtDiagnostic, labelFont, HEADER_BG);
        addFormRow(form, gbc, row++, "Traitement :", txtTraitement, labelFont, HEADER_BG);
        addFormRow(form, gbc, row++, "Patient :", comboPatients, labelFont, HEADER_BG);
        addFormRow(form, gbc, row++, "Médecin :", comboMedecins, labelFont, HEADER_BG);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.magenta);
        JButton btnAjouter = createStyledButton("➕ AJOUTER", BUTTON_ADD);
        JButton btnRafraichir = createStyledButton("🔄 RAFRAÎCHIR", BUTTON_REFRESH);

        btnAjouter.addActionListener(e -> ajouterConsultation());
        btnRafraichir.addActionListener(e -> {
            rafraichirTableau();
            remplirCombos();
        });

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnRafraichir);
        form.add(buttonPanel, gbc);
        add(form, BorderLayout.SOUTH);

        remplirCombos();
    }

    private JTextField createField() {
        JTextField f = new JTextField(15);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                            JComponent comp, Font font, Color color) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.2;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        panel.add(comp, gbc);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.black);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(btn.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void remplirCombos() {
        comboPatients.removeAllItems();
        comboMedecins.removeAllItems();
        for (Patient p : patientController.listerPatients()) {
            comboPatients.addItem(p.getId() + " - " + p.getNom() + " " + p.getPrenom());
        }
        for (Medecin m : medecinController.listerMedecins()) {
            comboMedecins.addItem(m.getId() + " - " + m.getNom() + " " + m.getPrenom() + " (" + m.getSpecialite() + ")");
        }
    }

    private void ajouterConsultation() {
        if (comboPatients.getItemCount() == 0 || comboMedecins.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez d'abord ajouter des patients et des médecins");
            return;
        }
        try {
            Consultation c = new Consultation();
            c.setDateConsultation(LocalDate.now());
            c.setMotif(txtMotif.getText());
            c.setDiagnostic(txtDiagnostic.getText());
            c.setTraitement(txtTraitement.getText());

            String selectedPatient = (String) comboPatients.getSelectedItem();
            int idPatient = Integer.parseInt(selectedPatient.split(" - ")[0]);
            c.setIdPatient(idPatient);

            String selectedMedecin = (String) comboMedecins.getSelectedItem();
            int idMedecin = Integer.parseInt(selectedMedecin.split(" - ")[0]);
            c.setIdMedecin(idMedecin);

            if (controller.ajouterConsultation(c)) {
                JOptionPane.showMessageDialog(this, "✅ Consultation ajoutée");
                viderChamps();
                rafraichirTableau();
                remplirCombos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Échec de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Consultation c : controller.listerConsultations()) {
            String patientNom = getPatientNom(c.getIdPatient());
            String medecinNom = getMedecinNom(c.getIdMedecin());
            tableModel.addRow(new Object[]{
                c.getId(), c.getDateConsultation(), c.getMotif(),
                c.getDiagnostic(), c.getTraitement(), patientNom, medecinNom
            });
        }
    }

    private String getPatientNom(int id) {
        for (Patient p : patientController.listerPatients()) {
            if (p.getId() == id) return p.getNom() + " " + p.getPrenom();
        }
        return "Inconnu";
    }

    private String getMedecinNom(int id) {
        for (Medecin m : medecinController.listerMedecins()) {
            if (m.getId() == id) return m.getNom() + " " + m.getPrenom();
        }
        return "Inconnu";
    }

    private void viderChamps() {
        txtMotif.setText("");
        txtDiagnostic.setText("");
        txtTraitement.setText("");
    }
}