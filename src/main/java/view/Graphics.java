package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import model.Map;
import model.Player;
import model.Soldier;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Graphics {
    private static final int TEXTURE_SIZE = 128;
    private static int WIDTH;
    private static int HEIGHT;

    private static final int TEXT_MARGIN = 200;

    public static void init(int width, int height){
        WIDTH = width;
        HEIGHT = height;
    }

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

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

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

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

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

    public static void drawGun(GL2 gl, ImageResource img, double centerX, double size){
        Texture tex = img.getTexture();

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

        if (tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(centerX - (size / 2), -1 + (size / 540));

        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(centerX + (size / 2), -1 + (size / 540));

        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(centerX + (size / 2), -1);

        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(centerX - (size / 2), -1);

        gl.glEnd();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public static void drawImage(GL2 gl, ImageResource img){
        Texture tex = img.getTexture();

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

        if (tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(0, 1);

        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(WIDTH - 1, 1);

        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(WIDTH - 1, -1);

        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(0, -1);

        gl.glEnd();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public static void drawMinimap(GL2 gl, Player player, ArrayList<Soldier> enemies){
        gl.glColor3d(0.5, 0.5, 0.5);
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(0, 0);
        gl.glVertex2d(0, 1);
        gl.glVertex2d(1, 1);
        gl.glVertex2d(1, 0);

        gl.glEnd();

        gl.glColor3d(0, 0.25, 1);

        int[][] walls = Map.getWalls();

        for (int i = 0; i < walls.length; i++) {
            for (int j = 0; j < walls[i].length; j++) {
                if (walls[i][j] != 0){
                    drawMinimapTile(gl, j, i);
                }
            }
        }

        gl.glColor3d(1, 0, 0);
        drawMinimapTile(gl, Map.coordToTile(player.getxCoor()), Map.coordToTile(player.getyCoor()));

        gl.glColor3d(0, 1, 0);
        for (Soldier soldier :
                enemies) {
            drawMinimapTile(gl, Map.coordToTile(soldier.getX()), Map.coordToTile(soldier.getY()));

            gl.glColor3d(0.5, 0, 1);

            drawMinimapTile(gl, soldier.targetTile[0], soldier.targetTile[1]);
        }
    }

    private static void drawMinimapTile(GL2 gl, int x, int y){
        double tileSize = 1.0 / Map.getNUMBER_OF_TILES();
        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(x * tileSize, y * tileSize);
        gl.glVertex2d((x + 1) * tileSize, y * tileSize);
        gl.glVertex2d((x + 1) * tileSize, (y + 1) * tileSize);
        gl.glVertex2d(x * tileSize, (y + 1) * tileSize);

        gl.glEnd();
    }

    public static void drawText(TextRenderer renderer, String text, TextPos position, float color){
        renderer.beginRendering(WIDTH, HEIGHT);

        renderer.setColor(1, 1, 1, color);

        int fontSize = renderer.getFont().getSize();

        Rectangle2D rect = renderer.getBounds(text);

        switch (position){
            case LEFT -> renderer.draw(text, (int)textStartLeft(fontSize), TEXT_MARGIN / 3);
            case CENTER_BOTTOM -> renderer.draw(text, (int) textStartCenterX(rect.getWidth()), TEXT_MARGIN / 2);
            case RIGHT -> renderer.draw(text, (int)textStartRight(text, fontSize), TEXT_MARGIN / 3);
            case MIDDLE -> renderer.draw(text, (int) textStartCenterX(rect.getWidth()), (int)textStartCenterY(rect.getHeight()));
        }

        renderer.endRendering();
    }

    public static void fillScreen(GL2 gl, double red, double green, double blue, double alpha){
        gl.glColor4d(red, green, blue, alpha);

        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex2d(0, 1);
        gl.glVertex2d(WIDTH - 1, 1);
        gl.glVertex2d(WIDTH - 1, -1);
        gl.glVertex2d(0, -1);

        gl.glEnd();
    }

    private static double textStartCenterX(double width){
        return (WIDTH - width) / 2;
    }

    private static double textStartCenterY(double height){
        return (HEIGHT - height) / 2;
    }

    private static double textStartLeft(int fontSize){
        return TEXT_MARGIN;
    }

    private static double textStartRight(String text, int fontSize){
        return WIDTH - (text.length() + TEXT_MARGIN);
    }

    public enum TextPos{
        LEFT, RIGHT, CENTER_BOTTOM, MIDDLE;
    }
}
