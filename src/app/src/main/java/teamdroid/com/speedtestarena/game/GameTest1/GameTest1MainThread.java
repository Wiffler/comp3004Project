package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.sound.CanvasTestAudioThread;
import teamdroid.com.speedtestarena.sound.CanvasTestSoundPoolThread;
import teamdroid.com.speedtestarena.actor.Circle;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.utility.GameTimer;

import static android.R.id.list;

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
    private GameTimer timer;

    private Random r = new Random();

    public int score = 0;

    public Circle[] circles;
    public Text scoreText;
    public Text tickText;
    public ParticleTracer trace;
    //public volatile ArrayList<Particle> particleList;

    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel, Context context) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        // Create the threads
        audioThread = new CanvasTestAudioThread(context);
        soundPoolThread =  new CanvasTestSoundPoolThread(context);
        timer = new GameTimer();

        // Create the objects
        circles = new Circle[4];
        circles[0] = new Circle(0, 0, 100, "#C0C0C0");
        circles[1] = new Circle(0, 0, 100, "#008000");
        circles[2] = new Circle(0, 0, 100, "#C0C0C0");
        circles[3] = new Circle(0, 0, 100, "#008000");
        scoreText = new Text(0, 0, "Score: " + score, "#FFFFFF");
        tickText = new Text(0, 0, "Interval: ", "#FFFFFF");
        trace = new ParticleTracer(context, this);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() { return this.running; }

    private void initialise() {
        // Setup the objects
        //particleList = new ArrayList<Particle>();
        circles[0].setCenter(gamePanel.getWidth() / 2, gamePanel.getHeight() / 2);
        circles[1].setCenter(gamePanel.getWidth() / 2, (gamePanel.getHeight() / 2) - 250);
        circles[2].setCenter((gamePanel.getWidth() / 2) + 250, gamePanel.getHeight() / 2);
        circles[3].setCenter((gamePanel.getWidth() / 2) + 250, (gamePanel.getHeight() / 2) - 250);
        scoreText.setPosition(50, 50);
        tickText.setPosition(50, 100);

        // Setup the threads
        audioThread.setRunning(true);
        soundPoolThread.setRunning(true);

        // Start the threads
        audioThread.start();
        soundPoolThread.start();
    }

    private void cleanup() {
        // Stop the threads
        try {
            audioThread.setRunning(false);
            soundPoolThread.setRunning(false);
            audioThread.join();
            soundPoolThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateState(long curTime) {
        circles[0].update(curTime,
                r.nextInt((gamePanel.getWidth() - 100) - 100) + 100,
                r.nextInt((gamePanel.getHeight() - 100) - 100) + 100);
        scoreText.setText("Score: " + score);

        /*
        System.out.println(particleList.size());
        for (Iterator<Particle> iterator = particleList.iterator(); iterator.hasNext(); ) {
            Particle p = iterator.next();
            if (p.getAlpha() <= 0) {
                //System.out.println("Removing...");
                iterator.remove();
            } else {
                p.update();
            }
        }
        */
    }

    @Override
    public void run() {
        long prevTime = 0;
        int frames_done = 0;
        int fps = 0;

        // Start the timer
        timer.setRunning(true);
        timer.start();

        // initialise the objects
        initialise();

        // Start the game
        while (!gamePanel.ready) {
            audioThread.startAudio();
        }

        while (this.running) {
            while (timer.getTicks() == 0) {
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
                updateState(timer.getTime());

                timer.decrementTicks();
                if(old_ticks <= timer.getTicks()) {
                    break;
                }
            }

            if (timer.getTime() - prevTime >= 1000) {
                // fps now holds the the number of frames done in the last second
                fps = frames_done;
                tickText.setText("FPS: " + fps);

                // reset for the next second
                frames_done = 0;
                prevTime = timer.getTime();
            }

            // draw a frame
            gamePanel.postInvalidate();
            frames_done++;
        }

        // Cleanup resources
        cleanup();

        // End the timer
        try {
            timer.setRunning(false);
            timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
