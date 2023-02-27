package view;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageResource {

    private Texture texture;
    private BufferedImage img;

    public ImageResource(String path) {
        URL url = getClass().getResource(path);

        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (img != null) {
            img.flush();
        }
    }

    public Texture getTexture() {
        if (texture == null) {
            texture = AWTTextureIO.newTexture(Renderer.getProfile(), img, true);
        }

        return texture;
    }
}
