package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import model.MainModel;
import model.Map;
import model.Player;
import model.Point;

import java.io.File;
import java.io.IOException;

public class EventListener implements GLEventListener {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = 1080;
    private final double WALL_HEIGHT = 100;
    private final boolean DEBUG = false;
    private MainModel model;

    private int[] keyEvents;
    private double[][] rayResult;

    private ImageResource[] textures;
    private ImageResource[] sprites;

    public EventListener(int SCREEN_WIDTH, MainModel model, int[] keyEvents) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.model = model;
        this.keyEvents = keyEvents;
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
        loadTextures();
        loadSprites();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        model.update(keyEvents);
        rayResult = model.castRays();

        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL2.GL_TEXTURE_2D);

        Graphics.clear(gl);

        if (!DEBUG){
            //draw floor and ceiling
            Graphics.drawBackground(gl, 0, SCREEN_WIDTH - 1);

            //draw rays
            for (int i = SCREEN_WIDTH - 1; i >= 0; i--) {
                double distToWall = rayResult[i][0];
                double scale = scaleRay(distToWall);

                if (scale > SCREEN_HEIGHT){
                    scale = SCREEN_HEIGHT;
                }

                ImageResource img = rayResult[i][1] == 0 ? textures[0] : textures[1];
                boolean bright = rayResult[i][1] == 0;

                Graphics.drawTexturedRay(gl, img, SCREEN_WIDTH - 1 - i, scale, rayResult[i][2], bright);

            }

            Graphics.drawImage(gl, sprites[0], 200, 256, 1024.0 / SCREEN_HEIGHT);
        } else {

            //for debugging, draws walls

            gl.glColor3f(255f, 255f, 255f);

            for (int i = 0; i < model.getMap().getWalls().length; i++) {
                for (int j = 0; j < model.getMap().getWalls()[i].length; j++) {
                    if (model.getMap().getWalls()[i][j]) {
                        gl.glBegin(GL2.GL_QUADS);

                        gl.glVertex2d(j * 0.1, i * 0.1);
                        gl.glVertex2d((j + 1) * 0.1, i * 0.1);
                        gl.glVertex2d((j + 1) * 0.1, (i + 1) * 0.1);
                        gl.glVertex2d(j * 0.1, (i + 1) * 0.1);

                        gl.glEnd();
                    }
                }
            }

            //draws rays

            gl.glColor3f(1, 0, 0);

            for (int i = 0; i < SCREEN_WIDTH; i++) {
                if (i % 100 == 0) {
                    Point[] p = new Point[]{new Point(model.getPlayer().getxCoor(), model.getPlayer().getyCoor()), new Point(rayResult[i][2], rayResult[i][3])};

                    if (i == 0) {
                        gl.glColor3f(1, 0, 1);
                    } else if (i == 1900) {
                        gl.glColor3f(0, 0.5f, 1);
                    } else {
                        gl.glColor3f(1, 0, 0);
                    }

                    gl.glBegin(GL2.GL_LINES);

                    gl.glVertex2d(p[0].getX() / 1000, p[0].getY() / 1000);
                    gl.glVertex2d(p[1].getX() / 1000, p[1].getY() / 1000);

                    gl.glEnd();
                }

            }

            //draw player direction
            gl.glColor3f(0, 1, 0);
            gl.glBegin(GL2.GL_LINES);

            gl.glVertex2d(model.getPlayer().getxCoor() / 1000, model.getPlayer().getyCoor() / 1000);
            gl.glVertex2d((model.getPlayer().getxCoor() / 1000) + Math.cos(Math.toRadians(model.getPlayer().getAngle())), (model.getPlayer().getyCoor() / 1000) - Math.sin(Math.toRadians(model.getPlayer().getAngle())));

            gl.glEnd();
            //draws grid

            gl.glColor3f(0, 0, 12f);

            for (int i = 1; i < 10; i++) {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2d(i * 0.1, 0);
                gl.glVertex2d(i * 0.1, 1);
                gl.glEnd();

                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2d(0, i * 0.1);
                gl.glVertex2d(1, i * 0.1);
                gl.glEnd();
            }
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {


    }

    private double scaleRay(double rayLength){
        return 192 / rayLength;
    }

    private void loadTextures(){
        textures = new ImageResource[2];

        textures[0] = new ImageResource("/BSTONEA1.png");
        textures[1] = new ImageResource("/BSTONEA2.png");
    }

    private void loadSprites(){
        sprites = new ImageResource[1];

        sprites[0] = new ImageResource("/GARDA1.png");
    }
}
