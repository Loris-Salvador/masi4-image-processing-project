package be.hepl.imageprocessing.filtragelineaire.Local;

public class MasqueConvolution {

    public static int[][] filtreMasqueConvolution(int[][] image, double[][] masque) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int tailleMasque = masque.length;
        int decalage = tailleMasque / 2;

        int[][] resultat = new int[hauteur][largeur];

        // Parcours de chaque pixel de l’image
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                double somme = 0;

                // Application du masque autour du pixel (x, y)
                for (int j = -decalage; j <= decalage; j++) {
                    for (int i = -decalage; i <= decalage; i++) {
                        int newY = y + j;
                        int newX = x + i;

                        // Vérifier les bords de l’image
                        if (newY >= 0 && newY < hauteur && newX >= 0 && newX < largeur) {
                            somme += image[newY][newX] * masque[j + decalage][i + decalage];
                        }
                    }
                }

                // Clamp la valeur entre 0 et 255
                int valeur = (int) Math.round(somme);
                valeur = Math.max(0, Math.min(255, valeur));
                resultat[y][x] = valeur;
            }
        }

        return resultat;
    }
}
