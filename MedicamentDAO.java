package dao;

import connexion.ConnexionDB;
import models.Medicament;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {

    public boolean ajouter(Medicament m) {
        String sql = "INSERT INTO medicaments (nom, categorie, dosage, prix_unitaire, quantite_stock) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNom());
            stmt.setString(2, m.getCategorie());
            stmt.setString(3, m.getDosage());
            stmt.setDouble(4, m.getPrixUnitaire());
            stmt.setInt(5, m.getQuantiteStock());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Medicament> lister() {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT * FROM medicaments ORDER BY nom";
        try (Connection conn = ConnexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id_medicament"));
                m.setNom(rs.getString("nom"));
                m.setCategorie(rs.getString("categorie"));
                m.setDosage(rs.getString("dosage"));
                m.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                m.setQuantiteStock(rs.getInt("quantite_stock"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean modifier(Medicament m) {
        String sql = "UPDATE medicaments SET nom=?, categorie=?, dosage=?, prix_unitaire=?, quantite_stock=? WHERE id_medicament=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNom());
            stmt.setString(2, m.getCategorie());
            stmt.setString(3, m.getDosage());
            stmt.setDouble(4, m.getPrixUnitaire());
            stmt.setInt(5, m.getQuantiteStock());
            stmt.setInt(6, m.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM medicaments WHERE id_medicament=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean decrementerStock(int idMedicament, int quantite) {
        String sql = "UPDATE medicaments SET quantite_stock = quantite_stock - ? WHERE id_medicament = ? AND quantite_stock >= ?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantite);
            stmt.setInt(2, idMedicament);
            stmt.setInt(3, quantite);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Medicament trouverParId(int id) {
        String sql = "SELECT * FROM medicaments WHERE id_medicament = ?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id_medicament"));
                m.setNom(rs.getString("nom"));
                m.setCategorie(rs.getString("categorie"));
                m.setDosage(rs.getString("dosage"));
                m.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                m.setQuantiteStock(rs.getInt("quantite_stock"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}