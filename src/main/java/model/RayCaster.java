package model;

import model.renderdata.RayData;

public class RayCaster {

    private static Player player;

    public static double castRay(Point start, double angle) {
        Ray ray = new Ray(start, angle);

        Point xIntersect = xLineWallIntersect(ray);
        Point yIntersect = yLineWallIntersect(ray);

        double xDist = Point.distance(start, xIntersect);
        double yDist = Point.distance(start, yIntersect);

        return Double.min(xDist, yDist);
    }

    public static RayData castRenderRay(double angle) {
        Ray ray = new Ray(player.getCoordinates(), angle);

        Point xIntersect = xLineWallIntersect(ray);
        Point yIntersect = yLineWallIntersect(ray);

        double xDist = Point.distance(player.getCoordinates(), xIntersect);
        double yDist = Point.distance(player.getCoordinates(), yIntersect);

        double angleToPlayer = Math.abs(player.getAngle() - angle);

        if (xDist < yDist) {
            return new RayData(removeFisheye(xDist, angleToPlayer), true, Map.coordInTile(xIntersect.x()), Map.getTextureIndex(xIntersect));
        } else {
            return new RayData(removeFisheye(yDist, angleToPlayer), false, Map.coordInTile(yIntersect.y()), Map.getTextureIndex(yIntersect));
        }
    }

    /**
     * @noinspection IntegerDivisionInFloatingPointContext
     */
    private static Point firsIntersectY(Ray ray) {
        double x, y;
        if (ray.getAngle() < 90 || ray.getAngle() >= 270) {
            x = (
                    //get next tile
                    (((int) ray.getxCoor() / Map.getTILE_SIZE()) + 1)
                            //get coordinate from tile
                            * Map.getTILE_SIZE()
            );
            //distance to next y line of grid
            double distFromY = Map.getTILE_SIZE() - (ray.getxCoor() % Map.getTILE_SIZE());
            //calculates the distance to next x line, positive for rays going up
            double distFromX = Math.tan(Math.toRadians(ray.getAngle())) * distFromY;

            y = ray.getyCoor() - distFromX;
        } else {  //beginning of tile
            x = ((int) ray.getxCoor() / Map.getTILE_SIZE()) * Map.getTILE_SIZE();

            double distFromY = ray.getxCoor() - x;
            double distFromX = Math.tan(Math.toRadians(ray.getAngle())) * distFromY;

            y = ray.getyCoor() + distFromX;
        }

        return new Point(x, y);
    }

    /**
     * @noinspection IntegerDivisionInFloatingPointContext
     */
    private static Point firstIntersectX(Ray ray) {
        double x, y;
        if (ray.getAngle() < 180) {
            y = (
                    ((int) ray.getyCoor() / Map.getTILE_SIZE())
                            * Map.getTILE_SIZE()
            );

            double distFromX = ray.getyCoor() - y;
            double distFromY = distFromX / Math.tan(Math.toRadians(ray.getAngle()));

            x = ray.getxCoor() + distFromY;
        } else {
            y = (
                    (((int) ray.getyCoor() / Map.getTILE_SIZE()) + 1)
                            * Map.getTILE_SIZE()
            );

            double distFromX = y - ray.getyCoor();
            double distFromY = distFromX / Math.tan(Math.toRadians(ray.getAngle()));

            x = ray.getxCoor() - distFromY;
        }
        return new Point(x, y);
    }

    private static Point xLineWallIntersect(Ray ray) {
        Point firstIntersectX = firstIntersectX(ray);

        double deltaX = xIntersectDeltaX(ray.getAngle());
        double deltaY = xIntersectDeltaY(ray.getAngle());

        Point nextIntersectX = new Point(firstIntersectX.x(), firstIntersectX.y());

        while (Map.inBounds(nextIntersectX)) {
            //step into cell
            Point temp;
            double tempY;

            if (ray.getAngle() < 180) {
                tempY = nextIntersectX.y() - 1;
            } else {
                tempY = nextIntersectX.y() + 1;
            }

            temp = new Point(nextIntersectX.x(), tempY);

            //check for walls
            if (Map.isWall(temp)) {
                return temp;
            } else {
                double newX = nextIntersectX.x() + deltaX;
                double newY = nextIntersectX.y() + deltaY;
                nextIntersectX = new Point(newX, newY);
            }
        }

        return Point.outOfBounds;
    }

    private static Point yLineWallIntersect(Ray ray) {
        Point firstIntersect = firsIntersectY(ray);

        double deltaX = yIntersectDeltaX(ray.getAngle());
        double deltaY = yIntersectDeltaY(ray.getAngle());

        Point nextIntersect = new Point(firstIntersect.x(), firstIntersect.y());

        while (Map.inBounds(nextIntersect)) {
            Point temp;
            double tempX;
            if (ray.getAngle() > 90 && ray.getAngle() <= 270) {
                tempX = nextIntersect.x() - 1;
            } else {
                tempX = nextIntersect.x() + 1;
            }

            temp = new Point(tempX, nextIntersect.y());

            if (Map.isWall(temp)) {
                return temp;
            } else {
                double newX = nextIntersect.x() + deltaX;
                double newY = nextIntersect.y() + deltaY;
                nextIntersect = new Point(newX, newY);
            }

        }

        return Point.outOfBounds;
    }

    private static double xIntersectDeltaX(double angle) {
        if (angle <= 90) {
            //deltaX must be positive
            return Math.pow(Math.tan(Math.toRadians(angle)), -1) * Map.getTILE_SIZE();
        } else if (angle > 90 && angle <= 180) {
            return Math.pow(Math.tan(Math.toRadians(180 - angle)), -1) * Map.getTILE_SIZE() * -1;
        } else if (angle > 180 && angle <= 270) {
            return Math.pow(Math.tan(Math.toRadians(angle - 180)), -1) * Map.getTILE_SIZE() * -1;
        } else {
            return Math.pow(Math.tan(Math.toRadians(360 - angle)), -1) * Map.getTILE_SIZE();
        }
    }

    /**
     * @noinspection DuplicatedCode
     */
    private static double xIntersectDeltaY(double angle) {
        if (angle <= 90) {
            //deltaX must be positive
            return -1 * Map.getTILE_SIZE();
        } else if (angle > 90 && angle <= 180) {
            return -1 * Map.getTILE_SIZE();
        } else if (angle > 180 && angle <= 270) {
            return Map.getTILE_SIZE();
        } else {
            return Map.getTILE_SIZE();
        }
    }

    /**
     * @noinspection DuplicatedCode
     */
    private static double yIntersectDeltaX(double angle) {
        if (angle <= 90) {
            return Map.getTILE_SIZE();
        } else if (angle > 90 && angle <= 180) {
            return -1 * Map.getTILE_SIZE();
        } else if (angle > 180 && angle <= 270) {
            return -1 * Map.getTILE_SIZE();
        } else {
            return Map.getTILE_SIZE();
        }
    }

    private static double yIntersectDeltaY(double angle) {
        if (angle <= 90) {
            return Math.tan(Math.toRadians(angle)) * Map.getTILE_SIZE() * -1;
        } else if (angle > 90 && angle <= 180) {
            return Math.tan(Math.toRadians(180 - angle)) * Map.getTILE_SIZE() * -1;
        } else if (angle > 180 && angle <= 270) {
            return Math.tan(Math.toRadians(angle - 180)) * Map.getTILE_SIZE();
        } else {
            return Math.tan(Math.toRadians(360 - angle)) * Map.getTILE_SIZE();
        }
    }

    private static double removeFisheye(double length, double angle) {
        return Math.cos(Math.toRadians(angle)) * length;
    }

    public static void setPlayer(Player player) {
        RayCaster.player = player;
    }
}
