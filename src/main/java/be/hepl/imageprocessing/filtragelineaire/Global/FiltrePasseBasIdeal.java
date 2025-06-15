package be.hepl.imageprocessing.filtragelineaire.Global;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;
import be.hepl.imageprocessing.filtragelineaire.DFT.TF2DCalculator;

public class FiltrePasseBasIdeal {

    // Applique un filtre passe-bas idéal via TF2D
    // Applique un filtre passe-bas idéal en domaine fréquentiel (TF2D)
    public static int[][] filtrePasseBasTF(int[][] image, int rayon) {
        int hauteur = image.length;
        int largeur = image[0].length;

        // Centrage de l'image avec (-1)^{x+y} et conversion en double[][]
        double[][] imageDouble = new double[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageDouble[y][x] = image[y][x] * Math.pow(-1, x + y);
            }
        }

        // TF2D
        Complex[][] tf2d = TF2DCalculator.TF2D(imageDouble);

        // Application du filtre passe-bas idéal
        Complex[][] tf2dFiltree = appliquerFiltrePasseBasIdeal(tf2d, rayon);

        // Transformée de Fourier inverse
        int[][] imageFiltrée = TF2DCalculator.ITF2D(tf2dFiltree);

        // Réappliquer (-1)^{x+y} + normalisation
        int[][] imageFinale = new int[hauteur][largeur];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int val = (int) (imageFiltrée[y][x] * Math.pow(-1, x + y));
                imageFinale[y][x] = val;
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
        }

        // Mise à l'échelle entre 0 et 255
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageFinale[y][x] = 255 * (imageFinale[y][x] - min) / (max - min);
            }
        }

        return imageFinale;
    }

    // Applique le masque passe-bas idéal en fréquence
    public static Complex[][] appliquerFiltrePasseBasIdeal(Complex[][] freqImage, int rayon) {
        int hauteur = freqImage.length;
        int largeur = freqImage[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                double distance = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));
                if (distance > rayon) {
                    freqImage[y][x] = new Complex(0, 0);
                }
            }
        }

        return freqImage;
    }

    // Ancien filtre passe-bas spatial --> mais pas demandé dans l'énoncé
    public static int[][] filtrePasseBas(int[][] image, int tailleMasque) {
        int hauteur = image.length;
        int largeur = image[0].length;

        int[][] resultat = new int[hauteur][largeur];
        int decalage = tailleMasque / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int somme = 0;
                int count = 0;

                for (int j = -decalage; j <= decalage; j++) {
                    for (int i = -decalage; i <= decalage; i++) {
                        int newY = y + j;
                        int newX = x + i;
                        if (newY >= 0 && newY < hauteur && newX >= 0 && newX < largeur) {
                            somme += image[newY][newX];
                            count++;
                        }
                    }
                }

                resultat[y][x] = (count > 0) ? somme / count : image[y][x];
            }
        }

        return resultat;
    }
}
