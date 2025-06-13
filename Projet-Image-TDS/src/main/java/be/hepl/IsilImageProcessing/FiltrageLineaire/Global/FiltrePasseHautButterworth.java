package be.hepl.IsilImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseHautButterworth {

    public static double[][] butterworthHighPassFilter(double[][] imageFFT, double D0, int n) {
        int hauteur = imageFFT.length;
        int largeur = imageFFT[0].length;
        double[][] resultat = new double[hauteur][largeur];

        for (int u = 0; u < hauteur; u++) {
            for (int v = 0; v < largeur; v++) {
                double du = u - hauteur / 2.0;
                double dv = v - largeur / 2.0;
                double D = Math.sqrt(du * du + dv * dv);

                // Butterworth passe-haut
                double H = 1.0 / (1.0 + Math.pow(D0 / (D + 0.0001), 2 * n)); // +0.0001 évite division par 0
                resultat[u][v] = imageFFT[u][v] * H;
            }
        }

        return resultat;
    }
}
