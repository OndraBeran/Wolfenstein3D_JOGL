package model;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ModelLoop {
    private static MainModel model;
    private static CyclicBarrier barrier;

    public static void initLoop(MainModel model) {
        ModelLoop.model = model;
        ModelLoop.barrier = model.barrier;

        //prepare data for first frame
        model.prepareRenderData();
        model.writingToFirst.set(!model.writingToFirst.get());
    }

    public static void start() {
        Thread modelThread = new Thread(() -> {
            while (true) {
                model.update();

                model.prepareRenderData();
                model.prepareSoundData();
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        modelThread.start();
    }
}
