package model;

public class MainModel {
    public final Player player;
    private final int RESOLUTION;
    private final double FOV;

    private Soldier[] enemies = new Soldier[1];

    //temp
    int counter = 5;

    private int windowWidth;

    public MainModel(int res, double fov, String path) {
        Map.loadMap(path);
        player = new Player(53.5 * Map.getTILE_SIZE(), 62.5 * Map.getTILE_SIZE(), 90, fov);

        enemies[0] = new Soldier(44.5 * Map.getTILE_SIZE(), 55.5 * Map.getTILE_SIZE(), 7.5 * Map.getTILE_SIZE(), 2.5 * Map.getTILE_SIZE());

        RESOLUTION = res;
        FOV = fov;
    }

    public void update(int[] keyEvents){
        player.setAngle(player.getAngle() + (keyEvents[0] * player.getAngleVelocity()));

        player.getGun().update();

        Point dir = new Point(player.getxCoor() - enemies[0].getX(), player.getyCoor() - enemies[0].getY());

        Point normDir = Point.normalizeVector(dir);

        /*enemies[0].setX(enemies[0].getX() + normDir.getX());
        enemies[0].setY(enemies[0].getY() + normDir.getY());*/
        enemies[0].update(player.getStandardDirVector(), player.getxCoor(), player.getyCoor());

        double newX = player.getxCoor() + keyEvents[1] * (Math.cos(Math.toRadians(player.getAngle())) * player.getVelocity());
        double newY = player.getyCoor() - keyEvents[1] * (Math.sin(Math.toRadians(player.getAngle())) * player.getVelocity());

        if (!Map.isWall(newX, newY)){
            player.setxCoor(newX);
            player.setyCoor(newY);
        }
    }

    public double[][] renderEnemies(){
        double[][] result = new double[enemies.length][3];

        for (int i = 0; i < enemies.length; i++) {
            result[i] = renderEnemy(enemies[i]);
        }

        return result;
    }

    private double[] renderEnemy(Soldier soldier){
        double[] result = new double[4];

        //distance
        result[0] = player.distToEnemy(soldier);

        //position on screen
        double angleToPlayer = player.angleToEnemy(soldier);

        Point enemyVector = new Point(soldier.getX() - player.getxCoor(), -(soldier.getY() - player.getyCoor()));

        Point[] vecs = player.getFOVVectors();

        double angle1 = Point.angle(vecs[0], enemyVector);
        double angle2 = Point.angle(vecs[1], enemyVector);

        if (angle1 + angle2 > 57 && angle1 + angle2 < 63){
            result[1] = (FOV - angle1) / FOV;
        } else {
            result[1] = 2;
        }

        result[2] = soldier.getCurrentSpriteStage();

        result[3] = soldier.getOrientatedSpriteIndex();

        return result;
    }

    public double[][] castRays(){
        double startAngle = player.getAngle() - (FOV / 2);
        //must be -1 to account for starting at 0
        double increment = FOV / (RESOLUTION - 1);

        //double[] result = new double[RESOLUTION];
        double[][] result = new double[RESOLUTION][4];

        for (int i = 0; i < RESOLUTION; i++) {
            double[] oneRay = castOneRay(startAngle);
            double angleFromPlayer = Math.abs(startAngle - player.getAngle());

            oneRay[0] *= Math.cos(Math.toRadians(angleFromPlayer));

            result[i] = oneRay;
            startAngle += increment;
        }

        return result;
    }

    private double[] castOneRay(double angle){
        Ray ray = new Ray(player.getxCoor(), player.getyCoor(), angle);

        double[] x = xLineIntersectDist(ray);
        double[] y = yLineIntersectDist(ray);

        return x[0] < y[0] ? x : y;
    }

    /** @noinspection IntegerDivisionInFloatingPointContext*/
    private Point firsIntersectY(Ray ray){
        double x, y;
        if (ray.getAngle() < 90 || ray.getAngle() >= 270){
            x = (
                    //get next tile
                    (((int)ray.getxCoor() / Map.getTILE_SIZE()) + 1)
                    //get coordinate from tile
                    * Map.getTILE_SIZE()
                );
            //distance to next y line of grid
            double distFromY = Map.getTILE_SIZE() - (ray.getxCoor() % Map.getTILE_SIZE());
            //calculates the distance to next x line, positive for rays going up
            double distFromX = Math.tan(Math.toRadians(ray.getAngle())) * distFromY;

            y = ray.getyCoor() - distFromX;
        }
        else {  //beginning of tile
            x = ((int)ray.getxCoor() / Map.getTILE_SIZE()) * Map.getTILE_SIZE();

            double distFromY = ray.getxCoor() - x;
            double distFromX = Math.tan(Math.toRadians(ray.getAngle())) * distFromY;

            y = ray.getyCoor() + distFromX;
        }

        return new Point(x, y);
    }

    /** @noinspection IntegerDivisionInFloatingPointContext*/
    private Point firstIntersectX(Ray ray){
        double x, y;
        if (ray.getAngle() < 180){
            y = (
                    ((int)ray.getyCoor() / Map.getTILE_SIZE())
                    * Map.getTILE_SIZE()
                    );

            double distFromX = ray.getyCoor() - y;
            double distFromY = distFromX / Math.tan(Math.toRadians(ray.getAngle()));

            x = ray.getxCoor() + distFromY;
        }
        else {
            y = (
                    (((int)ray.getyCoor() / Map.getTILE_SIZE()) + 1)
                    * Map.getTILE_SIZE()
                    );

            double distFromX = y - ray.getyCoor();
            double distFromY = distFromX / Math.tan(Math.toRadians(ray.getAngle()));

            x = ray.getxCoor() - distFromY;
        }
        return new Point(x, y);
    }

    private double[] xLineIntersectDist(Ray ray){
        Point firstIntersectX = firstIntersectX(ray);

        double deltaX = xIntersectDeltaX(ray.getAngle());
        double deltaY = xIntersectDeltaY(ray.getAngle());

        Point nextIntersectX = new Point(firstIntersectX.getX(), firstIntersectX.getY());

        while (Map.inBounds(nextIntersectX)){
            //step into cell
            Point temp;
            double tempY;

            if (ray.getAngle() < 180){
                tempY = nextIntersectX.getY() - 1;
            } else {
                tempY = nextIntersectX.getY() + 1;
            }

            temp = new Point(nextIntersectX.getX(), tempY);

            //check for walls
            if (Map.isWall(temp)){
                return new double[]{Point.distance(nextIntersectX, new Point(ray.getxCoor(), ray.getyCoor())), 0, Map.coordInTile(nextIntersectX.getX())};
            } else {
                double newX = nextIntersectX.getX() + deltaX;
                double newY = nextIntersectX.getY() + deltaY;
                nextIntersectX = new Point(newX, newY);
            }
        }

        return new double[]{Double.MAX_VALUE, 0, 0, 0};
    }

    private double[] yLineIntersectDist(Ray ray){
        Point firstIntersect = firsIntersectY(ray);

        double deltaX = yIntersectDeltaX(ray.getAngle());
        double deltaY = yIntersectDeltaY(ray.getAngle());

        Point nextIntersect = new Point(firstIntersect.getX(), firstIntersect.getY());

        while (Map.inBounds(nextIntersect)){
            Point temp;
            double tempX;
            if (ray.getAngle() > 90 && ray.getAngle() <= 270){
                tempX = nextIntersect.getX() - 1;
            } else {
                tempX = nextIntersect.getX() + 1;
            }

            temp = new Point(tempX, nextIntersect.getY());

            if (Map.isWall(temp)){
                return new double[]{Point.distance(nextIntersect, new Point(ray.getxCoor(), ray.getyCoor())), 1, Map.coordInTile(nextIntersect.getY())};
            } else {
                double newX = nextIntersect.getX() + deltaX;
                double newY = nextIntersect.getY() + deltaY;
                nextIntersect = new Point(newX, newY);
            }

        }

        return new double[]{Double.MAX_VALUE, 0, 0, 0};
    }

    private double xIntersectDeltaX(double angle){
        if(angle <= 90) {
            //deltaX must be positive
            return Math.pow(Math.tan(Math.toRadians(angle)), -1) * Map.getTILE_SIZE();
        } else if (angle > 90 && angle <= 180){
            return Math.pow(Math.tan(Math.toRadians(180 - angle)), -1) * Map.getTILE_SIZE() * -1;
        } else if (angle > 180 && angle <= 270){
            return Math.pow(Math.tan(Math.toRadians(angle - 180)), -1) * Map.getTILE_SIZE() * -1;
        } else {
            return Math.pow(Math.tan(Math.toRadians(360 - angle)), -1) * Map.getTILE_SIZE();
        }
    }

    private double xIntersectDeltaY(double angle){
        if(angle <= 90) {
            //deltaX must be positive
            return -1 * Map.getTILE_SIZE();
        } else if (angle > 90 && angle <= 180){
            return -1 * Map.getTILE_SIZE();
        } else if (angle > 180 && angle <= 270){
            return Map.getTILE_SIZE();
        } else {
            return Map.getTILE_SIZE();
        }
    }

   private double yIntersectDeltaX(double angle){
       if(angle <= 90) {
           return Map.getTILE_SIZE();
       } else if (angle > 90 && angle <= 180){
           return -1 * Map.getTILE_SIZE();
       } else if (angle > 180 && angle <= 270){
           return -1 * Map.getTILE_SIZE();
       } else {
           return Map.getTILE_SIZE();
       }
   }

   private double yIntersectDeltaY(double angle){
        if (angle <= 90){
            return Math.tan(Math.toRadians(angle)) * Map.getTILE_SIZE() * -1;
        } else if (angle > 90 && angle <= 180){
            return Math.tan(Math.toRadians(180 - angle)) * Map.getTILE_SIZE() * -1;
        } else if (angle > 180 && angle <= 270){
            return Math.tan(Math.toRadians(angle - 180)) * Map.getTILE_SIZE();
        } else {
            return Math.tan(Math.toRadians(360 - angle)) * Map.getTILE_SIZE();
        }
   }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public boolean[][] getMapWalls(){
        return Map.getWalls();
    }


    public Player getPlayer() {
        return player;
    }
}
