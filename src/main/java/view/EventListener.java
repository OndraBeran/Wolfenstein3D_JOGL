package view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import model.MainModel;
import model.ModelLoop;
import model.renderdata.RenderData;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class EventListener implements GLEventListener {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = 1080;
    private final double WALL_HEIGHT = 100;
    private final boolean DEBUG = false;
    private boolean MARK_MIDDLE = false;
    private MainModel model;

    private CyclicBarrier barrier;

    private ImageResource[] textures;
    private ImageResource[][] enemySprites;
    private ImageResource[] gunSprites;

    public EventListener(int SCREEN_WIDTH, MainModel model, int[] keyEvents, CyclicBarrier barrier) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.model = model;
        this.barrier = barrier;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (!DEBUG){
            gl.glOrtho(0, SCREEN_WIDTH - 1, -1, 1, 0, 1);
        } else {
            gl.glOrtho(0, 16 / 9.0, 1, 0, 0, 1);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);

        loadAssets();

        ModelLoop.initLoop(model);
        ModelLoop.start();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL2.GL_TEXTURE_2D);

        Graphics.clear(gl);

        //draw floor and ceiling
        Graphics.drawBackground(gl, 0, SCREEN_WIDTH - 1);

        RenderData data = model.writingToFirst.get() ? model.renderData2 : model.renderData1;

        //draw rays
        for (int i = 0; i < data.enemies().length + 1; i++) {

            for (int j = SCREEN_WIDTH - 1; j >= 0; j--) {
                double distToWall = data.rays()[j].length();
                double lastSpriteDist = i == 0 ? Double.MAX_VALUE : data.enemies()[i - 1].distance();
                double currentSpriteDist = i == data.enemies().length ? Double.MIN_VALUE : data.enemies()[i].distance();

                if (distToWall > currentSpriteDist && distToWall < lastSpriteDist){
                    double scale = scaleRay(distToWall);

                    ImageResource img = data.rays()[j].intersectsXAxis() ? textures[0] : textures[1];
                    boolean bright = data.rays()[j].intersectsXAxis();

                    Graphics.drawTexturedRay(gl, img, SCREEN_WIDTH - 1 - j, scale, data.rays()[j].intersectCordInTile(), bright);
                }
            }

            if (i != data.enemies().length){
                double scale = scaleRay(data.enemies()[i].distance());

                Graphics.drawSprite(gl, enemySprites[data.enemies()[i].spriteOrientation()][data.enemies()[i].movementStage()], data.enemies()[i].posInFOV() * SCREEN_WIDTH, scale * SCREEN_HEIGHT, scale);
            }
        }

        //draw gun
        Graphics.drawGun(gl, gunSprites[data.player().gunSprite()], SCREEN_WIDTH / 2, SCREEN_WIDTH / 4.0);

        //synchronize with model thread
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {


    }

    private double scaleRay(double rayLength){
        return 192 / rayLength;
    }

    private void loadAssets(){
        CountDownLatch latch = new CountDownLatch(3);

        loadTextures(latch);
        loadEnemies(latch);
        loadGuns(latch);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadTextures(CountDownLatch latch){
        Thread loadTexture = new Thread(() -> {
            textures = new ImageResource[2];

            textures[0] = new ImageResource("/textures/BSTONEA1.png");
            textures[1] = new ImageResource("/textures/BSTONEA2.png");

            latch.countDown();
        });

        loadTexture.start();
    }

    private void loadEnemies(CountDownLatch latch){
        Thread loadEnemies = new Thread(() -> {
            enemySprites = new ImageResource[8][];

            for (int i = 0; i < enemySprites.length; i++) {
                enemySprites[i] = new ImageResource[5];

                enemySprites[i][0] = new ImageResource("/gard_low_res/GARDA" + (i + 1) + ".png");
                enemySprites[i][1] = new ImageResource("/gard_low_res/GARDB" + (i + 1) + ".png");
                enemySprites[i][2] = new ImageResource("/gard_low_res/GARDC" + (i + 1) + ".png");
                enemySprites[i][3] = new ImageResource("/gard_low_res/GARDD" + (i + 1) + ".png");
                enemySprites[i][4] = new ImageResource("/gard_low_res/GARDE" + (i + 1) + ".png");
            }

            latch.countDown();
        });

        loadEnemies.start();
    }

    private void loadGuns(CountDownLatch latch){
        Thread loadGuns = new Thread(() -> {
            gunSprites = new ImageResource[3];

            gunSprites[0] = new ImageResource("/gun/V_LUGR_A.png");
            gunSprites[1] = new ImageResource("/gun/V_LUGR_B.png");
            gunSprites[2] = new ImageResource("/gun/V_LUGR_C.png");

            latch.countDown();
        });

        loadGuns.start();
    }
}
