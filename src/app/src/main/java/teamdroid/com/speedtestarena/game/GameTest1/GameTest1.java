package teamdroid.com.speedtestarena.game.GameTest1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import teamdroid.com.speedtestarena.graphics.Renderer;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GameTest1 extends SurfaceView implements SurfaceHolder.Callback {

    public volatile boolean ready = false;

    private Renderer render;
    private GameTest1MainThread gameThread;

    public GameTest1(Context context) {
        super(context);
        getHolder().addCallback(this);

        // create the game threads
        render = new Renderer();
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                gameThread.setRunning(false);
                ((Activity) getContext()).finish();

            } else {
                System.out.println("Coords: x=" + event.getX() + ", y=" + event.getY());

                if (gameThread.circles[0].inCircle(event.getX(), event.getY())) {
                    gameThread.score += 1;
                    System.out.println(gameThread.score);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void drawGame(Canvas canvas) {
        canvas.drawRGB(255, 255, 255);

        for (int i = 0; i < gameThread.circles.length; i++) {
            render.render(canvas, gameThread.circles[i]);
        }

        render.render(canvas, gameThread.tickText);
        render.render(canvas, gameThread.scoreText);

        ready = true;
    }
}
