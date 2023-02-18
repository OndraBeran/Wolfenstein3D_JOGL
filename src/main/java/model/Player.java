package model;

public class Player {

    private final double FOV;

    private double xCoor;
    private double yCoor;
    private double angle;
    private final double velocity = 16;
    private double angleVelocity = 2;

    private long lastUpdate = 0;

    private Gun gun;

    public double getxCoor() {
        return xCoor;
    }

    public void setxCoor(double xCoor) {
        this.xCoor = xCoor;
    }

    public double getyCoor() {
        return yCoor;
    }

    public void setyCoor(double yCoor) {
        this.yCoor = yCoor;
    }

    public double getAngle() {
        return angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngleVelocity() {
        return angleVelocity;
    }

    public void update(){
        long timeSinceUpdate = System.currentTimeMillis() - lastUpdate;

        double increment = timeSinceUpdate / (1000 / 60.0);

        double rotationSpeed = adjustedAngleVelocity(System.currentTimeMillis() - KeyInputData.getStartedTurning());

        setAngle(angle + (KeyInputData.getRotation() * rotationSpeed * increment));

        double newY = yCoor - KeyInputData.getMovement() * (Math.sin(Math.toRadians(angle)) * velocity * increment);
        double newX = xCoor + KeyInputData.getMovement() * (Math.cos(Math.toRadians(angle)) * velocity * increment);

        if (!Map.isWall(newX, newY)){
            xCoor = newX;
            yCoor = newY;
        }

        gun.update(angle);

        lastUpdate = System.currentTimeMillis();
    }

    public void setAngle(double angle) {
        if (angle < 0){
            this.angle = 360 + angle;
        } else if (angle >= 360){
            this.angle = angle % 360;
        } else {
            this.angle = angle;
        }
    }

    public Point getCoordinates(){
        return new Point(xCoor, yCoor);
    }

    public Gun getGun() {
        return gun;
    }

    public Point getDirVector(){
        return new Point(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
    }

    public Point getStandardDirVector(){
        return new Point(Math.cos(Math.toRadians(360 - angle)), Math.sin(Math.toRadians(360 - angle)));
    }

    public Point[] getFOVVectors(){
        Point[] vectors = new Point[2];

        vectors[0] = new Point(Math.cos(Math.toRadians(angle - (FOV / 2))), Math.sin(Math.toRadians(angle - (FOV / 2))));
        vectors[1] = new Point(Math.cos(Math.toRadians(angle + (FOV / 2))), Math.sin(Math.toRadians(angle + (FOV / 2))));

        return vectors;
    }

    public Player(double xCoor, double yCoor, double angle, double fov, Soldier[] enemies) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
        FOV = fov;

        lastUpdate = System.currentTimeMillis();

        gun = new Gun(enemies);

        RayCaster.setPlayer(this);
    }

    public double distToEnemy(Soldier soldier){
        double directDist = Point.distance(xCoor, yCoor, soldier.getX(), soldier.getY());

        return directDist;
    }

    public double angleToEnemy(Soldier soldier){
        Point playerVector = getDirVector();
        Point enemyVector = new Point(soldier.getX() - xCoor, -(soldier.getY() - yCoor));

        double dotProduct = playerVector.getX() * enemyVector.getX() + playerVector.getY() * enemyVector.getY();
        double angleCos = dotProduct / (playerVector.vectorMagnitude() * enemyVector.vectorMagnitude());

        return Math.toDegrees(Math.acos(angleCos));
    }

    private double adjustedAngleVelocity(long time){
        if (time < 100){
            return time / 100.0 * angleVelocity;
        } else {
            return angleVelocity;
        }
    }
}
