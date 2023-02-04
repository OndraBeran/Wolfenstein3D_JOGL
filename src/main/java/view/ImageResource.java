package view;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class ImageResource {

    private Texture texture;
    private BufferedImage img;

    public ImageResource(String path) {
        URL url = null;
        try {
            url = Path.of("C:\\Users\\Ondra\\IdeaProjects\\Wolfenstein3D_JOGL\\src\\main\\resources\\BSTONEA1.png").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println(url);

        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (img != null){
            img.flush();
        }
    }

    public Texture getTexture(){
        if (texture == null){
            texture = AWTTextureIO.newTexture(Renderer.getProfile(), img, true);
        }

        return texture;
    }
}
