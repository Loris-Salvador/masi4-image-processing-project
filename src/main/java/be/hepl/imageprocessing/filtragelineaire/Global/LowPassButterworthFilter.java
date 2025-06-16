package be.hepl.imageprocessing.filtragelineaire.Global;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

public class LowPassButterworthFilter {

    public static void filterButterworthLowPass(Complex[][] freq, int D0, int n) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int du = y - centreY;
                int dv = x - centreX;
                double D2 = du * du + dv * dv;
                if (D2 == 0) D2 = 1;

                double ratio = D2 / (double)(D0 * D0);
                double H = 1.0 / (1.0 + Math.pow(ratio, n * 2));

                double real = freq[y][x].getReal() * H;
                double imag = freq[y][x].getImaginary() * H;
                freq[y][x] = new Complex(real, imag);
            }
        }
    }
}
