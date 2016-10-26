package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import teamdroid.com.speedtestarena.R;
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

    // Actors
    public int score = 0;
    //public ShadowedCurve curve;
    public Text scoreText;
    public Text tickText;
    public ParticleTracer trace;

    public HitMap mapper;

    // Lists to hold actors
    public ArrayList<Particle> particleList;
    public ArrayList<HitCircle> hitcircleList;

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
        song.createAudio(gamePanel.activity, R.raw.test_sound_file2);

        //curve  = new ShadowedCurve(0, 0, 0, 0, 0, 0, 0, 0);
        scoreText = new Text(0, 0, "Score: " + score, "#FFFFFF");
        tickText = new Text(0, 0, "Interval: ", "#FFFFFF");
        trace = new ParticleTracer(gamePanel.textures, this);

        particleList = new ArrayList<Particle>();
        hitcircleList = new ArrayList<HitCircle>();
        mapper = new HitMap();
    }

    // Initialises the objects
    private void initialise() {
        // Setup the objects
        scoreText.setPosition(50, 50);
        tickText.setPosition(50, 100);

        mapper.initialise(gamePanel.activity);
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
            //action = event.getAction();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getY() > gamePanel.getHeight() - 50) {
                        this.setRunning(false);
                        ((Activity) gamePanel.getContext()).finish();

                    } else {
                        System.out.println("Coords: x=" + event.getX() + ", y=" + event.getY());

                        // Update the hitcircles
                        for (Iterator<HitCircle> iterator = hitcircleList.iterator(); iterator.hasNext(); ) {
                            HitCircle h = iterator.next();
                            if (h.inCircle(event.getX(), event.getY())) {
                                score += 1;
                                iterator.remove();
                            }
                        }

                        trace.set(event.getX(), event.getY());
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    trace.eventUpdate(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_UP:
                    trace.reset();
                    break;
            }
        }
    }

    // Updates the state of the objects every tick
    private void updateState(long curTime) {
        // Update the hitcircles
        for (Iterator<HitCircle> iterator = hitcircleList.iterator(); iterator.hasNext(); ) {
            HitCircle h = iterator.next();
            if (!h.update(song.getPosition())) {
                iterator.remove();
            }
        }

        // Spawn the hitcircles
        long spTime = 0;
        if (mapper.spawnTimeIndex < mapper.hitcircleSpawnTimes.size()) {
            spTime = mapper.hitcircleSpawnTimes.get(mapper.spawnTimeIndex);
            while (spTime <= song.getPosition()) {
                //System.out.println("SPTIME: " + spTime + " SONG POSITION: " + song.getPosition() + " SPAWN INDEX: " + mapper.spawnTimeIndex + " Size: " + mapper.hitcircleSpawnTimes.size());

                // Create new hit circle
                hitcircleList.add(new HitCircle(R.drawable.hitcircleoverlay,
                        mapper.spawnLocX[mapper.hitcircleLocation.get(mapper.spawnTimeIndex)],
                        mapper.spawnLocY[mapper.hitcircleLocation.get(mapper.spawnTimeIndex)],
                        50,
                        mapper.hitcircleSpawnTimes.get(mapper.spawnTimeIndex),
                        mapper.hitcircleDeathTimes.get(mapper.spawnTimeIndex)));

                mapper.spawnTimeIndex = mapper.spawnTimeIndex + 1;

                System.out.println("SPAWN INDEX: " + mapper.spawnTimeIndex);
                if (mapper.spawnTimeIndex < mapper.hitcircleSpawnTimes.size()) {
                    spTime = mapper.hitcircleSpawnTimes.get(mapper.spawnTimeIndex);
                } else {
                    spTime = song.getDuration() + 1;
                }
            }
        }

        // Update the connecting curves
        //curve.setStartPoint(randCircle.getX(), randCircle.getY());
        //curve.setControlPoint1(randCircle.getX(), randCircle.getY());
        //curve.setControlPoint2(randCircle2.getX(), randCircle2.getY());
        //curve.setEndPoint(randCircle2.getX(), randCircle2.getY());
        //curve.reconstruct();

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
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
