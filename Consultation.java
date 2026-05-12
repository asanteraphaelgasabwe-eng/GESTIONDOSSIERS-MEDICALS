/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author SHAURI
 */

import java.time.LocalDate;

public class Consultation {
    private int id;
    private LocalDate dateConsultation;
    private String motif;
    private String diagnostic;
    private String traitement;
    private int idPatient;
    private int idMedecin;

    public Consultation() {}

    public Consultation(int id, LocalDate dateConsultation, String motif, String diagnostic, String traitement, int idPatient, int idMedecin) {
        this.id = id;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(LocalDate dateConsultation) { this.dateConsultation = dateConsultation; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
}