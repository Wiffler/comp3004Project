package teamdroid.com.speedtestarena.game.MusicGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import teamdroid.com.speedtestarena.graphics.Renderer;
import teamdroid.com.speedtestarena.graphics.BitmapLoader;
import teamdroid.com.speedtestarena.io.EventQueue;
import teamdroid.com.speedtestarena.io.MusicGameEvent;

/**
 * Created by Kenny on 2016-10-17.
 */

public class MusicGameView extends SurfaceView implements SurfaceHolder.Callback {

    public MusicGameActivity activity;

    public volatile boolean ready = false;

    public Renderer render;
    public BitmapLoader textures;
    public EventQueue<MusicGameEvent> events;

    private MusicGameMainThread gameThread;

    // Constructors
    public MusicGameView(Context context) {
        super(context);
        activity = (MusicGameActivity) context;
        getHolder().addCallback(this);
        //setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Set the background color
        setBackgroundColor(Color.BLACK);

        // Create the objects
        render = new Renderer();
        textures = new BitmapLoader();
        events = new EventQueue<MusicGameEvent>();

        // Create the game thread
        gameThread = new MusicGameMainThread(getHolder(), this,
                activity.getSongID(), activity.getSimID(), activity.getBGID());
        gameThread.setRunning(true);
        gameThread.start();

        // Allow the view to update
        setWillNotDraw(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                // wait for the threads to end
                gameThread.setRunning(false);
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                System.out.println("Thread exit failed.");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (ready) {
            drawGame(canvas);
        }
    }

    // surfaceChanged event is called when the screen is first created or rotated
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            System.out.println("Cannot draw. Canvas is null.");
        } else {
            this.draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (ready) {
            events.enqueue(new MusicGameEvent(event, gameThread.song.getPosition()));
        }
        return true;
    }

    private void drawGame(Canvas canvas) {
        gameThread.updateLock.lock();

        // Draw the game text
        render.render(canvas, gameThread.fpsText);
        render.render(canvas, gameThread.scoreText);
        render.render(canvas, gameThread.totalScoreText);
        render.render(canvas, gameThread.songDurationText);

        // Draw the hitcircles
        for (int i = 0; i < gameThread.hitcircleList.size(); i++) {
            if (gameThread.hitcircleList.get(i).active) {
                render.render(canvas, gameThread.hitcircleList.get(i));
            }
        }

        // Draw the quit button
        render.render(canvas, gameThread.quitButton);

        // Draw the cursor
        render.render(canvas, gameThread.trace);
        gameThread.updateLock.unlock();

        ready = true;
    }
}
