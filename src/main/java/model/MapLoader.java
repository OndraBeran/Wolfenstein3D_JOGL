package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final int ARMOR = -32768;
    private static final int BARREL = -8355840;
    private static final int LAMP = -8388480;
    private static final int TABLE = -4194112;
    private static final int WELL = -8323200;

    public static void load(String path, MainModel model){
        URL url = MapLoader.class.getResource(path);

        try {
            BufferedImage mapImg = ImageIO.read(url);
            int[][] map = new int[mapImg.getHeight()][mapImg.getWidth()];
            Player player = null;
            ArrayList<Soldier> enemyList = new ArrayList<>();
            List<SpriteObject> spriteObjects = new ArrayList<>();

            for (int y = 0; y < mapImg.getHeight(); y++) {
                for (int x = 0; x < mapImg.getWidth(); x++) {
                    int rgbValue = mapImg.getRGB(x, y);

                    if (y == 0 && x < 7) System.out.println(rgbValue);

                    map[y][x] = materialIndex(rgbValue);

                    switch (rgbValue){
                        case START_CODE -> player = new Player((x + 0.5) * Map.getTILE_SIZE(), (y + 0.5) * Map.getTILE_SIZE(), 90, 60);
                        case SOLDIER_CODE -> enemyList.add(new Soldier((x + 0.5) * Map.getTILE_SIZE(), (y + 0.5) * Map.getTILE_SIZE()));
                        case END_CODE -> model.setFinishTile(new int[]{x, y});

                        case LAMP -> spriteObjects.add(createSpriteObject(x, y, rgbValue));
                        case ARMOR, BARREL, TABLE, WELL -> {
                            spriteObjects.add(createSpriteObject(x, y, rgbValue));
                            map[y][x] = -1;
                        }
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
            model.setSpriteObjects(spriteObjects);
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

    private static SpriteObject createSpriteObject(int x, int y, int index){
        Point tileCenter = Map.centerOfTile(x, y);

        double xCoor = tileCenter.getX();
        double yCoor = tileCenter.getY();

        int spriteIndex = switch (index){
            case ARMOR: yield 0;
            case BARREL: yield 1;
            case LAMP: yield 2;
            case TABLE: yield 3;
            case WELL: yield 4;
            default: yield -1;
        } + 10;

        return new SpriteObject(xCoor, yCoor, spriteIndex);
    }
}
