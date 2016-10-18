package teamdroid.com.speedtestarena;

/**
 * Created by Kenny on 2016-10-11.
 */

/* TODO:
    Add a proper chronometer to track time. (target 30 fps)
    Need to properly sync the update and render cycles after chronometer is implemented.
 */

import android.graphics.Canvas;
import android.view.SurfaceHolder;

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
        long tickCount = 0L;

        while (this.running) {
            tickCount++;

            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) {
                System.out.println("Cannot draw. Canvas is null.");
            } else {
                gamePanel.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
