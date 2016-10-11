package teamdroid.com.speedtestarena;

/**
 * Created by Kenny on 2016-10-11.
 */

import android.view.SurfaceHolder;

public class CanvasTestMainThread extends Thread {
    // flag to hold game state
    private boolean running;

    private SurfaceHolder surfaceHolder;
    private CanvasTest gamePanel;

    public CanvasTestMainThread(SurfaceHolder surfaceHolder, CanvasTest gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long tickCount = 0L;
        while (running) {
            tickCount++;
            // update game state
            // render state to the screen
        }
    }
}
