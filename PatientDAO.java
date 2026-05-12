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
import models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // AJOUTER
    public boolean ajouterPatient(Patient patient) {
        String sql = "INSERT INTO patients (nom, prenom, date_naissance, adresse, telephone, email, mutuelle) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, patient.getDateNaissance() != null ? Date.valueOf(patient.getDateNaissance()) : null);
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getMutuelle());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LISTER
    public List<Patient> listerPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id_patient DESC";
        try (Connection conn = ConnexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id_patient"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setDateNaissance(rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null);
                p.setAdresse(rs.getString("adresse"));
                p.setTelephone(rs.getString("telephone"));
                p.setEmail(rs.getString("email"));
                p.setMutuelle(rs.getString("mutuelle"));
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // MODIFIER
    public boolean modifierPatient(Patient patient) {
        String sql = "UPDATE patients SET nom=?, prenom=?, date_naissance=?, adresse=?, telephone=?, email=?, mutuelle=? WHERE id_patient=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, patient.getDateNaissance() != null ? Date.valueOf(patient.getDateNaissance()) : null);
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            stmt.setString(6, patient.getEmail());
            stmt.setString(7, patient.getMutuelle());
            stmt.setInt(8, patient.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SUPPRIMER
    public boolean supprimerPatient(int id) {
        String sql = "DELETE FROM patients WHERE id_patient=?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // RECHERCHER PAR NOM
    public List<Patient> rechercherParNom(String nom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ?";
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id_patient"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setDateNaissance(rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null);
                p.setAdresse(rs.getString("adresse"));
                p.setTelephone(rs.getString("telephone"));
                p.setEmail(rs.getString("email"));
                p.setMutuelle(rs.getString("mutuelle"));
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}