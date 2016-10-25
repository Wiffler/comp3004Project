package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.actor.ShadowedCurve;
import teamdroid.com.speedtestarena.actor.HitCircle;
import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.sound.GameAudio;
import teamdroid.com.speedtestarena.utility.GameTimer;


/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1MainThread extends Thread {
    // flag to hold game state
    private volatile boolean running = false;
    private Random r = new Random();

    private SurfaceHolder surfaceHolder;
    private GameTest1 gamePanel;
    private GameAudio song;
    private GameTimer timer;

    public int score = 0;
    public HitCircle randCircle, randCircle2;
    public ShadowedCurve curve;
    public Text scoreText;
    public Text tickText;
    public ParticleTracer trace;
    public ArrayList<Particle> particleList;

    // Constructor(s)
    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        timer = new GameTimer();
    }

    // Set and check the running state
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean isRunning() { return this.running; }

    // Creates the objects
    private void create() {
        // Load the textures
        int[] idList = {R.drawable.cursor, R.drawable.cursortrail,
                        R.drawable.hitcircle, R.drawable.hitcircleoverlay};
        gamePanel.render.loadBitmaps(gamePanel.activity, idList);

        // Create the objects
        song = new GameAudio();
        song.createAudio(gamePanel.activity, R.raw.test_sound_file1);

        randCircle = new HitCircle(R.drawable.hitcircleoverlay, 0, 0, 50);
        randCircle2 = new HitCircle(R.drawable.hitcircleoverlay, 0, 0, 50);
        curve  = new ShadowedCurve(0, 0, 0, 0, 0, 0, 0, 0);
        scoreText = new Text(0, 0, "Score: " + score, "#FFFFFF");
        tickText = new Text(0, 0, "Interval: ", "#FFFFFF");
        trace = new ParticleTracer(gamePanel.textures, this);

        particleList = new ArrayList<Particle>();
    }

    // Initialises the objects
    private void initialise() {
        // Setup the objects
        scoreText.setPosition(50, 50);
        tickText.setPosition(50, 100);
    }

    // Cleanup threads and resources when game ends
    private void cleanup() {
        song.pauseAudio();
        song.cleanup();
    }

    // Processes the event queue
    private void handleIO() {
        MotionEvent event;
        int action;

        while (gamePanel.events.size() > 0) {
            event = gamePanel.events.dequeue();
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

    // Updates the state of the objects every tick
    private void updateState(long curTime) {
        // Update circle
        randCircle.update(curTime,
                r.nextInt((gamePanel.getWidth() - 100) - 100) + 100,
                r.nextInt((gamePanel.getHeight() - 100) - 100) + 100);
        randCircle2.update(curTime,
                r.nextInt((gamePanel.getWidth() - 100) - 100) + 100,
                r.nextInt((gamePanel.getHeight() - 100) - 100) + 100);

        curve.setStartPoint(randCircle.getX(), randCircle.getY());
        curve.setControlPoint1(randCircle.getX(), randCircle.getY());
        curve.setControlPoint2(randCircle2.getX(), randCircle2.getY());
        curve.setEndPoint(randCircle2.getX(), randCircle2.getY());
        curve.reconstruct();

        //randCircle.getOverlay().update();
        //randCircle2.getOverlay().update();

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

        // create and initialise the objects
        create();
        initialise();

        // Set the ready state for the UI
        gamePanel.ready = true;

        // Start the audio
        song.startAudio();

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
