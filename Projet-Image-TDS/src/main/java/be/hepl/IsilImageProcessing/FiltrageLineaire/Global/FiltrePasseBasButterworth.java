package be.hepl.IsilImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseBasButterworth {

    public static double[][] butterworthLowPassFilter(double[][] image, double D0, int n) {
        int hauteur = image.length;
        int largeur = image[0].length;
        double[][] resultat = new double[hauteur][largeur];

        for (int u = 0; u < hauteur; u++) {
            for (int v = 0; v < largeur; v++) {
                // distance au centre
                double du = u - hauteur / 2.0;
                double dv = v - largeur / 2.0;
                double D = Math.sqrt(du * du + dv * dv);

                double H = 1.0 / (1.0 + Math.pow(D / D0, 2 * n));

                resultat[u][v] = image[u][v] * H;
            }
        }

        return resultat;
    }
}
