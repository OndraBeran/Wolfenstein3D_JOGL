package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class EventListener implements GLEventListener {

    private double[] rayResult;

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

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }

    public void setRayResult(double[] rayResult) {
        this.rayResult = rayResult;
    }
}
