package controller;

import model.MainModel;
import view.Renderer;

public class GameLoop {
    private static boolean running = false;

    private static int updates = 0;
    private static final int MAX_UPDATES = 1;

    private static long lastUpdateTime = 0;

    private static int targetFPS = 30;
    private static int targetTime = 1000000000 / targetFPS;


    public static void start(MainModel model, Renderer renderer){
        Thread t = new Thread(() -> {

            running = true;

            lastUpdateTime = System.nanoTime();

            int fps = 0;
            long lastFpsCheck = System.nanoTime();

            while (running) {
                long currentTime = System.nanoTime();

                updates = 0;
                double[][] rayResult = null;
                while (currentTime - lastUpdateTime >= targetTime) {
                    //update model
                    model.update(renderer.getKeyListener().getKeyEvents());
                    rayResult = model.castRays();

                    lastUpdateTime += targetTime;
                    updates++;

                    if (updates > MAX_UPDATES) {
                        break;
                    }
                }
                // render
                renderer.getwListener().setRayResult(rayResult);
                renderer.getWindow().display();
					/*
					fps++;
					if (System.nanoTime() >= lastFpsCheck + 1000000000) {
						System.out.println(fps);
						fps = 0;
						lastFpsCheck = System.nanoTime();
					}
					*/

                long timeTaken = System.nanoTime() - currentTime;
                if (targetTime > timeTaken) {
                    try {
                        Thread.sleep((targetTime - timeTaken) / 1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }
}
