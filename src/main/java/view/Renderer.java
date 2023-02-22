package view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import model.MainModel;

import java.util.concurrent.CyclicBarrier;

public class Renderer {

    private static GLProfile profile;
    private static GLWindow window = null;

    private EventListener wListener;
    private KeyListener keyListener;

    public void init(int res, MainModel model, CyclicBarrier barrier){
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setTitle("Wolfenstein 3D");
        window.setSize((int)(640 * 1.5), (int)(360 * 1.5));
        window.setPointerVisible(false);
        //window.setFullscreen(true);


        keyListener = new KeyListener();
        window.addKeyListener(keyListener);

        wListener = new EventListener(res, model, keyListener.getKeyEvents(), barrier);
        window.addGLEventListener(wListener);

        window.setVisible(true);
    }

    public EventListener getwListener() {
        return wListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public GLWindow getWindow() {
        return window;
    }

    public static GLProfile getProfile() {
        return profile;
    }
}
