package controller;

import model.MainModel;
import view.Renderer;

import java.util.Arrays;

public class Main {
    private final int SCREEN_WIDTH = 1920;
    private final double FOV = 60;

    private MainModel model;
    private Renderer renderer;

    public Main() {
        model = new MainModel(SCREEN_WIDTH, FOV);
        renderer = new Renderer();
    }

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();
        m.renderer.init(m.SCREEN_WIDTH, m.model.getMap());

        GameLoop.start(m.model, m.renderer);
//        while (true){
//            m.model.player.setAngle(m.model.player.getAngle() + 0.5);
//            m.renderer.getListener().setRayResult(m.model.castRays());
//            Thread.sleep(150);
//        }
    }
}
