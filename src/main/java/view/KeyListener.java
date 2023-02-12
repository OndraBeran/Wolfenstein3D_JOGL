package view;

import com.jogamp.newt.event.KeyEvent;
import model.KeyInputData;

import java.util.Arrays;

public class KeyListener implements com.jogamp.newt.event.KeyListener {
    private static int[] keyEvents = new int[]{0, 0};

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                KeyInputData.setRotation(1);
                break;
            case KeyEvent.VK_RIGHT:
                KeyInputData.setRotation(-1);
                break;
            case KeyEvent.VK_UP:
                KeyInputData.setMovement(1);
                break;
            case KeyEvent.VK_DOWN:
                KeyInputData.setMovement(-1);
                break;
            case KeyEvent.VK_SPACE:
                KeyInputData.setShooting(true);
                break;
            case KeyEvent.VK_F3:
                KeyInputData.setDebugging(!KeyInputData.isDebugging());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isAutoRepeat()){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    KeyInputData.setRotation(0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    KeyInputData.setMovement(0);
                    break;
                case KeyEvent.VK_SPACE:
                    KeyInputData.setShooting(false);
                    break;
            }
        }
    }

    public static int[] getKeyEvents() {
        return keyEvents;
    }
}
