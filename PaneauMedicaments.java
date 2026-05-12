package views;

import controllers.MedicamentController;
import models.Medicament;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PaneauMedicaments extends JPanel {

    private final MedicamentController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtCategorie, txtDosage, txtPrix, txtStock;

    public PaneauMedicaments(MedicamentController controller) {
        this.controller = controller;
        initUI();
        rafraichirTableau();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titre
        JLabel title = new JLabel("💊 GESTION DES MÉDICAMENTS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom", "Catégorie", "Dosage", "Prix unitaire", "Stock"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // En-tête du tableau
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.magenta);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("📋 Liste des médicaments"));
        add(scroll, BorderLayout.CENTER);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            "✏️ Formulaire médicament",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(41, 128, 185)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNom = new JTextField(15);
        txtCategorie = new JTextField(15);
        txtDosage = new JTextField(15);
        txtPrix = new JTextField(15);
        txtStock = new JTextField(15);

        // Ligne 1 : Nom
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNom, gbc);

        // Ligne 2 : Catégorie
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCategorie, gbc);

        // Ligne 3 : Dosage
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Dosage :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDosage, gbc);

        // Ligne 4 : Prix unitaire
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prix unitaire :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPrix, gbc);

        // Ligne 5 : Stock
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Stock initial :"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtStock, gbc);

        // Boutons
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        JButton btnAjouter = new JButton("➕ AJOUTER");
        JButton btnModifier = new JButton("✏️ MODIFIER");
        JButton btnSupprimer = new JButton("🗑️ SUPPRIMER");
        JButton btnRafraichir = new JButton("🔄 RAFRAÎCHIR");
        
        btnAjouter.setBackground(new Color(46, 204, 113));
        btnModifier.setBackground(new Color(52, 152, 219));
        btnSupprimer.setBackground(new Color(231, 76, 60));
        btnRafraichir.setBackground(new Color(241, 196, 15));
        
        btnAjouter.setForeground(Color.magenta);
        btnModifier.setForeground(Color.orange);
        btnSupprimer.setForeground(Color.black);
        btnRafraichir.setForeground(Color.cyan);
        
        btnAjouter.setFocusPainted(false);
        btnModifier.setFocusPainted(false);
        btnSupprimer.setFocusPainted(false);
        btnRafraichir.setFocusPainted(false);
        
        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnRafraichir.addActionListener(e -> rafraichirTableau());
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRafraichir);
        
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.SOUTH);
        
        // Sélection dans le tableau
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirChamps();
            }
        });
    }
    
    private void ajouter() {
        try {
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Le nom est obligatoire !");
                return;
            }
            if (txtPrix.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Le prix est obligatoire !");
                return;
            }
            
            Medicament m = new Medicament();
            m.setNom(txtNom.getText().trim());
            m.setCategorie(txtCategorie.getText().trim());
            m.setDosage(txtDosage.getText().trim());
            m.setPrixUnitaire(Double.parseDouble(txtPrix.getText().trim()));
            m.setQuantiteStock(Integer.parseInt(txtStock.getText().trim()));
            
            if (controller.ajouter(m)) {
                JOptionPane.showMessageDialog(this, "✅ Médicament ajouté avec succès !");
                rafraichirTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Prix ou stock invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifier() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à modifier", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int id = (int) tableModel.getValueAt(ligne, 0);
            Medicament m = new Medicament();
            m.setId(id);
            m.setNom(txtNom.getText().trim());
            m.setCategorie(txtCategorie.getText().trim());
            m.setDosage(txtDosage.getText().trim());
            m.setPrixUnitaire(Double.parseDouble(txtPrix.getText().trim()));
            m.setQuantiteStock(Integer.parseInt(txtStock.getText().trim()));
            
            if (controller.modifier(m)) {
                JOptionPane.showMessageDialog(this, "✅ Médicament modifié !");
                rafraichirTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Prix ou stock invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimer() {
        int ligne = table.getSelectedRow();
        if (ligne < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un médicament à supprimer", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(ligne, 0);
        String nom = tableModel.getValueAt(ligne, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Supprimer définitivement " + nom + " ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.supprimer(id)) {
                JOptionPane.showMessageDialog(this, "✅ Médicament supprimé !");
                rafraichirTableau();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void remplirChamps() {
        int ligne = table.getSelectedRow();
        if (ligne >= 0) {
            txtNom.setText(tableModel.getValueAt(ligne, 1).toString());
            txtCategorie.setText(tableModel.getValueAt(ligne, 2).toString());
            txtDosage.setText(tableModel.getValueAt(ligne, 3).toString());
            txtPrix.setText(tableModel.getValueAt(ligne, 4).toString());
            txtStock.setText(tableModel.getValueAt(ligne, 5).toString());
        }
    }
    
    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        List<Medicament> liste = controller.lister();
        for (Medicament m : liste) {
            tableModel.addRow(new Object[]{
                m.getId(), 
                m.getNom(), 
                m.getCategorie(),
                m.getDosage(), 
                m.getPrixUnitaire(), 
                m.getQuantiteStock()
            });
        }
    }
    
    private void viderChamps() {
        txtNom.setText("");
        txtCategorie.setText("");
        txtDosage.setText("");
        txtPrix.setText("");
        txtStock.setText("");
    }
}