package be.hepl.application;

import be.hepl.IsilImageProcessing.ImageProcessing.Histogramme.Histogramme;
import be.hepl.IsilImageProcessing.NonLineaire.MorphoComplexe;
import be.hepl.IsilImageProcessing.NonLineaire.MorphoElementaire;
import be.hepl.IsilImageProcessing.Seuillage.Seuillage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static be.hepl.IsilImageProcessing.Contours.ContoursNonLineaire.*;
import static be.hepl.IsilImageProcessing.ImageProcessing.Step3Utils.convertToBufferedImage;
import static be.hepl.IsilImageProcessing.ImageProcessing.Step3Utils.convertToMatrix;
import static be.hepl.IsilImageProcessing.NonLineaire.MorphoComplexe.filtreMedianCouleur;
import static be.hepl.IsilImageProcessing.Contours.ContoursLineaire.*;
import static be.hepl.IsilImageProcessing.Seuillage.Seuillage.*;


public class Application extends JFrame {
    private JLabel imageLabel;
    private BufferedImage currentImage;

    public Application() {
        // Configuration de la fenêtre principale
        setTitle("IsilImageProcessing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        String projectPath = System.getProperty("user.dir");

        // Création des composants
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);

        // Ajout des composants à la fenêtre
        add(scrollPane, BorderLayout.CENTER);

        // Charger une image par défaut (optionnel)
        // currentImage = ImageIO.read(new File("lena.jpg"));
        // displayImage(currentImage);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Filtrage Linéaire
        JMenu linearMenu = new JMenu("Filtrage linéaire");

        // Sous-menu Global
        JMenu globalMenu = new JMenu("Global");
        globalMenu.add(createMenuItem("Passe-bas idéal", e -> showFrequencyDialog("Passe-bas idéal")));
        globalMenu.add(createMenuItem("Passe-haut idéal", e -> showFrequencyDialog("Passe-haut idéal")));
        globalMenu.add(createMenuItem("Passe-bas Butterworth", e -> showButterworthDialog("Passe-bas Butterworth")));
        globalMenu.add(createMenuItem("Passe-haut Butterworth", e -> showButterworthDialog("Passe-haut Butterworth")));
        linearMenu.add(globalMenu);

        // Sous-menu Local
        JMenu localMenu = new JMenu("Local");
        localMenu.add(createMenuItem("Masque de convolution", e -> showConvolutionDialog()));
        localMenu.add(createMenuItem("Filtre moyenneur", e -> showAveragingDialog()));
        linearMenu.add(localMenu);

        menuBar.add(linearMenu);

        // Menu Traitement Non-Linéaire
        JMenu nonLinearMenu = new JMenu("Traitement non-linéaire");

        // Sous-menu Elémentaire
        JMenu elementaryMenu = new JMenu("Elémentaire");
        elementaryMenu.add(createMenuItem("Erosion", e -> showMorphoDialog("Erosion")));
        elementaryMenu.add(createMenuItem("Dilatation", e -> showMorphoDialog("Dilatation")));
        elementaryMenu.add(createMenuItem("Ouverture", e -> showMorphoDialog("Ouverture")));
        elementaryMenu.add(createMenuItem("Fermeture", e -> showMorphoDialog("Fermeture")));
        nonLinearMenu.add(elementaryMenu);

        // Sous-menu Complexe
        JMenu complexMenu = new JMenu("Complexe");
        complexMenu.add(createMenuItem("Dilatation géodésique", e -> showGeodesicDialog("Dilatation")));
        complexMenu.add(createMenuItem("Reconstruction géodésique", e -> showGeodesicDialog("Reconstruction")));
        complexMenu.add(createMenuItem("Filtre médian", e -> showMedianDialog()));
        nonLinearMenu.add(complexMenu);

        menuBar.add(nonLinearMenu);

        // Menu Histogramme
        JMenu histogramMenu = new JMenu("Histogramme");
        histogramMenu.add(createMenuItem("Afficher paramètres", e -> showImageParameters()));

        JMenu enhanceMenu = new JMenu("Rehaussement");
        enhanceMenu.add(createMenuItem("Transformation linéaire", e -> showLinearTransformDialog()));
        enhanceMenu.add(createMenuItem("Transformation linéaire avec saturation", e -> showSaturationDialog()));
        enhanceMenu.add(createMenuItem("Correction gamma", e -> showGammaDialog()));
        enhanceMenu.add(createMenuItem("Négatif", e -> applyNegative()));
        enhanceMenu.add(createMenuItem("Egalisation", e -> applyHistogramEqualization()));
        histogramMenu.add(enhanceMenu);

        menuBar.add(histogramMenu);

        // Menu Contours
        JMenu contoursMenu = new JMenu("Contours");

        JMenu linearContoursMenu = new JMenu("Linéaire");
        linearContoursMenu.add(createMenuItem("Gradient Prewitt", e -> showPrewittDialog()));
        linearContoursMenu.add(createMenuItem("Gradient Sobel", e -> showSobelDialog()));
        linearContoursMenu.add(createMenuItem("Laplacien 4-connexe", e -> applyLaplacian4()));
        linearContoursMenu.add(createMenuItem("Laplacien 8-connexe", e -> applyLaplacian8()));
        contoursMenu.add(linearContoursMenu);

        JMenu nonLinearContoursMenu = new JMenu("Non-linéaire");
        nonLinearContoursMenu.add(createMenuItem("Gradient érosion", e -> applyErosionGradient()));
        nonLinearContoursMenu.add(createMenuItem("Gradient dilatation", e -> applyDilationGradient()));
        nonLinearContoursMenu.add(createMenuItem("Gradient Beucher", e -> applyBeucherGradient()));
        nonLinearContoursMenu.add(createMenuItem("Laplacien non-linéaire", e -> applyNonLinearLaplacian()));
        contoursMenu.add(nonLinearContoursMenu);

        menuBar.add(contoursMenu);

        // Menu Seuillage
        JMenu thresholdMenu = new JMenu("Seuillage");
        thresholdMenu.add(createMenuItem("Seuillage simple", e -> showSimpleThresholdDialog()));
        thresholdMenu.add(createMenuItem("Seuillage double", e -> showDoubleThresholdDialog()));
        thresholdMenu.add(createMenuItem("Seuillage automatique", e -> applyAutoThreshold()));
        menuBar.add(thresholdMenu);

        // Menu Applications
        JMenu appsMenu = new JMenu("Applications");
        for (int i = 1; i <= 7; i++) {
            appsMenu.add(createMenuItem("Application " + i, e -> runApplication(e.getActionCommand())));
        }
        menuBar.add(appsMenu);

        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.add(createMenuItem("Ouvrir", e -> openImage()));
        fileMenu.add(createMenuItem("Enregistrer", e -> saveImage()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Quitter", e -> System.exit(0)));
        menuBar.add(fileMenu);

        return menuBar;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }

    // Méthodes pour afficher les différentes boîtes de dialogue
    private void showFrequencyDialog(String filterType)
    {
        JSpinner freqSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        Object[] message = {
                "Fréquence de coupure:", freqSpinner
        };

        int option = JOptionPane.showConfirmDialog(this, message, filterType,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int frequency = (Integer)freqSpinner.getValue();
            // Appeler la méthode de filtrage appropriée
            // currentImage = FiltrageLineaireGlobal.filtrePasseBasIdeal(currentImage, frequency);
            displayImage(currentImage);
        }
    }

    private void showButterworthDialog(String filterType) {
        JSpinner freqSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        JSpinner orderSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        Object[] message = {
                "Fréquence de coupure:", freqSpinner,
                "Ordre du filtre:", orderSpinner
        };

        int option = JOptionPane.showConfirmDialog(this, message, filterType,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int frequency = (Integer)freqSpinner.getValue();
            int order = (Integer)orderSpinner.getValue();
            // Appeler la méthode de filtrage appropriée
            // currentImage = FiltrageLineaireGlobal.filtrePasseBasButterworth(currentImage, frequency, order);
            displayImage(currentImage);
        }
    }

    private void showConvolutionDialog() {
        JTextArea maskArea = new JTextArea(5, 10);
        maskArea.setText("1 1 1\n1 1 1\n1 1 1");

        Object[] message = {
                "Entrez le masque de convolution (une ligne par rangée, valeurs séparées par des espaces):",
                new JScrollPane(maskArea)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Masque de convolution",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String[] rows = maskArea.getText().split("\n");
                double[][] mask = new double[rows.length][];

                for (int i = 0; i < rows.length; i++) {
                    String[] values = rows[i].trim().split("\\s+");
                    mask[i] = new double[values.length];
                    for (int j = 0; j < values.length; j++) {
                        mask[i][j] = Double.parseDouble(values[j]);
                    }
                }

                // Convertir en tableau 1D si nécessaire
                // currentImage = FiltrageLineaireLocal.filtreMasqueConvolution(currentImage, mask);
                displayImage(currentImage);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Format de masque invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAveragingDialog() {
        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 15, 2));

        Object[] message = {
                "Taille du masque (n x n, n impair):", sizeSpinner
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Filtre moyenneur",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int size = (Integer)sizeSpinner.getValue();
            // currentImage = FiltrageLineaireLocal.filtreMoyenneur(currentImage, size);
            displayImage(currentImage);
        }
    }

    private void showMorphoDialog(String operation)
    {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int[][] mat = convertToMatrix(currentImage);
        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 31, 2));
        Object[] message = {"Taille de l'élément structurant (impair):", sizeSpinner};
        int option = JOptionPane.showConfirmDialog(this, message, operation, JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;
        int taille = (Integer) sizeSpinner.getValue();
        int[][] result;
        switch (operation)
        {
            case "Erosion":
                result = MorphoElementaire.erosion(mat, taille);
                break;
            case "Dilatation":
                result = MorphoElementaire.dilatation(mat, taille);
                break;
            case "Ouverture":
                result = MorphoElementaire.ouverture(mat, taille);
                break;
            case "Fermeture":
                result = MorphoElementaire.fermeture(mat, taille);
                break;
            default:
                return;
        }
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void openImage() {
        //pour l'ouverture dans le bon fichier
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "/src/main/java/be/hepl/assets/images";
        JFileChooser fileChooser = new JFileChooser(imagePath);


        fileChooser.setDialogTitle("Ouvrir une image");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                currentImage = ImageIO.read(fileChooser.getSelectedFile());
                displayImage(currentImage);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image à enregistrer",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "/src/main/java/be/hepl/saves";
        JFileChooser fileChooser = new JFileChooser(imagePath);
        fileChooser.setDialogTitle("Enregistrer l'image");

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(currentImage, "png", fileChooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Image enregistrée avec succès");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(BufferedImage image) {
        if (image != null) {
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            pack();
        }
    }

    // Méthodes pour les autres fonctionnalités (à compléter)
    private void showGeodesicDialog(String operation) {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try {
            BufferedImage maskImage = ImageIO.read(chooser.getSelectedFile());


            if (maskImage.getWidth() != currentImage.getWidth() ||
                    maskImage.getHeight() != currentImage.getHeight())
            {
                JOptionPane.showMessageDialog(this,
                        "Le masque doit avoir la même taille que l'image originale",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int[][] masque = convertToMatrix(maskImage);
            int[][] image = convertToMatrix(currentImage);
            int[][] result;

            if (operation.equals("Dilatation"))
            {
                JSpinner iterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
                Object[] message = {"Nombre d'itérations:", iterSpinner};
                if (JOptionPane.showConfirmDialog(this, message, operation,
                        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
                {
                    int nbIter = (Integer) iterSpinner.getValue();
                    result = MorphoComplexe.dilatationGeodesique(image, masque, nbIter);
                } else return;
            }
            else
            {
                result = MorphoComplexe.reconstructionGeodesique(image, masque);
            }


            currentImage = convertToBufferedImage(result);
            displayImage(currentImage);

        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Erreur de chargement du masque", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showMedianDialog()
    {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Spinner pour choisir la taille du masque (impair uniquement)
        JSpinner tailleSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 31, 2));
        Object[] message = {"Taille du masque médian (impair):", tailleSpinner};

        int option = JOptionPane.showConfirmDialog(this, message, "Filtre médian",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION) return;

        int taille = (Integer) tailleSpinner.getValue();

        BufferedImage resultImage;

        if (currentImage.getType() == BufferedImage.TYPE_BYTE_GRAY)
        {
            int[][] matrix = convertToMatrix(currentImage);
            int[][] result = MorphoComplexe.filtreMedian(matrix, taille);
            resultImage = convertToBufferedImage(result);
        }
        else
        {
            resultImage = filtreMedianCouleur(currentImage, taille);
        }

        currentImage = resultImage;
        displayImage(currentImage);

    }
    private void showImageParameters()
    {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher l'histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Histogramme original");

        int min = Histogramme.minimum(imageMatrix);
        int max = Histogramme.maximum(imageMatrix);
        int lum = Histogramme.luminance(imageMatrix);
        double cont1 = Histogramme.contraste1(imageMatrix);
        double cont2 = Histogramme.contraste2(imageMatrix);

        String message = String.format(
                "Paramètres de l'image:\n" +
                        "Minimum: %d\nMaximum: %d\nLuminance: %d\n" +
                        "Contraste (écart-type): %.2f\nContraste (alternatif): %.2f",
                min, max, lum, cont1, cont2
        );

        JOptionPane.showMessageDialog(this, message, "Paramètres de l'image", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLinearTransformDialog() {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Avant transformation linéaire");

        int min = Histogramme.minimum(imageMatrix);
        int max = Histogramme.maximum(imageMatrix);

        int[] courbe = Histogramme.creeCourbeTonaleLineaireSaturation(min, max);
        int[][] result = Histogramme.rehaussement(imageMatrix, courbe);

        // Afficher histogramme après
        int[] histApres = Histogramme.Histogramme256(result);
        afficherHistogramme(histApres, "Après transformation linéaire");

        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void showSaturationDialog()
    {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Avant saturation");

        int min = Histogramme.minimum(imageMatrix);
        int max = Histogramme.maximum(imageMatrix);

        JSpinner minSpinner = new JSpinner(new SpinnerNumberModel(min, 0, max-1, 1));
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(max, min+1, 255, 1));

        Object[] message =
                {
                "Valeur minimale (smin):", minSpinner,
                "Valeur maximale (smax):", maxSpinner
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Transformation linéaire avec saturation",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION)
        {
            int smin = (Integer) minSpinner.getValue();
            int smax = (Integer) maxSpinner.getValue();
            int[] courbe = Histogramme.creeCourbeTonaleLineaireSaturation(smin, smax);
            int[][] result = Histogramme.rehaussement(imageMatrix, courbe);

            // Afficher histogramme après
            int[] histApres = Histogramme.Histogramme256(result);
            afficherHistogramme(histApres, "Après saturation");

            currentImage = convertToBufferedImage(result);
            displayImage(currentImage);
        }
    }

    private void showGammaDialog() {
        if (currentImage == null)
        {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Avant correction gamma");

        JSpinner gammaSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 5.0, 0.1));

        Object[] message = {"Valeur gamma (0.1-5.0):", gammaSpinner};

        if (JOptionPane.showConfirmDialog(
                this,
                message,
                "Correction Gamma",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        ) == JOptionPane.OK_OPTION) {

            double gamma = (Double) gammaSpinner.getValue();
            int[] courbe = Histogramme.creeCourbeTonaleGamma(gamma);
            int[][] result = Histogramme.rehaussement(imageMatrix, courbe);

            // Afficher histogramme après
            int[] histApres = Histogramme.Histogramme256(result);
            afficherHistogramme(histApres, "Après correction gamma");

            currentImage = convertToBufferedImage(result);
            displayImage(currentImage);
        }
    }

    private void applyNegative()
    {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Avant négatif");

        int[] courbe = Histogramme.creeCourbeTonaleNegatif();
        int[][] result = Histogramme.rehaussement(imageMatrix, courbe);

        // Afficher histogramme après
        int[] histApres = Histogramme.Histogramme256(result);
        afficherHistogramme(histApres, "Après négatif");

        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyHistogramEqualization()
    {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] imageMatrix = convertToMatrix(currentImage);

        // Afficher histogramme avant
        int[] histAvant = Histogramme.Histogramme256(imageMatrix);
        afficherHistogramme(histAvant, "Avant égalisation");

        int[] courbe = Histogramme.creeCourbeTonaleEgalisation(imageMatrix);
        int[][] result = Histogramme.rehaussement(imageMatrix, courbe);

        // Afficher histogramme après
        int[] histApres = Histogramme.Histogramme256(result);
        afficherHistogramme(histApres, "Après égalisation");

        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    // Méthode utilitaire pour afficher un histogramme
    private void afficherHistogramme(int[] hist, String titre)
    {
        int histWidth = 512;
        int histHeight = 400;
        BufferedImage histImage = new BufferedImage(histWidth, histHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = histImage.createGraphics();

        // Fond blanc
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, histWidth, histHeight);

        // Trouver le max pour la mise à l'échelle
        int maxCount = 0;
        for (int count : hist) {
            if (count > maxCount) maxCount = count;
        }

        // Dessiner l'histogramme
        g2d.setColor(Color.BLUE);
        int binWidth = histWidth / 256;
        for (int i = 0; i < 256; i++) {
            int height = (int)(((double)hist[i] / maxCount) * (histHeight - 20));
            g2d.fillRect(i * binWidth, histHeight - height, binWidth, height);
        }

        // Ajouter des étiquettes
        g2d.setColor(Color.BLACK);
        g2d.drawString(titre, 10, 20);
        g2d.drawString("0", 5, histHeight - 5);
        g2d.drawString("255", histWidth - 30, histHeight - 5);

        // Afficher dans une fenêtre
        JFrame frame = new JFrame("Histogramme - " + titre);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(histImage)));
        frame.pack();
        frame.setVisible(true);
    }

    private void showPrewittDialog() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Choix de la direction via un dialogue
        Object[] options = {"Horizontal (dir = 1)", "Vertical (dir = 2)"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choisissez la direction du gradient Prewitt :",
                "Gradient Prewitt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) return;

        int dir = (choice == 0) ? 1 : 2;

        // Conversion image -> matrice
        int[][] matrix = convertToMatrix(currentImage);

        // Application du filtre Prewitt
        int[][] result = gradientPrewitt(matrix, dir);

        // Conversion matrice -> BufferedImage
        currentImage = convertToBufferedImage(result);

        // Affichage de l'image résultante
        displayImage(currentImage);
    }

    private void showSobelDialog() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Choix de la direction via un dialogue
        Object[] options = {"Horizontal (dir = 1)", "Vertical (dir = 2)"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choisissez la direction du gradient Sobel :",
                "Gradient Sobel",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) return;

        int dir = (choice == 0) ? 1 : 2;

        // Conversion image -> matrice
        int[][] matrix = convertToMatrix(currentImage);

        // Application du filtre Sobel
        int[][] result = gradientSobel(matrix, dir);

        // Conversion matrice -> BufferedImage
        currentImage = convertToBufferedImage(result);

        // Affichage de l'image résultante
        displayImage(currentImage);
    }

    private void applyLaplacian4() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = laplacien4(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyLaplacian8() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = laplacien8(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyErosionGradient() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = gradientErosion(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyDilationGradient() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = gradientDilatation(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyBeucherGradient() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = gradientBeucher(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void applyNonLinearLaplacian() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = laplacienNonLineaire(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void showSimpleThresholdDialog() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Création d'un spinner pour entrer la valeur du seuil
        JSpinner seuilSpinner = new JSpinner(new SpinnerNumberModel(128, 0, 255, 1));

        Object[] message = {
                "Valeur du seuil (0-255):", seuilSpinner
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Seuillage simple",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            int seuil = (Integer) seuilSpinner.getValue();
            int[][] matrix = convertToMatrix(currentImage);
            int[][] result = seuillageSimple(matrix, seuil);
            currentImage = convertToBufferedImage(result);
            displayImage(currentImage);
        }
    }

    private void showDoubleThresholdDialog() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JSpinner seuil1Spinner = new JSpinner(new SpinnerNumberModel(85, 0, 254, 1));
        JSpinner seuil2Spinner = new JSpinner(new SpinnerNumberModel(170, 1, 255, 1));

        Object[] message = {
                "Seuil bas :", seuil1Spinner,
                "Seuil haut :", seuil2Spinner
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Seuillage double",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int seuil1 = (Integer) seuil1Spinner.getValue();
            int seuil2 = (Integer) seuil2Spinner.getValue();

            if (seuil1 >= seuil2) {
                JOptionPane.showMessageDialog(this, "Seuil bas doit être < seuil haut", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int[][] matrix = convertToMatrix(currentImage);
            int[][] result = Seuillage.seuillageDouble(matrix, seuil1, seuil2);
            currentImage = convertToBufferedImage(result);
            displayImage(currentImage);
        }
    }

    private void applyAutoThreshold() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image chargée", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[][] matrix = convertToMatrix(currentImage);
        int[][] result = Seuillage.seuillageAutomatique(matrix);
        currentImage = convertToBufferedImage(result);
        displayImage(currentImage);
    }

    private void runApplication(String appNumber) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}