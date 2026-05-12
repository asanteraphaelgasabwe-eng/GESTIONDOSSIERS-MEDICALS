/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author SHAURI
 */


import controllers.MedecinController;
import models.Medecin;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PanneauMedecins extends JPanel {

    private final MedecinController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrenom, txtSpecialite, txtEmail;
    private JFormattedTextField txtTelephone;

    private final Color HEADER_BG = new Color(0, 80, 120);
    private final Color HEADER_FG = Color.white;
    private final Color FORM_BG = new Color(248, 250, 252);

    public PanneauMedecins(MedecinController controller) {
        this.controller = controller;
        initUI();
        rafraichirTableau();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 6));
        setBackground(new Color(240, 245, 250));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // ----- TITRE -----
        JLabel title = new JLabel("👨‍⚕️ GESTION DES MÉDECINS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(HEADER_BG);
        add(title, BorderLayout.NORTH);

        // ----- TABLEAU -----
        String[] colonnes = {"ID", "Nom", "Prénom", "Spécialité", "Téléphone", "Email"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        table.setIntercellSpacing(new Dimension(2, 2));

        // En-tête renforcé
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBackground(new Color(2, 90, 180));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Alternance des lignes
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                if (!isSel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE: new Color(245, 248, 250));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(HEADER_BG, 1));
        add(scroll, BorderLayout.CENTER);

        // ----- FORMULAIRE TRÈS COMPACT (bordure verte, champs étroits) -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(FORM_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HEADER_BG, 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        form.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(HEADER_BG, 1),
            "Enregistrement médecin",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            HEADER_BG
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 5;

        // Champs ultra compacts (6 colonnes, bordure verte)
        txtNom = createTinyField();
        txtPrenom = createTinyField();
        txtSpecialite = createTinyField();
        txtTelephone = createTinyNumericField();
        txtEmail = createTinyField();

        Font labelFont = new Font("Segoe UI", Font.BOLD, 9);
        int row = 0;
        addFormRow(form, gbc, row++, "Nom :", txtNom, labelFont);
        addFormRow(form, gbc, row++, "Prénom :", txtPrenom, labelFont);
        addFormRow(form, gbc, row++, "Spécialité :", txtSpecialite, labelFont);
        addFormRow(form, gbc, row++, "Tél :", txtTelephone, labelFont);
        addFormRow(form, gbc, row++, "Email :", txtEmail, labelFont);

        // Boutons plus grands et centrés
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttonPanel.setBackground(FORM_BG);

        JButton btnAjouter = createLargeButton("Ajouter", Color.BLUE, Color.BLACK, Color.RED);
        JButton btnModifier = createLargeButton("Modifier", Color.BLACK, Color.WHITE, Color.BLUE);
        JButton btnSupprimer = createLargeButton("Supprimer", Color.BLUE, Color.BLACK, Color.WHITE);
        JButton btnRafraichir = createLargeButton("Rafraîchir", Color.BLUE, Color.WHITE, Color.BLACK);

        btnAjouter.addActionListener(e -> ajouterMedecin());
        btnModifier.addActionListener(e -> modifierMedecin());
        btnSupprimer.addActionListener(e -> supprimerMedecin());
        btnRafraichir.addActionListener(e -> rafraichirTableau());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);
        form.add(buttonPanel, gbc);

        add(form, BorderLayout.SOUTH);

        // Sélection ligne → remplir formulaire
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) remplirChampsDepuisTableau();
        });
    }

    // ----- Champs texte avec bordure verte (6 colonnes) -----
    private JTextField createTinyField() {
        JTextField f = new JTextField(6);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        return f;
    }

    // Champ téléphone (chiffres seulement)
    private JFormattedTextField createTinyNumericField() {
        JFormattedTextField f = new JFormattedTextField();
        f.setColumns(6);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 10));
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

    // Bouton de taille moyenne (120x35) avec couleurs personnalisées
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

    // ----- Méthodes métier -----
    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty()) {
            erreur("Nom obligatoire");
            return false;
        }
        if (txtPrenom.getText().trim().isEmpty()) {
            erreur("Prénom obligatoire");
            return false;
        }
        return true;
    }

    private void erreur(String msg) {
        JOptionPane.showMessageDialog(this, "❌ " + msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void ajouterMedecin() {
        if (!validerChamps()) return;
        try {
            Medecin m = new Medecin();
            m.setNom(txtNom.getText().trim());
            m.setPrenom(txtPrenom.getText().trim());
            m.setSpecialite(txtSpecialite.getText());
            m.setTelephone(txtTelephone.getText());
            m.setEmail(txtEmail.getText());
            if (controller.ajouterMedecin(m)) {
                JOptionPane.showMessageDialog(this, "✅ Médecin ajouté");
                rafraichirTableau();
                viderChamps();
            } else {
                erreur("Échec de l'ajout (vérifiez la base)");
            }
        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void modifierMedecin() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) {
            erreur("Sélectionnez un médecin dans le tableau");
            return;
        }
        if (!validerChamps()) return;
        try {
            int id = (int) tableModel.getValueAt(ligne, 0);
            Medecin m = new Medecin();
            m.setId(id);
            m.setNom(txtNom.getText().trim());
            m.setPrenom(txtPrenom.getText().trim());
            m.setSpecialite(txtSpecialite.getText());
            m.setTelephone(txtTelephone.getText());
            m.setEmail(txtEmail.getText());
            if (controller.modifierMedecin(m)) {
                JOptionPane.showMessageDialog(this, "✅ Médecin modifié");
                rafraichirTableau();
                viderChamps();
            } else {
                erreur("Modification échouée");
            }
        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void supprimerMedecin() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) {
            erreur("Sélectionnez un médecin");
            return;
        }
        int id = (int) tableModel.getValueAt(ligne, 0);
        String nom = tableModel.getValueAt(ligne, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer définitivement " + nom + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.supprimerMedecin(id)) {
                JOptionPane.showMessageDialog(this, "✅ Médecin supprimé");
                rafraichirTableau();
                viderChamps();
            } else {
                erreur("Suppression échouée");
            }
        }
    }

    private void remplirChampsDepuisTableau() {
        int ligne = table.getSelectedRow();
        if (ligne >= 0) {
            txtNom.setText(tableModel.getValueAt(ligne, 1).toString());
            txtPrenom.setText(tableModel.getValueAt(ligne, 2).toString());
            txtSpecialite.setText(tableModel.getValueAt(ligne, 3).toString());
            txtTelephone.setText(tableModel.getValueAt(ligne, 4).toString());
            txtEmail.setText(tableModel.getValueAt(ligne, 5).toString());
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        List<Medecin> medecins = controller.listerMedecins();
        for (Medecin m : medecins) {
            tableModel.addRow(new Object[]{
                m.getId(), m.getNom(), m.getPrenom(),
                m.getSpecialite(), m.getTelephone(), m.getEmail()
            });
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtSpecialite.setText("");
        txtTelephone.setText("");
        txtEmail.setText("");
    }
}