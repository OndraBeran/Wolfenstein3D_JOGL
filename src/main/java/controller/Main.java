package controller;

import com.jogamp.opengl.util.FPSAnimator;
import model.MainModel;
import view.Renderer;

import java.util.concurrent.CyclicBarrier;

public class Main {
    private final int SCREEN_WIDTH = 1920;
    private final double FOV = 60;

    private MainModel model;
    private final Renderer renderer;
    public CyclicBarrier barrier;

    public Main() {
        System.setProperty("newt.window.icons", "icon_48px.png icon_48px.png");
        barrier = new CyclicBarrier(3, () -> {
            model.writingToFirst.set(!model.writingToFirst.get());
        });

        model = new MainModel(SCREEN_WIDTH, FOV, new String[]{"/maps/map_objects.png"}, barrier);
        renderer = new Renderer();
    }

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();

        m.renderer.init(m.SCREEN_WIDTH, m.model, m.barrier);

        FPSAnimator animator = new FPSAnimator(m.renderer.getWindow(), 60, true);
        animator.start();
    }
}
