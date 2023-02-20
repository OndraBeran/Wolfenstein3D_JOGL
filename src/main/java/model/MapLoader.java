package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MapLoader {
    private static final int WALL_CODE = 16777216;
    private static final int SOLDIER_CODE = 16776961;
    private static final int START_CODE = 65536;
    private static final int END_CODE = 16711936;

    public static void load(String path, MainModel model){
        URL url = MapLoader.class.getResource(path);

        try {
            BufferedImage mapImg = ImageIO.read(url);
            boolean[][] map = new boolean[mapImg.getHeight()][mapImg.getWidth()];
            Player player = null;
            ArrayList<Soldier> enemyList = new ArrayList<>();

            for (int y = 0; y < mapImg.getHeight(); y++) {
                for (int x = 0; x < mapImg.getWidth(); x++) {
                    map[y][x] = Math.abs(mapImg.getRGB(x, y)) == WALL_CODE;
                    if (Math.abs(mapImg.getRGB(x, y)) == START_CODE){
                        player = new Player(x * (Map.getTILE_SIZE() + 0.5), y * (Map.getTILE_SIZE() + 0.5), 90, 60);
                    } else if (Math.abs(mapImg.getRGB(x, y)) == SOLDIER_CODE){
                        enemyList.add(new Soldier(x * (Map.getTILE_SIZE() + 0.5), y * (Map.getTILE_SIZE() + 0.5)));
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
