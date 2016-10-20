package teamdroid.com.speedtestarena.game.CanvasTest;

/**
 * Created by Kenny on 2016-10-11.
 */

/* TODO:
    Add a proper chronometer to track time. (target 30 fps)
    Need to properly sync the update and render cycles after chronometer is implemented.
 */

import android.view.SurfaceHolder;

import teamdroid.com.speedtestarena.game.CanvasTest.CanvasTest;

public class CanvasTestMainThread extends Thread {
    // flag to hold game state
    private volatile boolean running = false;

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
        long prevTime = 0;
        long curTime = System.currentTimeMillis();
        long intervalTime = (curTime - prevTime);

        while (this.running) {
            prevTime = curTime;
            curTime = System.currentTimeMillis();
            intervalTime = (curTime - prevTime);

            gamePanel.trace.tickUpdate();

            gamePanel.tickText.setText("Interval: " + intervalTime);

            gamePanel.postInvalidate();
        }
    }
}
