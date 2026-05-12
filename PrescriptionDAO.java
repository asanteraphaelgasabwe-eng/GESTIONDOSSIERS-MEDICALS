package dao;

import connexion.ConnexionDB;
import models.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    public boolean ajouter(Prescription p) {
        String sql = "INSERT INTO prescriptions (id_consultation, id_medicament, quantite, prix_total) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdConsultation());
            stmt.setInt(2, p.getIdMedicament());
            stmt.setInt(3, p.getQuantite());
            stmt.setDouble(4, p.getPrixTotal());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescription> listerParConsultation(int idConsultation) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE id_consultation = ?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConsultation);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getInt("id_prescription"));
                p.setIdConsultation(rs.getInt("id_consultation"));
                p.setIdMedicament(rs.getInt("id_medicament"));
                p.setQuantite(rs.getInt("quantite"));
                p.setPrixTotal(rs.getDouble("prix_total"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}