package teamdroid.com.speedtestarena;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1MainThread extends Thread {
    // flag to hold game state
    private volatile boolean running = false;

    private SurfaceHolder surfaceHolder;
    private GameTest1 gamePanel;

    private CanvasTestAudioThread audioThread;
    private CanvasTestSoundPoolThread soundPoolThread;

    private Random r = new Random();

    public int score = 0;

    public GraphicCircle[] circles;
    public GraphicText scoreText;
    public GraphicText tickText;

    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel, Context context) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        this.audioThread = new CanvasTestAudioThread(context);
        this.soundPoolThread =  new CanvasTestSoundPoolThread(context);

        circles = new GraphicCircle[4];
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long curTime = 0;
        long prevTime = 0;
        long totTime = 0;
        long intervalTime = 0;

        audioThread.setRunning(true);
        soundPoolThread.setRunning(true);

        audioThread.start();
        soundPoolThread.start();

        // Create the objects
        circles[0] = new GraphicCircle(gamePanel.getWidth() / 2, gamePanel.getHeight() / 2, 100, "#C0C0C0");
        circles[1] = new GraphicCircle(gamePanel.getWidth() / 2, (gamePanel.getHeight() / 2) - 250, 100, "#008000");
        circles[2] = new GraphicCircle((gamePanel.getWidth() / 2) + 250, gamePanel.getHeight() / 2, 100, "#C0C0C0");
        circles[3] = new GraphicCircle((gamePanel.getWidth() / 2) + 250, (gamePanel.getHeight() / 2) - 250, 100, "#008000");
        scoreText = new GraphicText(50, 50, "Score: " + score, "#000000");
        tickText = new GraphicText(50, 100, "Interval: " + (curTime - prevTime), "#000000");

        audioThread.startAudio();

        while (this.running) {
            prevTime = curTime;
            curTime = System.currentTimeMillis();
            totTime += (curTime - prevTime);
            intervalTime += (curTime - prevTime);

            if (intervalTime >= 15) {
                if (totTime >= 2990) {
                    circles[0].setCenter(r.nextInt((gamePanel.getWidth() - 100) - 100) + 100,
                            r.nextInt((gamePanel.getHeight() - 100) - 100) + 100);
                    totTime = 0;
                }

                scoreText.setText("Score: " + score);
                tickText.setText("Interval: " + intervalTime);
                intervalTime = 0;
            }

            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) {
                System.out.println("Cannot draw. Canvas is null.");
            } else {
                gamePanel.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        audioThread.setRunning(false);
        soundPoolThread.setRunning(false);

        try {
            audioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            soundPoolThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
