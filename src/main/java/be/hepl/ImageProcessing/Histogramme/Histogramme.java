package be.hepl.ImageProcessing.Histogramme;

public class Histogramme {

    public static int[] Histogramme256(int mat[][])
    {
        int M = mat.length;
        int N = mat[0].length;
        int histo[] = new int[256];

        for(int i=0 ; i<256 ; i++) histo[i] = 0;

        for(int i=0 ; i<M ; i++)
            for(int j=0 ; j<N ; j++)
                if ((mat[i][j] >= 0) && (mat[i][j]<=255)) histo[mat[i][j]]++;

        return histo;
    }

    // Méthodes pour l'étape 3
    //Parcourt l’image pour trouver le pixel le plus sombre, plus petit niveau de gris
    public static int minimum(int[][] image) {
        int min = 255;
        for (int[] row : image) {
            for (int pixel : row) {
                if (pixel < min) {
                    min = pixel;
                }
            }
        }
        return min;
    }

    //Parcourt l’image pour trouver le pixel le plus clair, plus grand niveau de gris
    public static int maximum(int[][] image) {
        int max = 0;
        for (int[] row : image) {
            for (int pixel : row) {
                if (pixel > max) {
                    max = pixel;
                }
            }
        }
        return max;
    }

    //Parcourt l’image pour calculer la luminance, moyenne des niveaux de gris
    //"quelle est la lumière générale de l’image"
    public static int luminance(int[][] image) {
        long sum = 0;
        int count = 0;
        for (int[] row : image) {
            for (int pixel : row) {
                sum += pixel;
                count++;
            }
        }
        return count == 0 ? 0 : (int)(sum / count);
    }

    //Parcourt l’image pour calculer le contraste, écart-type des niveaux de gris
    //Plus l’écart-type est grand, plus l’image a des zones très claires et très sombres
    public static double contraste1(int[][] image)  //
    {
        int lum = luminance(image); // moyenne
        long sum = 0; // somme (xᵢ - μ)²
        int count = 0; //N
        for (int[] row : image) {
            for (int pixel : row) {
                sum += (long) (pixel - lum) * (pixel - lum); // (xᵢ - μ)²
                count++;
            }
        }
        return count == 0 ? 0 : Math.sqrt((double)sum / count); // ecart-type
    }

    //méthode + simple pour calculer le contraste => Contraste = (max - min) / (max + min)
    public static double contraste2(int[][] image) {
        int min = minimum(image);
        int max = maximum(image);
        return (max == 0 && min == 0) ? 0 : (double)(max - min) / (max + min);
    }


    public static int[][] rehaussement(int[][] image, int[] courbeTonale)
    {
        int width = image.length;
        int height = image[0].length;
        int[][] result = new int[width][height];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++) {
                int pixel = Math.max(0, Math.min(255, image[i][j]));
                result[i][j] = courbeTonale[pixel];
            }
        }
        return result;
    }

    public static int[] creeCourbeTonaleLineaireSaturation(int smin, int smax) {
        int[] courbe = new int[256];
        for (int i = 0; i < 256; i++) {
            if (i <= smin) {
                courbe[i] = 0;
            } else if (i >= smax) {
                courbe[i] = 255;
            } else {
                courbe[i] = (int)(255 * (i - smin) / (double)(smax - smin));
            }
        }
        return courbe;
    }

    //Sert à rendre l’image plus claire ou plus foncée rn fct du gamma choisi
    public static int[] creeCourbeTonaleGamma(double gamma) {
        int[] courbe = new int[256];
        for (int i = 0; i < 256; i++) {
            courbe[i] = (int)(255 * Math.pow(i / 255.0, 1.0 / gamma));
        }
        return courbe;
    }

    public static int[] creeCourbeTonaleNegatif()
    {
        int[] courbe = new int[256];
        for (int i = 0; i < 256; i++) {
            courbe[i] = 255 - i;
        }
        return courbe;
    }

    public static int[] creeCourbeTonaleEgalisation(int[][] image) {
        int[] hist = Histogramme256(image);
        int totalPixels = image.length * image[0].length;
        int[] courbe = new int[256];

        // Calcul de l'histogramme cumulé
        int sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += hist[i];
            courbe[i] = (int)(255 * sum / (double)totalPixels);
        }

        return courbe;
    }


}