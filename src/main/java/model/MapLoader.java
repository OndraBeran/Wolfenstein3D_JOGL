package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MapLoader {
    private static final int WALL_CODE = -16777216;
    private static final int SOLDIER_CODE = -16776961;
    private static final int START_CODE = -65536;
    private static final int END_CODE = -16711936;

    public static void load(String path, MainModel model){
        URL url = MapLoader.class.getResource(path);

        try {
            BufferedImage mapImg = ImageIO.read(url);
            boolean[][] map = new boolean[mapImg.getHeight()][mapImg.getWidth()];
            Player player = null;
            ArrayList<Soldier> enemyList = new ArrayList<>();

            for (int y = 0; y < mapImg.getHeight(); y++) {
                for (int x = 0; x < mapImg.getWidth(); x++) {
                    int rgbValue = mapImg.getRGB(x, y);

                    map[y][x] = rgbValue == WALL_CODE;

                    switch (rgbValue){
                        case START_CODE -> player = new Player((x + 0.5) * Map.getTILE_SIZE(), (y + 0.5) * Map.getTILE_SIZE(), 90, 60);
                        case SOLDIER_CODE -> enemyList.add(new Soldier((x + 0.5) * Map.getTILE_SIZE(), (y + 0.5) * Map.getTILE_SIZE()));
                        case END_CODE -> model.setFinishTile(new int[]{x, y});
                    }
                }
            }

            for (Soldier soldier :
                    enemyList) {
                soldier.setPlayer(player);
            }

            player.setEnemies(enemyList);

            Map.setWalls(map);
            model.setPlayer(player);
            model.setEnemies(enemyList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
