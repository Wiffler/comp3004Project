package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.actor.HitCircle;
import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.io.GameTest1Event;
import teamdroid.com.speedtestarena.sound.AudioDelayThread;
import teamdroid.com.speedtestarena.sound.GameAudio;
import teamdroid.com.speedtestarena.utility.GameTimer;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.min;


/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1MainThread extends Thread {
    // Locks
    public final Lock hitCircleMutex = new ReentrantLock(true);
    public final Lock particleMutex = new ReentrantLock(true);

    // Flag to hold the game state
    private volatile boolean running = false;

    // Score
    public int score = 0; // might create a player actor instead

    // UI, Audio and Timer
    private SurfaceHolder surfaceHolder;
    private GameTest1 gamePanel;
    public GameAudio song;
    public GameTimer timer;
    public AudioDelayThread audioDelayThread;

    // Actors
    public Text scoreText;
    public Text tickText;
    public ParticleTracer trace;
    public HitMap mapper;
    public Texture bg;

    // Lists to hold actors
    public ArrayList<Particle> particleList;
    public volatile ArrayList<HitCircle> hitcircleList;

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

    // Instantiates the objects
    private void create() {
        // Load the textures
        int[] idList = {R.drawable.cursor, R.drawable.cursortrail,
                        R.drawable.hitcircle, R.drawable.hitcircleoverlay};
        gamePanel.render.loadBitmaps(gamePanel.activity, idList);
        gamePanel.render.loadBitmap(gamePanel.activity, R.drawable.test_sound_file2_bg,
                                    gamePanel.getWidth(), gamePanel.getHeight(), true);

        // Create the objects
        audioDelayThread = new AudioDelayThread();
        song = new GameAudio(timer);
        song.createAudio(gamePanel.activity, R.raw.test_sound_file2);

        scoreText = new Text(0, 0, "Score: " + score, "#FFFFFF");
        tickText = new Text(0, 0, "Interval: ", "#FFFFFF");
        trace = new ParticleTracer(gamePanel.textures, this);

        particleList = new ArrayList<Particle>();
        hitcircleList = new ArrayList<HitCircle>();
        mapper = new HitMap();

        bg = new Texture(R.drawable.test_sound_file2_bg, 0, 0, 255, null);
    }

    // Initialises the objects
    private void initialise() {
        // Setup the objects
        bg.setTranslation(0, (gamePanel.getHeight() - bg.getHeight()) / 2);
        bg.recomputeCoordinateMatrix();

        scoreText.setPosition(50, 50);
        tickText.setPosition(50, 100);

        mapper.initialise(gamePanel.activity);
    }

    // Cleanup threads and resources when game ends
    private void cleanup() {
        song.stopAudio();
        song.cleanup();
    }

    // Processes the event queue
    private void handleIO() {
        GameTest1Event gameEvent;
        MotionEvent e;

        while (gamePanel.events.size() > 0) {
            gameEvent = gamePanel.events.dequeue();
            e = gameEvent.e;

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (e.getY() > gamePanel.getHeight() - 50) {
                        this.setRunning(false);
                        ((Activity) gamePanel.getContext()).finish();

                    } else {
                        System.out.println("Coords: x=" + e.getX() + ", y=" + e.getY());

                        // Update the hitcircles
                        hitCircleMutex.lock();
                        for (Iterator<HitCircle> iterator = hitcircleList.iterator(); iterator.hasNext(); ) {
                            HitCircle h = iterator.next();
                            if (h.inCircle(e.getX(), e.getY())) {
                                score += (int) (100f * min(1 - (((float) abs(gameEvent.songTime - h.getBeatTime())) / 1000f), 1f));
                                iterator.remove();
                            }
                        }
                        hitCircleMutex.unlock();

                        trace.set(e.getX(), e.getY());
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    trace.eventUpdate(e.getX(), e.getY());
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
        hitCircleMutex.lock();
        for (Iterator<HitCircle> iterator = hitcircleList.iterator(); iterator.hasNext(); ) {
            HitCircle h = iterator.next();
            if (!h.update(song.getPosition())) {
                iterator.remove();
            }
        }

        // Spawn the hitcircles
        HitInfo info = null;
        if (mapper.spawnTimeIndex < mapper.spawnInfoList.size()) {
            info = mapper.spawnInfoList.get(mapper.spawnTimeIndex);

            //spTime = mapper.hitcircleSpawnTimes.get(mapper.spawnTimeIndex);
            while ((info != null) && (info.spawnTime <= song.getPosition())) {
                //System.out.println("SPTIME: " + spTime + " SONG POSITION: " + song.getPosition() + " SPAWN INDEX: " + mapper.spawnTimeIndex + " Size: " + mapper.hitcircleSpawnTimes.size());

                // Create new hit circle
                hitcircleList.add(new HitCircle(
                        R.drawable.hitcircleoverlay,
                        mapper.spawnLocX[info.spawnLocation],
                        mapper.spawnLocY[info.spawnLocation],
                        info.spawnTime,
                        info.deathTime,
                        info.beatTime));

                mapper.spawnTimeIndex = mapper.spawnTimeIndex + 1;

                //System.out.println("SPAWN INDEX: " + mapper.spawnTimeIndex);
                if (mapper.spawnTimeIndex < mapper.spawnInfoList.size()) {
                    info = mapper.spawnInfoList.get(mapper.spawnTimeIndex);
                } else {
                    info = null;
                }
            }
        }
        hitCircleMutex.unlock();

        // Update the score text
        scoreText.setText("Score: " + score);

        // Update the particle list
        particleMutex.lock();
        for (Iterator<Particle> iterator = particleList.iterator(); iterator.hasNext(); ) {
            Particle p = iterator.next();
            if (p.getAlpha() <= 0) {
                iterator.remove();
            } else {
                p.update();
            }
        }
        particleMutex.unlock();
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
        audioDelayThread.start();
        /*
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        while (!audioDelayThread.ready) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        song.startAudio(5000, audioDelayThread.delayHandler);

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
