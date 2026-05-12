package models;

import java.time.LocalDate;

public class Facture {
    private int id;
    private int idPatient;
    private int idConsultation;
    private double montantTotal;
    private LocalDate dateFacture;
    private String statut;

    public Facture() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    public int getIdConsultation() { return idConsultation; }
    public void setIdConsultation(int idConsultation) { this.idConsultation = idConsultation; }
    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    public LocalDate getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}