package dao;

import connexion.ConnexionDB;
import models.Facture;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public boolean ajouter(Facture f) {
        String sql = "INSERT INTO factures (id_patient, id_consultation, montant_total, date_facture, statut) VALUES (?,?,?,?,?)";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, f.getIdPatient());
            stmt.setInt(2, f.getIdConsultation());
            stmt.setDouble(3, f.getMontantTotal());
            stmt.setDate(4, Date.valueOf(f.getDateFacture()));
            stmt.setString(5, f.getStatut());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Facture> listerParPatient(int idPatient) {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM factures WHERE id_patient = ? ORDER BY date_facture DESC";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Facture f = new Facture();
                f.setId(rs.getInt("id_facture"));
                f.setIdPatient(rs.getInt("id_patient"));
                f.setIdConsultation(rs.getInt("id_consultation"));
                f.setMontantTotal(rs.getDouble("montant_total"));
                f.setDateFacture(rs.getDate("date_facture").toLocalDate());
                f.setStatut(rs.getString("statut"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean payer(int idFacture) {
        String sql = "UPDATE factures SET statut = 'Paye' WHERE id_facture = ?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFacture);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}