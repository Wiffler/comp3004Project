package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.actor.Button;
import teamdroid.com.speedtestarena.actor.HitCircle;
import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.graphics.Background;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.graphics.Renderer;
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
    // Constants
    private static int HITCIRCLE_MAX = 64;

    // Locks
    public final Lock updateLock = new ReentrantLock(true);

    // Flag to hold the game state
    private volatile boolean running = false;

    // Track if redraw is needed
    private volatile boolean dirty = false;

    // FPS
    public volatile int fps = 0;

    // Score
    public volatile int score = 0; // might create a player actor instead

    // UI, Audio and Timer objects
    private SurfaceHolder surfaceHolder;
    private GameTest1 gamePanel;
    public GameAudio song;
    public GameTimer timer;
    public AudioDelayThread audioDelayThread;

    // Actors and graphics objects
    public Text scoreText, fpsText;
    public Button quitButton;
    public ParticleTracer trace;
    public HitMap mapper;
    public Background bg;

    // List(s) to hold actors
    public volatile ArrayList<HitCircle> hitcircleList;

    // Resource IDs
    int songID, simfileID, bgID;

    // Constructor(s)
    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        this.songID = 0;
        this.simfileID = 0;
        this.bgID = 0;

        timer = new GameTimer();
    }

    public GameTest1MainThread(SurfaceHolder surfaceHolder, GameTest1 gamePanel,
                               int songID, int simfileID, int bgID) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

        this.songID = songID;
        this.simfileID = simfileID;
        this.bgID = bgID;

        timer = new GameTimer();
    }

    // Set and check the running state
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean isRunning() { return this.running; }

    // Initialises the objects
    private void initialise() {
        // Load the textures
        int[] idList = {R.drawable.cursor, R.drawable.cursortrail};
        gamePanel.render.loadBitmaps(gamePanel.activity, idList);
        gamePanel.render.loadBitmap(gamePanel.activity, R.drawable.hitcircle2,
                                    100, 100, false);
        gamePanel.render.loadBitmap(gamePanel.activity, R.drawable.hitcircleoverlay,
                                    200, 200, false);
        gamePanel.render.loadBitmap(gamePanel.activity, R.drawable.test_sound_file2_bg,
                                    gamePanel.getWidth(), gamePanel.getHeight(), true);
        gamePanel.render.loadBitmap(gamePanel.activity, R.drawable.star,
                                    100, 100, true);

        // Set the background drawable if id is valid
        if (bgID != 0) {
            bg = new Background(gamePanel.activity, bgID, gamePanel.getWidth(), gamePanel.getHeight());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gamePanel.setBackground(bg);
            } else {
                gamePanel.setBackgroundDrawable(bg);
            }
        }

        // Setup the cursor, button and text
        scoreText = new Text(50, 50, "Score: 0", "#FFFFFF");
        fpsText = new Text(50, 100, "FPS:", "#FFFFFF");
        quitButton = new Button(R.drawable.star, gamePanel.getWidth() - 100, 0);
        trace = new ParticleTracer();

        // Setup the hitcircle objects
        hitcircleList = new ArrayList<HitCircle>(HITCIRCLE_MAX);
        for (int i = 0; i < HITCIRCLE_MAX; i++) {
            hitcircleList.add(new HitCircle(R.drawable.hitcircle2, 0, 0, 0, 0, 0));
        }

        // Setup the audio
        audioDelayThread = new AudioDelayThread();
        song = new GameAudio(timer);
        song.createAudio(gamePanel.activity, songID);

        // Setup the audio mapping
        //mapper = new HitMap(gamePanel.getWidth(), gamePanel.getHeight(), Renderer.getBitmapWidth(R.drawable.hitcircle2));
        mapper = new HitMap(gamePanel.getHeight(), gamePanel.getWidth(), Renderer.getBitmapWidth(R.drawable.hitcircle2));
        mapper.initialise(gamePanel.activity, simfileID);

        // Set redraw flag
        dirty = true;
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
                    if (quitButton.inButton(e.getX(), e.getY())) {
                        this.setRunning(false);
                        ((Activity) gamePanel.getContext()).finish();

                    } else {
                        //System.out.println("Coords: x=" + e.getX() + ", y=" + e.getY());

                        // Update the hitcircles
                        HitCircle h;
                        for (int i = 0; i < hitcircleList.size(); i++) {
                            h = hitcircleList.get(i);
                            if (h.active) {
                                if (h.inCircle(e.getX(), e.getY())) {
                                    score += (int) (100f * min(1 - (((float) abs(gameEvent.songTime - h.getBeatTime())) / 1000f), 1f));
                                    scoreText.setText("Score: " + score);
                                    h.active = false;
                                }
                            }
                        }

                        trace.set(e.getX(), e.getY());
                        dirty = true;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    trace.eventUpdate(e.getX(), e.getY());
                    dirty = true;
                    break;

                case MotionEvent.ACTION_UP:
                    trace.reset();
                    dirty = true;
                    break;
            }
        }
    }

    // Updates the state of the objects every tick
    private void updateState(long curTime) {
        // Update the hitcircles
        HitCircle h;
        for (int i = 0; i < hitcircleList.size(); i++) {
            h = hitcircleList.get(i);
            if (h.active) {
                if (!h.update(song.getPosition())) {
                    h.active = false;
                }
                dirty = true;
            }
        }

        // Spawn the hitcircles
        HitInfo info = null;
        if (mapper.spawnTimeIndex < mapper.spawnInfoList.size()) {
            info = mapper.spawnInfoList.get(mapper.spawnTimeIndex);

            int index = 0;
            while ((info != null) && (info.spawnTime <= song.getPosition())) {
                //System.out.println("SPTIME: " + spTime + " SONG POSITION: " + song.getPosition() + " SPAWN INDEX: " + mapper.spawnTimeIndex + " Size: " + mapper.hitcircleSpawnTimes.size());

                for (int i = index; i < hitcircleList.size(); i++) {
                    h = hitcircleList.get(i);
                    if (!h.active) {
                        /*
                        h.activate(mapper.spawnLoc[0][info.spawnLocation],
                                   mapper.spawnLoc[1][info.spawnLocation],
                                   info.spawnTime, info.deathTime, info.beatTime);
                                   */
                        h.activate(mapper.spawnLoc[1][info.spawnLocation],
                                   mapper.spawnLoc[0][info.spawnLocation],
                                   info.spawnTime, info.deathTime, info.beatTime);
                        index = i + 1;
                        break;
                    }
                }

                mapper.spawnTimeIndex = mapper.spawnTimeIndex + 1;

                if (mapper.spawnTimeIndex < mapper.spawnInfoList.size()) {
                    info = mapper.spawnInfoList.get(mapper.spawnTimeIndex);
                } else {
                    info = null;
                }
            }
        }

        // Update the tracer particles
        dirty = trace.update() || dirty;
    }

    @Override
    public void run() {
        long prevTime = 0;
        int frames_done = 0;

        // Start the timer
        timer.setRunning(true);
        timer.start();

        // create and initialise the objects
        initialise();

        // Set the ready state for the UI
        gamePanel.ready = true;

        // Start the audio
        audioDelayThread.start();
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

                // handle the IO events and game state updates
                updateLock.lock();
                handleIO();
                updateState(timer.getTime());
                updateLock.unlock();

                timer.decrementTicks();
                if(old_ticks <= timer.getTicks()) {
                    break;
                }
            }

            if (timer.getTime() - prevTime >= 1000) {
                // fps now holds the the number of frames done in the last second
                updateLock.lock();
                fps = frames_done;
                fpsText.setText("FPS: " + fps);
                updateLock.unlock();

                // reset for the next second
                frames_done = 0;
                prevTime = timer.getTime();
            }

            // draw a frame
            if (dirty) {
                gamePanel.postInvalidate();
                frames_done++;
                dirty = false;
            }
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
