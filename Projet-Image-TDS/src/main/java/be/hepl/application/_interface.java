package be.hepl.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class _interface extends JFrame {

    public _interface() {
        setTitle("Traitement Image");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        JMenuBar menuBar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");

        JMenuItem nouveauItem = new JMenuItem("Nouveau");
        JMenuItem ouvrirItem = new JMenuItem("Ouvrir");
        JMenuItem enregistrerItem = new JMenuItem("Enregistrer");
        JMenuItem quitterItem = new JMenuItem("Quitter");

        quitterItem.addActionListener(e -> System.exit(0));

        menuFichier.add(nouveauItem);
        menuFichier.add(ouvrirItem);
        menuFichier.add(enregistrerItem);
        menuFichier.addSeparator(); // Séparateur
        menuFichier.add(quitterItem);

        // Ajout d'autres menus si besoin
        JMenu menuEdition = new JMenu("Édition");
        JMenu menuAffichage = new JMenu("Affichage");
        JMenu menuAide = new JMenu("Aide");

        menuBar.add(menuFichier);
        menuBar.add(menuEdition);
        menuBar.add(menuAffichage);
        menuBar.add(menuAide);

        setJMenuBar(menuBar);

        JLabel labelTitre = new JLabel("Bienvenue dans l'application", JLabel.CENTER);
        labelTitre.setFont(new Font("Arial", Font.BOLD, 18));

        JButton boutonAction = new JButton("Cliquez ici");
        JTextField champTexte = new JTextField(20);
        JTextArea zoneTexte = new JTextArea(10, 40);
        zoneTexte.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(zoneTexte);

        boutonAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texte = champTexte.getText();
                if (!texte.isEmpty()) {
                    zoneTexte.append("Vous avez saisi: " + texte + "\n");
                    champTexte.setText("");
                }
            }
        });

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        JPanel panelHaut = new JPanel();
        JPanel panelCentre = new JPanel();
        JPanel panelBas = new JPanel();

        panelHaut.add(labelTitre);

        panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.Y_AXIS));
        panelCentre.add(new JLabel("Entrez du texte:"));
        panelCentre.add(champTexte);
        panelCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentre.add(boutonAction);
        panelCentre.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentre.add(scrollPane);

        panelBas.add(new JLabel("© 2025 HEPL Application Traitement Image"));

        panelPrincipal.add(panelHaut, BorderLayout.NORTH);
        panelPrincipal.add(panelCentre, BorderLayout.CENTER);
        panelPrincipal.add(panelBas, BorderLayout.SOUTH);

        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panelPrincipal);
    }
}