package view;

import com.jogamp.newt.event.KeyEvent;

import java.util.Arrays;

public class KeyListener implements com.jogamp.newt.event.KeyListener {
    private int[] keyEvents = new int[]{0, 0};

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                keyEvents[0] = 1;
                break;
            case KeyEvent.VK_RIGHT:
                keyEvents[0] = -1;
                break;
            case KeyEvent.VK_UP:
                keyEvents[1] = 1;
                break;
            case KeyEvent.VK_DOWN:
                keyEvents[1] = -1;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isAutoRepeat()){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    keyEvents[0] = 0;
                    break;
            }
        }
    }

    public int[] getKeyEvents() {
        return keyEvents;
    }
}
