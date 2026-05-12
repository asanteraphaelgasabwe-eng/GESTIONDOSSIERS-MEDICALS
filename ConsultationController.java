/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author SHAURI
 */


import dao.ConsultationDAO;
import models.Consultation;
import java.util.List;

public class ConsultationController {
    private ConsultationDAO consultationDAO;

    public ConsultationController() {
        this.consultationDAO = new ConsultationDAO();
    }

    public boolean ajouterConsultation(Consultation consultation) {
        if (consultation.getMotif() == null || consultation.getMotif().trim().isEmpty()) {
            System.err.println("Erreur : Le motif est obligatoire");
            return false;
        }
        return consultationDAO.ajouterConsultation(consultation);
    }

    public List<Consultation> listerConsultations() {
        return consultationDAO.listerConsultations();
    }

    public boolean supprimerConsultation(int id) {
        if (id <= 0) return false;
        return consultationDAO.supprimerConsultation(id);
    }
}