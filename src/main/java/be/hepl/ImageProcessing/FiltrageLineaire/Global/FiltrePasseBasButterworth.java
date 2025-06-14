package be.hepl.ImageProcessing.FiltrageLineaire.Global;

public class FiltrePasseBasButterworth {

    public static int[][] butterworthLowPassFilter(int[][] image, int D0, int n) {
        int h = image.length, w = image[0].length;
        int[][] res = new int[h][w];
        int scale = 10000; // facteur d'échelle pour simuler les réels

        for (int u = 0; u < h; u++) {
            int du = u - h / 2; // distance verticale au centre
            for (int v = 0; v < w; v++) {
                int dv = v - w / 2;
                int D2 = du * du + dv * dv; // distance au carré au centre
                if (D2 == 0) D2 = 1; // éviter division par zéro

                // ratio = (D / D0)^2 à l'échelle
                long r = ((long) D2 * scale) / (D0 * D0);

                // ratio^(2n)
                long p = r;
                for (int i = 1; i < 2 * n; i++) p = (p * r) / scale;

                // H = 1 / (1 + ratio^(2n)), à l’échelle
                long H = (scale * scale) / (scale + p);

                // pixel filtré = pixel * H (puis divisé par l’échelle)
                res[u][v] = (int)((long) image[u][v] * H / scale);
            }
        }

        return res;
    }
}
