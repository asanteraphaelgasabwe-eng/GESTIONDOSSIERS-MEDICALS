/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author SHAURI
 */

import dao.PatientDAO;
import models.Patient;
import java.util.List;

public class PatientController {
    private PatientDAO patientDAO;

    public PatientController() {
        this.patientDAO = new PatientDAO();
    }

    public boolean ajouterPatient(Patient patient) {
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            System.err.println("Erreur : Le nom est obligatoire");
            return false;
        }
        return patientDAO.ajouterPatient(patient);
    }

    public List<Patient> listerPatients() {
        return patientDAO.listerPatients();
    }

    public boolean modifierPatient(Patient patient) {
        if (patient.getId() <= 0) return false;
        return patientDAO.modifierPatient(patient);
    }

    public boolean supprimerPatient(int id) {
        if (id <= 0) return false;
        return patientDAO.supprimerPatient(id);
    }

    public List<Patient> rechercherParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) return listerPatients();
        return patientDAO.rechercherParNom(nom);
    }
}