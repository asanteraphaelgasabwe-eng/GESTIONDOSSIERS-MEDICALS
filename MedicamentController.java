package controllers;

import dao.MedicamentDAO;
import models.Medicament;
import java.util.List;

public class MedicamentController {
    private MedicamentDAO dao = new MedicamentDAO();

    public boolean ajouter(Medicament m) {
        return dao.ajouter(m);
    }

    public List<Medicament> lister() {
        return dao.lister();
    }

    public boolean modifier(Medicament m) {
        return dao.modifier(m);
    }

    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }

    public boolean decrementerStock(int id, int quantite) {
        return dao.decrementerStock(id, quantite);
    }

    public Medicament trouverParId(int id) {
        return dao.trouverParId(id);
    }
}