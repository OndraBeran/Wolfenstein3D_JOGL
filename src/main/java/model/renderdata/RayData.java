package model.renderdata;

public record RayData(double length, boolean intersectsXAxis, double intersectCordInTile, int textureIndex) {
    public final static RayData outOfBounds = new RayData(Double.MAX_VALUE, true, 0, 0);
}
