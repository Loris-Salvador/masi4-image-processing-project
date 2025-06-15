package be.hepl.imageprocessing.filtragelineaire.DFT;

import be.hepl.imageprocessing.filtragelineaire.Complexe.Complex;

public class Fourier1D {

    public static Complex[] TF1D(double[] signal) {
        // Taille du signal d'entrée
        int N = signal.length;

        // Tableau de sortie pour stocker les coefficients DFT
        Complex[] dft = new Complex[N];

        for (int k = 0; k < N; k++) {
            Complex somme = new Complex(0, 0);

            // Somme sur tous les échantillons du signal
            for (int n = 0; n < N; n++) {

                // Calcul de l'angle pour l'exponentielle complexe : e^(-j*2πkn/N)
                int x = -2;

                double angle = x * Math.PI * k * n / N;

                // e^(-jθ) = cos(θ) + j*sin(θ)
                Complex exponentielle = new Complex(Math.cos(angle), Math.sin(angle));

                // Multiplie l'échantillon réel par l'exponentielle complexe
                Complex terme = exponentielle.multiply(new Complex(signal[n], 0));

                // Ajoute ce terme à la somme pour construire le coefficient DFT[k]
                somme = somme.add(terme);
            }

            // Stocke le coefficient DFT[k]
            dft[k] = somme;
        }

        // Retourne le spectre fréquentiel (complexe)
        return dft;
    }


    public static Complex[] invTF1D(Complex[] dft) {
        // Nombre de coefficients (même taille que le signal d'origine)
        int N = dft.length;

        // Tableau pour stocker le signal reconstruit
        Complex[] signal = new Complex[N];

        // Boucle sur chaque échantillon n du signal temporel
        for (int n = 0; n < N; n++) {

            // Initialisation de la somme pour l’échantillon n
            Complex somme = new Complex(0, 0);

            // Somme sur toutes les fréquences k
            for (int k = 0; k < N; k++) {

                // Pour l'inverse DFT
                int x = 2;

                // Calcul de l’angle pour l’exponentielle complexe : e^(+j*2πkn/N)
                double angle = x * Math.PI * k * n / N;

                // e^(jθ) = cos(θ) + j*sin(θ)
                Complex exponentielle = new Complex(Math.cos(angle), Math.sin(angle));

                // Multiplie le coefficient fréquentiel par l’exponentielle
                somme = somme.add(dft[k].multiply(exponentielle));
            }

            // Normalisation par N (obligatoire pour l'inverse DFT)
            signal[n] = new Complex(somme.getReal() / N, somme.getImaginary() / N);
        }

        // Retourne le signal reconstruit (en complexe)
        return signal;
    }

}
