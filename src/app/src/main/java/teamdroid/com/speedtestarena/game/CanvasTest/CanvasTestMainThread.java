package teamdroid.com.speedtestarena.game.CanvasTest;

/**
 * Created by Kenny on 2016-10-11.
 */

/* TODO:
    Add a proper chronometer to track time. (target 30 fps)
    Need to properly sync the update and render cycles after chronometer is implemented.
 */

import android.view.SurfaceHolder;

import teamdroid.com.speedtestarena.utility.GameTimer;

public class CanvasTestMainThread extends Thread {
    private volatile boolean running = false;

    private SurfaceHolder surfaceHolder;
    private CanvasTest gamePanel;
    private GameTimer timer;

    public CanvasTestMainThread(SurfaceHolder surfaceHolder, CanvasTest gamePanel) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        timer = new GameTimer();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long frames_done = 0;
        long fps = 0;
        long prevTime = 0;

        timer.setRunning(true);
        timer.start();

        while (this.running) {
            while(timer.getTicks() == 0) {
                // No need to update so sleep the thread
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while(timer.getTicks() > 0) {
                long old_ticks = timer.getTicks();

                // update game state
                gamePanel.trace.tickUpdate();

                timer.decrementTicks();
                if(old_ticks <= timer.getTicks()) {
                    break;
                }
            }

            if (timer.getTime() - prevTime >= 1000) {
                // fps now holds the the number of frames done in the last second
                fps = frames_done;
                gamePanel.tickText.setText("FPS: " + fps);

                // reset for the next second
                frames_done = 0;
                prevTime = timer.getTime();
            }

            // draw a frame
            gamePanel.postInvalidate();
            frames_done++;
        }

        try {
            timer.setRunning(false);
            timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
