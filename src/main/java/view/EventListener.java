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

    //temp
    Map m = new Map();

    private Point[][] rayResult;

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

        if (rayResult == null){
            return;
        }

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
        }

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

        gl.glColor3f(255f, 0, 0);

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            Point[] p = rayResult[i];

            gl.glBegin(GL2.GL_LINES);

            gl.glVertex2d(p[0].getX() / 1000, p[0].getY() / 1000);
            gl.glVertex2d(p[1].getX() / 1000, p[1].getY() / 1000);

            gl.glEnd();
        }

//        gl.glBegin(GL2.GL_QUADS);
//
//        gl.glVertex2f(0.1f, 0.1f);
//        gl.glVertex2f(0.2f, 0.1f);
//        gl.glVertex2f(0.2f, 0.2f);
//        gl.glVertex2f(0.1f, 0.2f);
//
//        gl.glEnd();
//        for (int i = 0; i < SCREEN_WIDTH; i++) {
//            double scale = scaleRay(rayResult[i]);
//            System.out.println(scale);
//            if (scale > SCREEN_HEIGHT){
//                scale = SCREEN_HEIGHT;
//            }
//
//            gl.glBegin(GL2.GL_LINES);
//
//            gl.glVertex2d(i, scale);
//            gl.glVertex2d(i, -scale);
//
//            gl.glEnd();
//        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        /*original
        gl.glOrtho(0, SCREEN_WIDTH - 1, -1, 1, 0, 1);*/

        //temp
        gl.glOrtho(0, 16 / 9.0, 1, 0, 0, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void setRayResult(Point[][] rayResult) {
        this.rayResult = rayResult;
    }

    private double scaleRay(double rayLength){
        return WALL_HEIGHT / rayLength;
    }
}
