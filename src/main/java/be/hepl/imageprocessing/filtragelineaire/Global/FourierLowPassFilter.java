package be.hepl.imageprocessing.filtragelineaire.Global;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

public class FourierLowPassFilter {

    public static int[][] apply(int[][] image, int rayon) {
        int hauteur = image.length;
        int largeur = image[0].length;

        double[][] imageCentered = new double[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageCentered[y][x] = image[y][x] * Math.pow(-1, x + y);
            }
        }

        Complex[][] freq = tf2d(imageCentered);
        filterLowPass(freq, rayon);
        double[][] imageFiltered = itf2d(freq);

        int[][] resultat = new int[hauteur][largeur];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                imageFiltered[y][x] *= Math.pow(-1, x + y);
                min = Math.min(min, imageFiltered[y][x]);
                max = Math.max(max, imageFiltered[y][x]);
            }
        }

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                resultat[y][x] = (int) (255 * (imageFiltered[y][x] - min) / (max - min));
            }
        }

        return resultat;
    }

    public static Complex[][] tf2d(double[][] image) {
        int hauteur = image.length;
        int largeur = image[0].length;
        Complex[][] temp = new Complex[hauteur][largeur];
        Complex[][] output = new Complex[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            Complex[] ligne = new Complex[largeur];
            for (int x = 0; x < largeur; x++) {
                ligne[x] = new Complex(image[y][x], 0);
            }
            Complex[] tfLigne = tf1d(ligne);
            temp[y] = tfLigne;
        }

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

    public static void filterLowPass(Complex[][] freq, int rayon) {
        int hauteur = freq.length;
        int largeur = freq[0].length;
        int centreY = hauteur / 2;
        int centreX = largeur / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                double dist = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));
                if (dist > rayon) {
                    freq[y][x] = new Complex(0, 0);
                }
            }
        }
    }
}