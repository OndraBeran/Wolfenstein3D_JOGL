package view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import model.Map;

public class Renderer {

    private static GLWindow window = null;

    private EventListener wListener;
    private KeyListener keyListener;

    public void init(int res, Map map){
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setSize(640, 360);
        //window.setFullscreen(true);

        wListener = new EventListener(res, map);
        window.addGLEventListener(wListener);

        keyListener = new KeyListener();
        window.addKeyListener(keyListener);

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();

        window.setVisible(true);
    }

    public EventListener getwListener() {
        return wListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public static GLWindow getWindow() {
        return window;
    }
}
