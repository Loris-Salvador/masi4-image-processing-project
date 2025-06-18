package be.hepl.imageprocessing.filtragelineaire.global;

import be.hepl.imageprocessing.filtragelineaire.complex.Complex;

public class HighPassFilter {

    public static void filterHighPass(Complex[][] freq, int rayon) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        // Application du filtre passe-haut idéal
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                // Calcul de la distance du pixel au centre
                double dist = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));
                if (dist <= rayon) {
                    freq[y][x] = new Complex(0, 0);
                }
            }
        }
    }
}
