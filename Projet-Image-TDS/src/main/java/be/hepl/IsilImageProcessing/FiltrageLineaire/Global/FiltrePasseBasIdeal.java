package be.hepl.IsilImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseBasIdeal {

    public static int[][] filtrePasseBas(int[][] image, int tailleMasque) {

        int hauteur = image.length;
        int largeur = image[0].length;

        //creation nouvelle matrice pour stocke l'image filtrée
        int[][] resultat = new int[hauteur][largeur];

        int decalage = tailleMasque / 2;
        System.out.println(decalage);

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int somme = 0;
                int count = 0;

                //parcourir le masque
                for (int j = -decalage; j <= decalage; j++) {
                    for (int i = -decalage; i <= decalage; i++) {
                        int newY = y + j;
                        int newX = x + i;

                        //vérifier si les indices sont dans les bords de l'image
                        if (newY >= 0 && newY < hauteur && newX >= 0 && newX < largeur) {
                            somme += image[newY][newX];
                            count++;
                        }
                    }
                }
                //calculer la moyenne
                if (count > 0) {
                    resultat[y][x] = somme / count;
                } else {
                    resultat[y][x] = image[y][x]; // Si aucun pixel n'est trouvé, conserver la valeur d'origine
                }
                
            }
        }


        return resultat;
    }
}
