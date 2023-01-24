package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class EventListener implements GLEventListener {

    private final int SCREEN_WIDTH;
    private final double FULL_HEIGHT_DIST = 25;

    private double[] rayResult;

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

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            double scale = scaleRay(rayResult[i]);

            gl.glBegin(GL2.GL_LINES);

            gl.glVertex2d(i, 1 - scale);
            gl.glVertex2d(i, -(1 - scale));

            gl.glEnd();
        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0, SCREEN_WIDTH - 1, -1, 1, 0, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void setRayResult(double[] rayResult) {
        this.rayResult = rayResult;
    }

    private double scaleRay(double rayLength){
        return FULL_HEIGHT_DIST / rayLength;
    }
}
