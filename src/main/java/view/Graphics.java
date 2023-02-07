package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Graphics {
    private static final int TEXTURE_SIZE = 128;


    public static void clear(GL2 gl){
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }

    public static void drawBackground(GL2 gl, double x1, double x2){
        //draw ceiling
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(x1, 1);
        gl.glVertex2d(x2, 1);
        gl.glVertex2d(x2, 0);
        gl.glVertex2d(x1, 0);

        gl.glEnd();

        //draw floor
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(x1, 0);
        gl.glVertex2d(x2, 0);
        gl.glVertex2d(x2, -1);
        gl.glVertex2d(x1, -1);

        gl.glEnd();
    }

    public static void drawRay(GL2 gl, float color, double x, double y){
        gl.glColor3f(color, 0, 0);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(x - 0.5, y);
        gl.glVertex2d(x + 0.5, y);
        gl.glVertex2d(x + 0.5, -y);
        gl.glVertex2d(x - 0.5, -y);

        gl.glEnd();
    }

    public static void drawTexturedRay(GL2 gl, ImageResource img, double x, double y, double index, boolean bright){
        Texture tex = img.getTexture();

        if (tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glColor3f(bright ? 1 : 0.7f, bright ? 1 : 0.7f, bright ? 1 : 0.7f);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2d(index / TEXTURE_SIZE, 0);
        gl.glVertex2d(x - 0.5, y);

        gl.glTexCoord2d((index + 1) / TEXTURE_SIZE, 0);
        gl.glVertex2d(x + 0.5, y);

        gl.glTexCoord2d((index + 1) / TEXTURE_SIZE, 1);
        gl.glVertex2d(x + 0.5, -y);

        gl.glTexCoord2d(index / TEXTURE_SIZE, 1);
        gl.glVertex2d(x - 0.5, -y);

        gl.glEnd();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public static void drawSprite(GL2 gl, ImageResource img, double x, double width, double height){
        Texture tex = img.getTexture();

        if (tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(x - width / 2, height);

        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(x + width / 2, height);

        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(x + width / 2, -height);

        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(x - width / 2, -height);

        gl.glEnd();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public static void drawGrid(GL2 gl, int screenWidth){
        gl.glColor3f(0.75f, 0, 0);

        gl.glBegin(GL2.GL_LINES);

        gl.glVertex2d(0, 0);
        gl.glVertex2d(screenWidth - 1, 0);

        gl.glVertex2d(screenWidth / 2, 1);
        gl.glVertex2d(screenWidth / 2, -1);

        gl.glEnd();
    }
}
