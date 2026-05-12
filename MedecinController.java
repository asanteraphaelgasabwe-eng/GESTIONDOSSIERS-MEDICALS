/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author SHAURI
 */


import dao.MedecinDAO;
import models.Medecin;
import java.util.List;

public class MedecinController {
    private MedecinDAO medecinDAO;

    public MedecinController() {
        this.medecinDAO = new MedecinDAO();
    }

    public boolean ajouterMedecin(Medecin medecin) {
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            System.err.println("Erreur : Le nom est obligatoire");
            return false;
        }
        return medecinDAO.ajouterMedecin(medecin);
    }

    public List<Medecin> listerMedecins() {
        return medecinDAO.listerMedecins();
    }

    public boolean modifierMedecin(Medecin medecin) {
        if (medecin.getId() <= 0) return false;
        return medecinDAO.modifierMedecin(medecin);
    }

    public boolean supprimerMedecin(int id) {
        if (id <= 0) return false;
        return medecinDAO.supprimerMedecin(id);
    }
}