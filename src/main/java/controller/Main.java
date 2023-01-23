package controller;

import model.MainModel;
import view.Renderer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {
    private final int SCREEN_WIDTH = 1080;
    private final double FOV = 90;

    private MainModel model;
    private Renderer renderer;

    public Main() {
        model = new MainModel(SCREEN_WIDTH, FOV);
        renderer = new Renderer();
    }

    public static void main(String[] args) {
        Main m = new Main();

        List l = Arrays.asList(ArrayUtils);

        System.out.println(Collections.min(l) + " " + Collections.max(l));
    }
}
