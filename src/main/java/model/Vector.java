package model;

public record Vector(double x, double y) {

    /**
     * @param vector vector must be in standard coordinate system (y coord multiplied by -1)
     * @return returns the angle relative to the x-axis
     */
    public static double angleToXAxis(Vector vector) {
        double basicAngle = Math.toDegrees(Math.atan(vector.y() / vector.x()));

        //1. quadrant
        if (vector.x() >= 0 && vector.y() >= 0) {
            return basicAngle;
        }

        //2. quadrant
        if (vector.x() <= 0 && vector.y() >= 0) {
            return basicAngle + 180;
        }

        //3. quadrant
        if (vector.x() < 0 && vector.y() < 0) {
            return basicAngle + 180;
        }

        //4. quadrant
        return basicAngle + 360;

    }

    public double vectorMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public static Vector normalVectFromAngle(double angle) {
        angle %= 360;

        angle = Math.toRadians(angle);

        return new Vector(Math.cos(angle), Math.sin(angle));
    }
}
