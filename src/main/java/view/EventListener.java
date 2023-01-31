package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import model.Map;
import model.Point;

public class EventListener implements GLEventListener {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = 1080;
    private final double WALL_HEIGHT = 100;

    private double[][] rayResult;

    public EventListener(int SCREEN_WIDTH) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(0, 1);
        gl.glVertex2d(SCREEN_WIDTH - 1, 1);
        gl.glVertex2d(SCREEN_WIDTH - 1, 0);
        gl.glVertex2d(0, 0);

        gl.glEnd();

        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(0, 0);
        gl.glVertex2d(SCREEN_WIDTH - 1, 0);
        gl.glVertex2d(SCREEN_WIDTH - 1, -1);
        gl.glVertex2d(0, -1);

        gl.glEnd();

        if (rayResult == null){
            return;
        }

        /*

        for debugging, draws walls

        gl.glColor3f(255f, 255f, 255f);

        for (int i = 0; i < m.getWalls().length; i++) {
            for (int j = 0; j < m.getWalls()[i].length; j++) {
                if (m.getWalls()[i][j]){
                    gl.glBegin(GL2.GL_QUADS);

                    gl.glVertex2d(j * 0.1, i * 0.1);
                    gl.glVertex2d((j + 1) * 0.1, i * 0.1);
                    gl.glVertex2d((j + 1) * 0.1, (i + 1) * 0.1);
                    gl.glVertex2d(j * 0.1, (i + 1) * 0.1);

                    gl.glEnd();
                }
            }
        }*/

        /*
        draws grid

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
        }*/

        /*

        draws rays

        gl.glColor3f(255f, 0, 0);

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            Point[] p = rayResult[i];

            gl.glBegin(GL2.GL_LINES);

            gl.glVertex2d(p[0].getX() / 1000, p[0].getY() / 1000);
            gl.glVertex2d(p[1].getX() / 1000, p[1].getY() / 1000);

            gl.glEnd();
        }*/


        for (int i = SCREEN_WIDTH - 1; i >= 0; i--) {
            double distToWall = rayResult[i][0];
            double scale = scaleRay(distToWall);

            if (scale > SCREEN_HEIGHT){
                scale = SCREEN_HEIGHT;
            }

            if (rayResult[i][1] == 0){
                gl.glColor3f(1f, 0, 0);
            } else {
                gl.glColor3f(0.5f, 0, 0);
            }

            gl.glBegin(GL2.GL_LINES);

            gl.glVertex2d(i, scale);
            gl.glVertex2d(i, -scale);

            gl.glEnd();
        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();


        gl.glOrtho(0, SCREEN_WIDTH - 1, -1, 1, 0, 1);

        /* for debugging
        gl.glOrtho(0, 16 / 9.0, 1, 0, 0, 1);*/

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void setRayResult(double[][] rayResult) {
        this.rayResult = rayResult;
    }

    private double scaleRay(double rayLength){
        return WALL_HEIGHT / rayLength;
    }
}
