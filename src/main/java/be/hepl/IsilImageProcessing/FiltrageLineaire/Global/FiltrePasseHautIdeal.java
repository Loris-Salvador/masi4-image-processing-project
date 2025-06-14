package be.hepl.IsilImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseHautIdeal {

    public static int[][] filtrePasseHaut(int[][] image, int tailleMasque) {

        int hauteur = image.length;
        int largeur = image[0].length;

        // Filtrage passe-bas (flou)
        int[][] floutee = new int[hauteur][largeur];
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

                if (count > 0) {
                    floutee[y][x] = somme / count;
                } else {
                    floutee[y][x] = image[y][x];
                }

            }
        }

        // Calcul du passe-haut : original - flouté
        int[][] resultat = new int[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int valeur = image[y][x] - floutee[y][x];
                // Clamp la valeur entre 0 et 255
                resultat[y][x] = Math.max(0, Math.min(255, valeur));
            }
        }

        return resultat;
    }
}
