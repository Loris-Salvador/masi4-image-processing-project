package be.hepl.ImageProcessing.FiltrageLineaire.DFT;

import be.hepl.ImageProcessing.FiltrageLineaire.Complexe.Complex;


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



