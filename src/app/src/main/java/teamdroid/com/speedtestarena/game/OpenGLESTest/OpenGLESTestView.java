package teamdroid.com.speedtestarena.game.OpenGLESTest;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import teamdroid.com.speedtestarena.game.GameTest1.GameTest1MainThread;
import teamdroid.com.speedtestarena.graphics.BitmapLoader;
import teamdroid.com.speedtestarena.graphics.OpenGLES.GLRenderer;
import teamdroid.com.speedtestarena.io.EventQueue;

/**
 * Created by Kenny on 2016-10-17.
 */

public class OpenGLESTestView extends GLSurfaceView {

    public volatile boolean ready = false;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public GLRenderer render;
    public BitmapLoader textures;
    public EventQueue events;

    private GameTest1MainThread gameThread;
    public static Context activity;

    public OpenGLESTestView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        activity = context;

        render = new GLRenderer();
        setRenderer(render);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //getHolder().addCallback(this);
        //setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (e.getY() > getHeight() - 50) {
                    //gameThread.setRunning(false);
                    ((Activity) getContext()).finish();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                render.setAngle(
                        render.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    /*
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
    */
}
