package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import teamdroid.com.speedtestarena.graphics.Renderer;
import teamdroid.com.speedtestarena.graphics.BitmapLoader;
import teamdroid.com.speedtestarena.io.EventQueue;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1 extends SurfaceView implements SurfaceHolder.Callback {

    public volatile boolean ready = false;

    public Renderer render;
    public BitmapLoader textures;
    public EventQueue events;

    private GameTest1MainThread gameThread;
    public static Context activity;

    public GameTest1(Context context) {
        super(context);
        activity = context;
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Create the objects
        render = new Renderer();
        textures = new BitmapLoader();
        events = new EventQueue();

        // Create the game thread
        gameThread = new GameTest1MainThread(getHolder(), this);
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
                gameThread.join();
                retry = false;

            } catch (InterruptedException e) {
                System.out.println("Thread exit failed.");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRGB(0, 0, 0);

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
            events.enqueue(event);
        }
        return true;
    }

    private void drawGame(Canvas canvas) {
        render.render(canvas, gameThread.curve);
        render.render(canvas, gameThread.randCircle);
        render.render(canvas, gameThread.randCircle2);

        render.render(canvas, gameThread.trace);

        for (int i = 0; i < gameThread.particleList.size(); i++) {
            render.render(canvas, gameThread.particleList.get(i));
        }

        render.render(canvas, gameThread.tickText);
        render.render(canvas, gameThread.scoreText);

        ready = true;
    }
}
