package be.hepl.IsilImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseHautButterworth {

    public static int[][] butterworthHighPassFilter(int[][] img, int D0, int n) {
        int h = img.length, w = img[0].length;
        int[][] res = new int[h][w];
        int scale = 10000, D0s = D0 * scale; // facteur d'échelle pour simuler les réels

        for (int u = 0; u < h; u++) {
            int du = u - h / 2; // distance verticale au centre
            for (int v = 0; v < w; v++) {
                int dv = v - w / 2;
                int D2 = du * du + dv * dv; // distance au carré
                if (D2 == 0) D2 = 1; // éviter division par zéro

                // ratio = (D0 / D)^2 à l’échelle
                long r = ((long) D0s * D0s) / D2;

                // ratio^(2n)
                long p = r;
                for (int i = 1; i < 2 * n; i++) p = (p * r) / scale;

                // H = 1 / (1 + ratio^(2n)), à l’échelle
                long H = (scale * scale) / (scale + p);

                // multiplication du pixel par H
                res[u][v] = (int)((long) img[u][v] * H / scale);
            }
        }

        return res;
    }
}
