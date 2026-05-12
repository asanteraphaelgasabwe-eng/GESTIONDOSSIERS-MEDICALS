package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/dossier_medical";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir la connexion
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL non trouvé : " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            return null;
        }
    }

    // Pour tester la connexion
    public static void main(String[] args) {
        Connection conn = ConnexionDB.getConnection();
        if (conn != null) {
            System.out.println("✅ Connexion réussie à MySQL !");
        } else {
            System.out.println("❌ Échec de connexion. Vérifie XAMPP (MySQL démarré) et la base 'dossier_medical'.");
        }
    }
}