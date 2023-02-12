package controller;

import com.jogamp.opengl.util.FPSAnimator;
import model.MainModel;
import view.Renderer;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private final int SCREEN_WIDTH = 1920;
    private final double FOV = 60;

    private MainModel model;
    private Renderer renderer;
    private CyclicBarrier barrier;

    public Main() {
        System.setProperty("newt.window.icons", "icon_48px.png icon_48px.png");
        barrier = new CyclicBarrier(2, () -> {
            model.writingToFirst.set(!model.writingToFirst.get());
        });

        model = new MainModel(SCREEN_WIDTH, FOV, "/maps/map.png", barrier);
        renderer = new Renderer();
    }

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();

        m.renderer.init(m.SCREEN_WIDTH, m.model, m.barrier);

        FPSAnimator animator = new FPSAnimator(m.renderer.getWindow(), 60, true);
        animator.start();
    }
}
