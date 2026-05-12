/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author SHAURI
 */


import connexion.ConnexionDB;
import models.Consultation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    // AJOUTER une consultation
    public boolean ajouterConsultation(Consultation consultation) {
        String sql = "INSERT INTO consultations (date_consultation, motif, diagnostic, traitement, id_patient, id_medecin) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(consultation.getDateConsultation()));
            stmt.setString(2, consultation.getMotif());
            stmt.setString(3, consultation.getDiagnostic());
            stmt.setString(4, consultation.getTraitement());
            stmt.setInt(5, consultation.getIdPatient());
            stmt.setInt(6, consultation.getIdMedecin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LISTER toutes les consultations
    public List<Consultation> listerConsultations() {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM consultations ORDER BY id_consultation DESC";
        try (Connection conn = ConnexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setId(rs.getInt("id_consultation"));
                c.setDateConsultation(rs.getDate("date_consultation").toLocalDate());
                c.setMotif(rs.getString("motif"));
                c.setDiagnostic(rs.getString("diagnostic"));
                c.setTraitement(rs.getString("traitement"));
                c.setIdPatient(rs.getInt("id_patient"));
                c.setIdMedecin(rs.getInt("id_medecin"));
                consultations.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    // ⭐ MODIFIER une consultation ⭐
    public boolean modifierConsultation(Consultation consultation) {
        String sql = "UPDATE consultations SET date_consultation=?, motif=?, diagnostic=?, traitement=?, id_patient=?, id_medecin=? WHERE id_consultation=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(consultation.getDateConsultation()));
            stmt.setString(2, consultation.getMotif());
            stmt.setString(3, consultation.getDiagnostic());
            stmt.setString(4, consultation.getTraitement());
            stmt.setInt(5, consultation.getIdPatient());
            stmt.setInt(6, consultation.getIdMedecin());
            stmt.setInt(7, consultation.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ⭐ SUPPRIMER une consultation ⭐
    public boolean supprimerConsultation(int id) {
        String sql = "DELETE FROM consultations WHERE id_consultation=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}