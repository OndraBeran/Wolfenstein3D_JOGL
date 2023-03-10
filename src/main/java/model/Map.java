package model;

public class Map {
    static int temCounter = 0;

    private static final int NUMBER_OF_TILES = 64;
    private static final int TILE_SIZE = 128;

    private static int[][] walls;

    public static int getNUMBER_OF_TILES() {
        return NUMBER_OF_TILES;
    }

    public static int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public static int[][] getWalls() {
        return walls;
    }

    public static boolean inBounds(Point point) {
        if (point.x() < 0 || point.x() > (TILE_SIZE * NUMBER_OF_TILES - 1)) return false;
        return !(point.y() < 0) && !(point.y() > (TILE_SIZE * NUMBER_OF_TILES - 1));
    }

    public static boolean isWall(Point point) {
        int xIndex = coordToTile(point.x());
        int yIndex = coordToTile(point.y());

        int tile = walls[yIndex][xIndex];

        return tile != 0 && tile != -1;
    }

    public static boolean isWall(double x, double y) {
        int tile = walls[coordToTile(y)][coordToTile(x)];

        return tile != 0 && tile != -1;
    }

    public static boolean isWall(int x, int y) {
        int tile = walls[y][x];

        return tile != 0 && tile != -1;
    }

    public static boolean isWalkable(Point point) {
        int xIndex = coordToTile(point.x());
        int yIndex = coordToTile(point.y());

        int tile = walls[yIndex][xIndex];

        return tile == 0;
    }

    public static boolean isWalkable(double x, double y) {
        int tile = walls[coordToTile(y)][coordToTile(x)];

        return tile == 0;
    }

    public static boolean isWalkable(int x, int y) {
        int tile = walls[y][x];

        return tile == 0;
    }

    public static int coordToTile(double coord) {
        return (int) coord / TILE_SIZE;
    }

    public static double coordInTile(double coord) {
        return coord % TILE_SIZE;
    }

    public static Point centerOfTile(int x, int y) {
        return new Point(x * TILE_SIZE + (TILE_SIZE / 2), y * TILE_SIZE + (TILE_SIZE / 2));
    }

    public static void setWalls(int[][] walls) {
        Map.walls = walls;
    }

    public static int getTextureIndex(Point point) {
        int xIndex = coordToTile(point.x());
        int yIndex = coordToTile(point.y());

        if (walls[yIndex][xIndex] - 1 < 0 && temCounter == 0) {
            System.out.println(xIndex + " " + yIndex);
            System.out.println(point);
            temCounter++;
        }

        return walls[yIndex][xIndex] - 1;
    }
}
