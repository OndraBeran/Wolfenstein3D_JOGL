package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class MapLoader {
    //wall materials
    private static final int BSTONE = -16777088;
    private static final int BSTONECELL = -16744320;
    private static final int GSTONE = -8355712;
    private static final int GSTONESLIME = -4144960;
    private static final int WOOD = -8388608;
    private static final int DOOR = -65281;

    private static final int SOLDIER_CODE = -16776961;
    private static final int START_CODE = -65536;
    private static final int END_CODE = -16711936;

    public static void load(String path, MainModel model){
        URL url = MapLoader.class.getResource(path);

        try {
            BufferedImage mapImg = ImageIO.read(url);
            int[][] map = new int[mapImg.getHeight()][mapImg.getWidth()];
            Player player = null;
            ArrayList<Soldier> enemyList = new ArrayList<>();

            for (int y = 0; y < mapImg.getHeight(); y++) {
                for (int x = 0; x < mapImg.getWidth(); x++) {
                    int rgbValue = mapImg.getRGB(x, y);

                    map[y][x] = materialIndex(rgbValue);

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

    private static int materialIndex(int materialCode){
        switch (materialCode){
            case BSTONE -> {
                return 1;
            }
            case BSTONECELL -> {return 2;}
            case GSTONE -> {return 3;}
            case GSTONESLIME -> {return 4;}
            case WOOD -> {return 5;}
            case DOOR -> {return 6;}
        }

        return 0;
    }
}
