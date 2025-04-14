package be.hepl.IsilImageProcessing.FiltrePasseBas;

import be.hepl.IsilImageProcessing.Complex.Complex;
import be.hepl.IsilImageProcessing.DFT.TF2DCalulator;

public class FiltrePasseBasIdeal {
    public static double[][] filtrePasseBasIdeal(double[][] image, double frequenceCoupure){
        int hauteur = image.length;
        int largeur = image[0].length;

        // Calculer la DFT de l'image
        Complex[][] ftImage = TF2DCalulator.TF2D(image);

        // Créer le filtre passe-bas idéal
        double[][] filtre = new double[hauteur][largeur];


        double[][] imageFilter = new double[0][];
        return imageFilter;
    }

}
