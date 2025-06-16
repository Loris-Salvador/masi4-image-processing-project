package be.hepl.imageprocessing.filtragelineaire.Global.OldVersion;


public class FiltrePasseBasIdeal {


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
