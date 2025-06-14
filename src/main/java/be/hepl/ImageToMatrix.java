package be.hepl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageToMatrix {

    public static int[][] convertImageToMatrix(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return convertImageToMatrix(image);
    }

    // Méthode qui convertit une BufferedImage en matrice de pixels ARGB
    public static int[][] convertImageToMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] pixels = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = image.getRGB(x, y);
            }
        }
        return pixels;
    }
}
