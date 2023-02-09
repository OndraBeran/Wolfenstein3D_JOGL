package controller;

import com.jogamp.opengl.util.FPSAnimator;
import model.MainModel;
import view.Renderer;

import java.util.Arrays;

public class Main {
    private final int SCREEN_WIDTH = 1920;
    private final double FOV = 60;

    private MainModel model;
    private Renderer renderer;

    public Main() {
        model = new MainModel(SCREEN_WIDTH, FOV, "/maps/map.png");
        renderer = new Renderer();
    }

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();

        m.renderer.init(m.SCREEN_WIDTH, m.model);

        FPSAnimator animator = new FPSAnimator(m.renderer.getWindow(), 60, true);
        animator.start();
    }
}
