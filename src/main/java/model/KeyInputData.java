package model;

public class KeyInputData {
    private static int movement = 0;
    private static int rotation = 0;
    private static boolean shooting = false;
    private static boolean debugging = false;

    public static synchronized int getMovement() {
        return movement;
    }

    public static synchronized void setMovement(int movement) {
        KeyInputData.movement = movement;
    }

    public static synchronized int getRotation() {
        return rotation;
    }

    public static synchronized void setRotation(int rotation) {
        KeyInputData.rotation = rotation;
    }

    public static synchronized boolean isShooting() {
        return shooting;
    }

    public static synchronized void setShooting(boolean shooting) {
        KeyInputData.shooting = shooting;
    }

    public static synchronized boolean isDebugging() {
        return debugging;
    }

    public static synchronized void setDebugging(boolean debugging) {
        KeyInputData.debugging = debugging;
    }
}

