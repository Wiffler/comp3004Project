package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.io.EventQueue;
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
    public EventQueue events;

    private Random r = new Random();

    public int score = 0;

    public Circle randCircle;
    public Text scoreText;
    public Text tickText;
    public ParticleTracer trace;
    public ArrayList<Particle> particleList;

    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel, Context context) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        // Create the threads
        audioThread = new CanvasTestAudioThread(context);
        soundPoolThread =  new CanvasTestSoundPoolThread(context);
        timer = new GameTimer();
        events = new EventQueue();

        // Create the objects
        randCircle = new Circle(0, 0, 100, "#008000");
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
        particleList = new ArrayList<Particle>();
        randCircle.setCenter(gamePanel.getWidth() / 2, gamePanel.getHeight() / 2);
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

    private void handleIO() {
        MotionEvent event;
        int action;

        while (events.size() > 0) {
            event = events.dequeue();
            action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
                //System.out.println("ACTION_DOWN");
                if (event.getY() > gamePanel.getHeight() - 50) {
                    this.setRunning(false);
                    ((Activity) gamePanel.getContext()).finish();

                } else {
                    System.out.println("Coords: x=" + event.getX() + ", y=" + event.getY());

                    if (randCircle.inCircle(event.getX(), event.getY())) {
                        score += 1;
                    }

                    trace.set(event.getX(), event.getY());
                }

            } else if (action == MotionEvent.ACTION_MOVE) {
                //System.out.println("ACTION_MOVE");
                trace.eventUpdate(event.getX(), event.getY());

            } else if (action == MotionEvent.ACTION_UP) {
                //System.out.println("ACTION_UP");
                trace.reset();
            }
        }
    }

    private void updateState(long curTime) {
        // Update circle
        randCircle.update(curTime,
                r.nextInt((gamePanel.getWidth() - 100) - 100) + 100,
                r.nextInt((gamePanel.getHeight() - 100) - 100) + 100);

        // Update the score text
        scoreText.setText("Score: " + score);

        // Update the particle list
        for (Iterator<Particle> iterator = particleList.iterator(); iterator.hasNext(); ) {
            Particle p = iterator.next();
            if (p.getAlpha() <= 0) {
                iterator.remove();
            } else {
                p.update();
            }
        }
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

                // handle the IO events
                handleIO();

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
