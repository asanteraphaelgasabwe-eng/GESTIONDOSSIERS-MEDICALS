package models;

public class Prescription {
    private int id;
    private int idConsultation;
    private int idMedicament;
    private int quantite;
    private double prixTotal;

    public Prescription() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdConsultation() { return idConsultation; }
    public void setIdConsultation(int idConsultation) { this.idConsultation = idConsultation; }
    public int getIdMedicament() { return idMedicament; }
    public void setIdMedicament(int idMedicament) { this.idMedicament = idMedicament; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }
}