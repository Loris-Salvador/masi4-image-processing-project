package be.hepl.imageprocessing.filtragelineaire.local;

import static be.hepl.imageprocessing.filtragelineaire.local.MasqueConvolution.filtreMasqueConvolution;

public class FiltreMoyenneur {

    // Filtrage moyenneur : appel de filtreMasqueConvolution avec un masque rempli de 1/(n*n)
    public static int[][] filtreMoyenneur(int[][] image, int tailleMasque) {
        double[][] masque = new double[tailleMasque][tailleMasque];
        double valeur = 1.0 / (tailleMasque * tailleMasque);

        // Remplir le masque avec la même valeur
        for (int i = 0; i < tailleMasque; i++) {
            for (int j = 0; j < tailleMasque; j++) {
                masque[i][j] = valeur;
            }
        }

        // Appel à la convolution
        return filtreMasqueConvolution(image, masque);
    }
}
