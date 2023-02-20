package view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import model.KeyInputData;
import model.MainModel;
import model.ModelLoop;
import model.renderdata.RenderData;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class EventListener implements GLEventListener {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = 1080;
    private final double WALL_HEIGHT = 100;
    private MainModel model;

    private CyclicBarrier barrier;

    TextRenderer textRenderer;

    private ImageResource introImg;
    private ImageResource[] textures;
    private ImageResource[][] enemySprites;
    private ImageResource[] gunSprites;

    private boolean gameStarted = false;
    private float promptColor = 0;
    private long lastChange = 0;

    private double deadScreenOpacity = 0;

    private boolean transitionOngoing = false;
    private double endLevelOpacity = 0;
    private double newLevelOpacity = 1;

    public EventListener(int SCREEN_WIDTH, MainModel model, int[] keyEvents, CyclicBarrier barrier) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.model = model;
        this.barrier = barrier;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        Graphics.init(SCREEN_WIDTH, SCREEN_HEIGHT);

        try {
            URL font = getClass().getResource("/wolfenstein.ttf");

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(font.toURI())));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        textRenderer = new TextRenderer(new Font("Wolfenstein", Font.PLAIN, 64));

        defaultUnits(gl);

        loadAssets();
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

        if (!gameStarted){
            Graphics.drawImage(gl, introImg);
            setGameStarted(KeyListener.firstKeyPressed);

            //draw prompt
            Graphics.drawText(textRenderer, "press any key to start", Graphics.TextPos.MIDDLE, promptColor);
            if (System.currentTimeMillis() - lastChange > 500){
                lastChange = System.currentTimeMillis();
                promptColor = 1 - promptColor;
            }
            return;
        }

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

                    ImageResource img;

                    if (!KeyInputData.isSwastikaMode()){
                        img = data.rays()[j].intersectsXAxis() ? textures[0] : textures[1];
                    } else {
                        img = data.rays()[j].intersectsXAxis() ? textures[2] : textures[3];
                    }

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

        //draw status
        Graphics.drawText(textRenderer, "HP: " + data.player().HP(), Graphics.TextPos.RIGHT, 1);

        if (KeyInputData.isDebugging()){
            //draw minimap
            minimapUnits(gl);
            Graphics.drawMinimap(gl, model.player, model.enemies);
            defaultUnits(gl);
        }

        if (data.player().isDead()){
            Graphics.fillScreen(gl, 1, 0, 0, deadScreenOpacity);
            if (deadScreenOpacity < 1){
                deadScreenOpacity += 0.05;
            }
        }

        if (data.state().levelFinished()){
            transitionOngoing = true;
        }

        if (transitionOngoing){
            if (endLevelOpacity <= 1){
                Graphics.fillScreen(gl, 0, 0, 0, endLevelOpacity);
                endLevelOpacity += 0.05;
            } else {
                Graphics.fillScreen(gl, 0, 0, 0, newLevelOpacity);
                newLevelOpacity -= 0.05;

                if (newLevelOpacity <= 0){
                    transitionOngoing = false;
                    endLevelOpacity = 0;
                    newLevelOpacity = 1;
                }
            }
        }

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
        CountDownLatch latch = new CountDownLatch(4);

        loadIntroImg(latch);
        loadTextures(latch);
        loadEnemies(latch);
        loadGuns(latch);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadIntroImg(CountDownLatch latch){
        Thread loadIntroImg = new Thread(() -> {
            introImg = new ImageResource("/intro_graphic.jpg");

            latch.countDown();
        }, "loadIntroImg");

        loadIntroImg.start();
    }

    private void loadTextures(CountDownLatch latch){
        Thread loadTexture = new Thread(() -> {
            textures = new ImageResource[4];

            textures[0] = new ImageResource("/textures/BSTONEA1.png");
            textures[1] = new ImageResource("/textures/BSTONEA2.png");
            textures[2] = new ImageResource("/textures/GSTHTLR1.png");
            textures[3] = new ImageResource("/textures/GSTHTLR2.png");

            latch.countDown();
        }, "loadTexture");

        loadTexture.start();
    }

    private void loadEnemies(CountDownLatch latch){
        Thread loadEnemies = new Thread(() -> {
            enemySprites = new ImageResource[10][];

            for (int i = 0; i < enemySprites.length - 2; i++) {
                enemySprites[i] = new ImageResource[5];

                enemySprites[i][0] = new ImageResource("/gard/GARDA" + (i + 1) + ".png");
                enemySprites[i][1] = new ImageResource("/gard/GARDB" + (i + 1) + ".png");
                enemySprites[i][2] = new ImageResource("/gard/GARDC" + (i + 1) + ".png");
                enemySprites[i][3] = new ImageResource("/gard/GARDD" + (i + 1) + ".png");
                enemySprites[i][4] = new ImageResource("/gard/GARDE" + (i + 1) + ".png");
            }

            enemySprites[8] = new ImageResource[3];

            enemySprites[8][0] = new ImageResource("/gard/GARDF0.png");
            enemySprites[8][1] = new ImageResource("/gard/GARDG0.png");
            enemySprites[8][2] = new ImageResource("/gard/GARDH0.png");

            enemySprites[9] = new ImageResource[5];

            enemySprites[9][0] = new ImageResource("/gard/GARDI0.png");
            enemySprites[9][1] = new ImageResource("/gard/GARDK0.png");
            enemySprites[9][2] = new ImageResource("/gard/GARDL0.png");
            enemySprites[9][3] = new ImageResource("/gard/GARDM0.png");
            enemySprites[9][4] = new ImageResource("/gard/GARDN0.png");

            latch.countDown();
        }, "loadEnemy");

        loadEnemies.start();
    }

    private void loadGuns(CountDownLatch latch){
        Thread loadGuns = new Thread(() -> {
            gunSprites = new ImageResource[3];

            gunSprites[0] = new ImageResource("/gun/V_LUGR_A.png");
            gunSprites[1] = new ImageResource("/gun/V_LUGR_B.png");
            gunSprites[2] = new ImageResource("/gun/V_LUGR_C.png");

            latch.countDown();
        }, "loadGun");

        loadGuns.start();
    }

    private void defaultUnits(GL2 gl){
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0, SCREEN_WIDTH - 1, -1, 1, 0, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void minimapUnits(GL2 gl){
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(-3, 1, SCREEN_HEIGHT / (SCREEN_WIDTH / 4.0), 0, 0, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void startGame(){
        ModelLoop.initLoop(model);
        ModelLoop.start();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void setGameStarted(boolean started){
        if (started){
            startGame();
        }
        gameStarted = started;
    }
}
