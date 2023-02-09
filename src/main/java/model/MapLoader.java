package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapLoader {
    public static boolean[][] load(String path){
        URL url = MapLoader.class.getResource(path);

        try {
            BufferedImage mapImg = ImageIO.read(url);
            boolean[][] map = new boolean[mapImg.getHeight()][mapImg.getWidth()];

            for (int y = 0; y < mapImg.getHeight(); y++) {
                for (int x = 0; x < mapImg.getWidth(); x++) {
                    map[y][x] = mapImg.getRGB(x, y) != 0;
                }
            }

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
