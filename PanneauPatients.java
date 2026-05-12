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
import models.Patient;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanneauPatients extends JPanel {

    private final PatientController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrenom, txtDateNaissance, txtAdresse, txtEmail, txtMutuelle;
    private JFormattedTextField txtTelephone;
    private JLabel lblAge;

    private final Color HEADER_BG = new Color(0, 80, 120);
    private final Color HEADER_FG = Color.YELLOW;
    private final Color FORM_BG = new Color(248, 250, 252);

    public PanneauPatients(PatientController controller) {
        this.controller = controller;
        initUI();
        rafraichirTableau();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 8));
        setBackground(new Color(230, 240, 220));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ----- TITRE -----
        JLabel title = new JLabel("👥 GESTION DES PATIENTS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(HEADER_BG);
        add(title, BorderLayout.NORTH);

        // ----- TABLEAU (compact) -----
        String[] colonnes = {"ID", "Nom", "Prénom", "Âge", "Naissance", "Téléphone", "Email", "Mutuelle"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        table.setIntercellSpacing(new Dimension(4, 2));
        JTableHeader header = table.getTableHeader();
        
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));      // police plus grande
        header.setBackground(new Color(0, 80, 220));             // bleu très foncé
        header.setForeground(Color.BLUE);                       // texte blanc
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // hauteur augmentée
        header.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // optionnel : contour blanc

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                if (!isSel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 250));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(HEADER_BG, 1));
        add(scroll, BorderLayout.CENTER);

        // ----- FORMULAIRE TRÈS COMPACT, CENTRÉ, CHAMPS ÉTROITS -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(FORM_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HEADER_BG, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        form.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(HEADER_BG, 1),
            "Enregistrement patient",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 10),
            HEADER_BG
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        // Champs très étroits (6 colonnes) avec bordure verte
        txtNom = createTinyField();
        txtPrenom = createTinyField();
        txtDateNaissance = createTinyField();
        txtDateNaissance.setToolTipText("AAAA-MM-JJ");
        txtAdresse = createTinyField();
        txtTelephone = createTinyNumericField();
        txtEmail = createTinyField();
        txtMutuelle = createTinyField();
        lblAge = new JLabel("-- ans");
        lblAge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblAge.setForeground(HEADER_BG);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 9);
        int row = 0;
        addFormRow(form, gbc, row++, "Nom :", txtNom, labelFont);
        addFormRow(form, gbc, row++, "Prénom :", txtPrenom, labelFont);
        addFormRow(form, gbc, row++, "Naissance :", txtDateNaissance, labelFont);
        addFormRow(form, gbc, row++, "Âge :", lblAge, labelFont);
        addFormRow(form, gbc, row++, "Adresse :", txtAdresse, labelFont);
        addFormRow(form, gbc, row++, "Tél :", txtTelephone, labelFont);
        addFormRow(form, gbc, row++, "Email :", txtEmail, labelFont);

        // Boutons plus grands (120x35) et centrés
        gbc.gridx = 2; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 6));
        buttonPanel.setBackground(FORM_BG);

        JButton btnAjouter = createLargeButton("Ajouter", Color.BLUE, Color.YELLOW, Color.RED);
        JButton btnModifier = createLargeButton("Modifier", Color.GREEN, Color.BLUE, Color.YELLOW);
        JButton btnSupprimer = createLargeButton("Supprimer", Color.PINK, Color.BLACK, Color.BLUE);
        JButton btnRafraichir = createLargeButton("Rafraîchir", Color.WHITE, Color.YELLOW, Color.GRAY);

        btnAjouter.addActionListener(e -> ajouterPatient());
        btnModifier.addActionListener(e -> modifierPatient());
        btnSupprimer.addActionListener(e -> supprimerPatient());
        btnRafraichir.addActionListener(e -> rafraichirTableau());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);
        form.add(buttonPanel, gbc);

        add(form, BorderLayout.SOUTH);

        // Événements
        txtDateNaissance.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { calculerAge(); }
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) remplirChampsDepuisTableau();
        });
    }

    // Champ texte ultra compact (6 colonnes, bordure verte)
    private JTextField createTinyField() {
        JTextField f = new JTextField(6);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        return f;
    }

    // Champ numérique ultra compact
    private JFormattedTextField createTinyNumericField() {
        JFormattedTextField f = new JFormattedTextField();
        f.setColumns(6);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        ((AbstractDocument) f.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                    throws javax.swing.text.BadLocationException {
                if (string.matches("\\d*")) super.insertString(fb, offset, string, attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
                    throws javax.swing.text.BadLocationException {
                if (text.matches("\\d*")) super.replace(fb, offset, length, text, attrs);
            }
        });
        return f;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp, Font font) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.2;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(HEADER_BG);
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        panel.add(comp, gbc);
    }

    // Bouton plus grand
    private JButton createLargeButton(String text, Color bg, Color fg, Color borderColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        btn.setPreferredSize(new Dimension(120, 35));
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

    // ----- Méthodes métier (inchangées) -----
    private LocalDate parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return LocalDate.parse(s.trim(), DateTimeFormatter.ISO_LOCAL_DATE); }
        catch (Exception e) { return null; }
    }

    private void calculerAge() {
        LocalDate d = parseDate(txtDateNaissance.getText());
        lblAge.setText((d != null) ? Period.between(d, LocalDate.now()).getYears() + " ans" : "-- ans");
    }

    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty()) { erreur("Nom obligatoire"); return false; }
        if (txtPrenom.getText().trim().isEmpty()) { erreur("Prénom obligatoire"); return false; }
        if (txtDateNaissance.getText().trim().isEmpty()) { erreur("Date naissance obligatoire"); return false; }
        if (parseDate(txtDateNaissance.getText()) == null) { erreur("Format AAAA-MM-JJ"); return false; }
        return true;
    }

    private void erreur(String msg) { JOptionPane.showMessageDialog(this, "❌ " + msg); }

    private void ajouterPatient() {
        if (!validerChamps()) return;
        try {
            Patient p = new Patient();
            p.setNom(txtNom.getText().trim());
            p.setPrenom(txtPrenom.getText().trim());
            p.setDateNaissance(parseDate(txtDateNaissance.getText()));
            p.setAdresse(txtAdresse.getText());
            p.setTelephone(txtTelephone.getText());
            p.setEmail(txtEmail.getText());
            p.setMutuelle(txtMutuelle.getText());
            if (controller.ajouterPatient(p)) {
                JOptionPane.showMessageDialog(this, "✅ Patient ajouté");
                rafraichirTableau();
                viderChamps();
            } else erreur("Échec ajout");
        } catch (Exception ex) { erreur(ex.getMessage()); }
    }

    private void modifierPatient() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) { erreur("Sélectionnez un patient"); return; }
        if (!validerChamps()) return;
        try {
            int id = (int) tableModel.getValueAt(ligne, 0);
            Patient p = new Patient();
            p.setId(id);
            p.setNom(txtNom.getText().trim());
            p.setPrenom(txtPrenom.getText().trim());
            p.setDateNaissance(parseDate(txtDateNaissance.getText()));
            p.setAdresse(txtAdresse.getText());
            p.setTelephone(txtTelephone.getText());
            p.setEmail(txtEmail.getText());
            p.setMutuelle(txtMutuelle.getText());
            if (controller.modifierPatient(p)) {
                JOptionPane.showMessageDialog(this, "✅ Patient modifié");
                rafraichirTableau();
                viderChamps();
            } else erreur("Modification échouée");
        } catch (Exception ex) { erreur(ex.getMessage()); }
    }

    private void supprimerPatient() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) { erreur("Sélectionnez un patient"); return; }
        int id = (int) tableModel.getValueAt(ligne, 0);
        String nom = tableModel.getValueAt(ligne, 1).toString();
        if (JOptionPane.showConfirmDialog(this, "Supprimer " + nom + " ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (controller.supprimerPatient(id)) {
                JOptionPane.showMessageDialog(this, "✅ Patient supprimé");
                rafraichirTableau();
                viderChamps();
            } else erreur("Suppression échouée");
        }
    }

    private void remplirChampsDepuisTableau() {
        int ligne = table.getSelectedRow();
        if (ligne >= 0) {
            txtNom.setText(tableModel.getValueAt(ligne, 1).toString());
            txtPrenom.setText(tableModel.getValueAt(ligne, 2).toString());
            txtDateNaissance.setText(tableModel.getValueAt(ligne, 4).toString());
            txtTelephone.setText(tableModel.getValueAt(ligne, 5).toString());
            txtEmail.setText(tableModel.getValueAt(ligne, 6).toString());
            txtMutuelle.setText(tableModel.getValueAt(ligne, 7).toString());
            txtAdresse.setText("");
            calculerAge();
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        List<Patient> patients = controller.listerPatients();
        for (Patient p : patients) {
            int age = (p.getDateNaissance() != null) ? Period.between(p.getDateNaissance(), LocalDate.now()).getYears() : 0;
            String dateNaiss = (p.getDateNaissance() != null) ? p.getDateNaissance().toString() : "";
            tableModel.addRow(new Object[]{
                p.getId(), p.getNom(), p.getPrenom(), age, dateNaiss,
                p.getTelephone(), p.getEmail(), p.getMutuelle()
            });
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtDateNaissance.setText("");
        txtAdresse.setText("");
        txtTelephone.setText("");
        txtEmail.setText("");
        txtMutuelle.setText("");
        lblAge.setText("-- ans");
    }
}