package be.hepl.imageprocessing.filtragelineaire.dft;

import be.hepl.imageprocessing.filtragelineaire.complex.Complex;

public class Fourier {

    public static Complex[][] tf2d(double[][] image) {
        int hauteur = image.length;
        int largeur = image[0].length;
        Complex[][] temp = new Complex[hauteur][largeur];
        Complex[][] output = new Complex[hauteur][largeur];

        // Transformée de Fourier 1D sur les lignes
        for (int y = 0; y < hauteur; y++) {
            Complex[] ligne = new Complex[largeur];
            for (int x = 0; x < largeur; x++) {
                ligne[x] = new Complex(image[y][x], 0);
            }
            Complex[] tfLigne = tf1d(ligne);
            temp[y] = tfLigne;
        }

        // Transformée de Fourier 1D sur les colonnes
        for (int x = 0; x < largeur; x++) {
            Complex[] colonne = new Complex[hauteur];
            for (int y = 0; y < hauteur; y++) {
                colonne[y] = temp[y][x];
            }
            Complex[] tfCol = tf1d(colonne);
            for (int y = 0; y < hauteur; y++) {
                output[y][x] = tfCol[y];
            }
        }

        return output;
    }

    public static double[][] itf2d(Complex[][] freq) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        Complex[][] temp = new Complex[hauteur][largeur];
        double[][] output = new double[hauteur][largeur];

        // Transformée de Fourier inverse 1D sur les lignes
        for (int x = 0; x < largeur; x++) {
            Complex[] colonne = new Complex[hauteur];
            for (int y = 0; y < hauteur; y++) {
                colonne[y] = freq[y][x];
            }
            Complex[] itfCol = itf1d(colonne);
            for (int y = 0; y < hauteur; y++) {
                temp[y][x] = itfCol[y];
            }
        }

        // Transformée de Fourier inverse 1D sur les colonnes
        for (int y = 0; y < hauteur; y++) {
            Complex[] ligne = itf1d(temp[y]);
            for (int x = 0; x < largeur; x++) {
                output[y][x] = ligne[x].getReal();
            }
        }

        return output;
    }

    public static Complex[] tf1d(Complex[] signal) {
        int N = signal.length;
        Complex[] result = new Complex[N];

        // Transformée de Fourier discrète 1D
        for (int k = 0; k < N; k++) {
            double sumRe = 0;
            double sumIm = 0;
            for (int n = 0; n < N; n++) {
                double angle = -2 * Math.PI * k * n / N;
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                double re = signal[n].getReal();
                double im = signal[n].getImaginary();
                sumRe += re * cos - im * sin;
                sumIm += re * sin + im * cos;
            }
            result[k] = new Complex(sumRe, sumIm);
        }

        return result;
    }

    public static Complex[] itf1d(Complex[] freq) {
        int N = freq.length;
        Complex[] result = new Complex[N];

        // Transformée de Fourier inverse discrète 1D
        for (int k = 0; k < N; k++) {
            double sumRe = 0;
            double sumIm = 0;
            for (int n = 0; n < N; n++) {
                double angle = 2 * Math.PI * k * n / N;
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                double re = freq[n].getReal();
                double im = freq[n].getImaginary();
                sumRe += re * cos - im * sin;
                sumIm += re * sin + im * cos;
            }
            result[k] = new Complex(sumRe / N, sumIm / N);
        }

        return result;
    }
}
