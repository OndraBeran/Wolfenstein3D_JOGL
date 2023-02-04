package model;

import java.util.Arrays;

public class Map {
    private final int NUMBER_OF_TILES = 10;
    private final int TILE_SIZE = 128;

    private final boolean[][] walls = {
            {true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, true, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, true, true},
            {true, true, false, false, false, false, false, false, true, true},
            {true, true, false, false, false, false, false, false, true, true},
            {true, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, true, false, false, false, true, true},
            {true, true, true, true, true, true, true, true, true, true}
    };

    public int getNUMBER_OF_TILES() {
        return NUMBER_OF_TILES;
    }

    public int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public boolean[][] getWalls() {
        return walls;
    }

    public boolean inBounds(Point point){
        if (point.getX() < 0 || point.getX() > (TILE_SIZE * NUMBER_OF_TILES - 1)) return false;
        if (point.getY() < 0 || point.getY() > (TILE_SIZE * NUMBER_OF_TILES - 1)) return false;
        return true;
    }
    
    public boolean isWall(Point point){
        int xIndex = coordToTile(point.getX());
        int yIndex = coordToTile(point.getY());

        return walls[yIndex][xIndex];
    }

    public boolean isWall(double x, double y){
        return walls[coordToTile(y)] [coordToTile(x)];
    }
    
    private int coordToTile(double coord){
        return (int) coord / TILE_SIZE;
    }

    public double coordInTile(double coord){
        return coord % TILE_SIZE;
    }
}
