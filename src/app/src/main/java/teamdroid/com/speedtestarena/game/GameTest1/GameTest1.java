package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.graphics.Renderer;
import teamdroid.com.speedtestarena.graphics.TextureLoader;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1 extends SurfaceView implements SurfaceHolder.Callback {

    public volatile boolean ready = false;

    private Renderer render;
    public TextureLoader textures;
    private GameTest1MainThread gameThread;

    public GameTest1(Context context) {
        super(context);
        getHolder().addCallback(this);

        // Create the objects
        render = new Renderer();
        textures = new TextureLoader();
        // Load the textures
        textures.loadTexture(context, R.drawable.cursor);
        textures.loadTexture(context, R.drawable.cursortrail);

        // Create the game thread
        gameThread = new GameTest1MainThread(getHolder(), this, context);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Set the state of the threads
        gameThread.setRunning(true);

        // Start the threads
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
                // try again shutting down the thread
                System.out.println("Thread exit failed.");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gameThread.isRunning()) {
            drawGame(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            System.out.println("Cannot draw. Canvas is null.");
        } else {
            drawGame(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        gameThread.events.enqueue(event);

        return true;
    }

    private void drawGame(Canvas canvas) {
        canvas.drawRGB(0, 0, 0);

        render.render(canvas, gameThread.randCircle);
        render.render(canvas, gameThread.tickText);
        render.render(canvas, gameThread.scoreText);

        render.render(canvas, gameThread.trace);

        for (int i = 0; i < gameThread.particleList.size(); i++) {
            render.render(canvas, gameThread.particleList.get(i));
        }

        ready = true;
    }
}
