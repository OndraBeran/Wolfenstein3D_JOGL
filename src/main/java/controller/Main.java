package controller;

import model.MainModel;
import view.Renderer;

import java.util.Arrays;

public class Main {
    private final int SCREEN_WIDTH = 1920;
    private final double FOV = 45;

    private MainModel model;
    private Renderer renderer;

    public Main() {
        model = new MainModel(SCREEN_WIDTH, FOV);
        renderer = new Renderer();
    }

    public static void main(String[] args) {
        Main m = new Main();
        System.out.println(Arrays.toString(m.model.castRays()));
        m.renderer.init(m.SCREEN_WIDTH);
        m.renderer.getListener().setRayResult(m.model.castRays());
    }
}
