package view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import model.Map;

public class Renderer {

    private static GLWindow window = null;

    private EventListener listener;

    public void init(int res, Map map){
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setSize(640, 360);
        window.setFullscreen(true);

        listener = new EventListener(res, map);
        window.addGLEventListener(listener);

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();

        window.setVisible(true);
    }

    public EventListener getListener() {
        return listener;
    }
}
