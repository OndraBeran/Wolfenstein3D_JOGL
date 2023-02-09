package model;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double distance(Point a, Point b){
        double x = a.getX() - b.getX();
        double y = a.getY() - b.getY();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static double distance(double x1, double y1, double x2, double y2){
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * @param vector vector must be in standard coordinate system (y coord multiplied by -1)
     * @return returns the angle relative to the x-axis
     */
    public static double angleToXAxis(Point vector) {
        double basicAngle = Math.toDegrees(Math.atan(vector.getY() / vector.getX()));

        //1. quadrant
        if (vector.getX() >= 0 && vector.getY() >= 0){
            return basicAngle;
        }

        //2. quadrant
        if (vector.getX() <= 0 && vector.getY() >= 0){
            return basicAngle + 180;
        }

        //3. quadrant
        if (vector.getX() < 0 && vector.getY() < 0){
            return basicAngle + 180;
        }

        //4. quadrant
        return basicAngle + 360;

    }

    public static double angle(Point vec1, Point vec2){

        double dotProduct = vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
        double angleCos = dotProduct / (vec1.vectorMagnitude() * vec2.vectorMagnitude());

        return Math.toDegrees(Math.acos(angleCos));
    }

    public static Point normalizeVector(Point vec){
        double magnitude = vec.vectorMagnitude();

        return new Point(vec.getX() / magnitude, vec.getY() / magnitude);
    }

    public static Point pointsToVector(Point a, Point b){
        return new Point(b.getX() - a.getX(), b.getY() - a.getY());
    }

    public double vectorMagnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public static Point normalVectFromAngle(double angle){
        angle %= 360;

        angle = Math.toRadians(angle);

        return new Point(Math.cos(angle), Math.sin(angle));
    }

    @Override
    public String toString(){
        return "X: " + getX() + ", Y: " + getY();
    }
}
