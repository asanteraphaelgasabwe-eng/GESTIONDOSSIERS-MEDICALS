/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author SHAURI
 */

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class Accueil extends JPanel {

    private JFrame parentFrame;

    public Accueil(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // ========== ENTÊTE ==========
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), getWidth(), 0, new Color(52, 152, 219));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel logoLabel = new JLabel("🏥", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 70));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("HÔPITAL GÉNÉRAL DE RÉFÉRENCE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Système de Gestion des Dossiers Médicaux", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(230, 240, 255));

        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ========== MENU AVEC ICÔNES DANS DES CERCLES (STICKS) ==========
        JPanel menuPanel = new JPanel(new GridLayout(0, 2, 25, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // Données : (icône, texte, couleur de fond)
        Object[][] items = {
            {"👥", "PATIENTS", new Color(46, 204, 113)},
            {"👨‍⚕️", "MÉDECINS", new Color(52, 152, 219)},
            {"📋", "CONSULTATIONS", new Color(241, 196, 15)},
            {"📅", "ANNÉE", new Color(155, 89, 182)},
            {"🏥", "SERVICES", new Color(230, 126, 34)},
            {"🩺", "SPÉCIALITÉS", new Color(231, 76, 60)},
            {"🫀", "CARDIOLOGIE", new Color(46, 204, 113)},
            {"🧠", "NEUROLOGIE", new Color(52, 152, 219)},
            {"🦷", "DENTISTERIE", new Color(241, 196, 15)},
            {"👶", "PÉDIATRIE", new Color(155, 89, 182)}
        };

        for (Object[] item : items) {
            menuPanel.add(createStickItem((String) item[0], (String) item[1], (Color) item[2]));
        }

        add(menuPanel, BorderLayout.CENTER);
    }

    private JPanel createStickItem(String icon, String text, Color bgColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setOpaque(false);

        // Cercle coloré contenant l'icône
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        circlePanel.setPreferredSize(new Dimension(50, 50));
        circlePanel.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        circlePanel.add(iconLabel);

        // Texte à côté
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textLabel.setForeground(new Color(41, 128, 185));

        panel.add(circlePanel);
        panel.add(textLabel);
        return panel;
    }
}