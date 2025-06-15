package be.hepl.imageprocessing.filtragelineaire.DFT;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;


public class TF1DCalculator{
    public static Complex[] TF1D(double[] signal) {
        int N = signal.length;
        Complex[] dft = new Complex[N];

        for (int k = 0; k < N; k++) {
            Complex somme = new Complex(0, 0);

            for (int n = 0; n < N; n++) {
                double angle = -2 * Math.PI * k * n / N;
                Complex exponentielle = new Complex(Math.cos(angle), Math.sin(angle));
                Complex terme = exponentielle.multiply(new Complex(signal[n], 0));
                somme = somme.add(terme);
            }

            dft[k] = somme;
        }

        return dft;
    }

    public static Complex[] ITF1D(Complex[] signalFreq) {
        int N = signalFreq.length;
        Complex[] idft = new Complex[N];

        for (int n = 0; n < N; n++) {
            double re = 0;
            double im = 0;

            for (int k = 0; k < N; k++) {
                double angle = 2 * Math.PI * k * n / N;
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                double realPart = signalFreq[k].getReal();
                double imagPart = signalFreq[k].getImaginary();

                // (a + ib) * (cos + i sin) = (a cos - b sin) + i (a sin + b cos)
                re += realPart * cos - imagPart * sin;
                im += realPart * sin + imagPart * cos;
            }

            idft[n] = new Complex(re / N, im / N);
        }

        return idft;
    }



    // test unitaire
    public static void main(String[] args) {
        // Exemple d'utilisation
        double[] signal = {1, 2, 3, 4};
        Complex[] dftResult = TF1D(signal);

        // Afficher les résultats
        for (int i = 0; i < dftResult.length; i++) {
            System.out.println("DFT[" + i + "] = " + dftResult[i]);
        }

        /*
        DFT[0] --> Moyenne (offset).
        DFT[1] --> Fréquence fondamentale + déphasage.
        DFT[2] --> Fréquence haute (Nyquist).
        DFT[3] --> Double de la fréquence de base (conjuguée).
         */
    }

}



