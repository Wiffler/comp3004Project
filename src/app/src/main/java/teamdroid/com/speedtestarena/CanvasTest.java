package teamdroid.com.speedtestarena;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import teamdroid.com.speedtestarena.graphics.GraphicCircle;
import teamdroid.com.speedtestarena.graphics.GraphicCubicBezier;
import teamdroid.com.speedtestarena.graphics.GraphicText;
import teamdroid.com.speedtestarena.graphics.Tracer;

/**
 * Created by Kenny on 2016-10-11.
 */

public class CanvasTest extends SurfaceView implements SurfaceHolder.Callback {

    private CanvasTestMainThread gameThread;
    private CanvasTestAudioThread audioThread;
    private CanvasTestSoundPoolThread soundPoolThread;

    // objects
    private GraphicCircle pauseCircle, startCircle, pauseCircle2, startCircle2;
    public GraphicText tickText;
    private GraphicCubicBezier curve1;
    public Tracer trace;

    public CanvasTest(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        // Create the game threads
        gameThread = new CanvasTestMainThread(getHolder(), this);
        audioThread = new CanvasTestAudioThread(context);
        soundPoolThread =  new CanvasTestSoundPoolThread(context);

        // Create the objects
        pauseCircle = new GraphicCircle(0, 0, 100, "#C0C0C0");
        startCircle = new GraphicCircle(0, 0, 100, "#008000");
        pauseCircle2 = new GraphicCircle(0, 0, 100, "#C0C0C0");
        startCircle2 = new GraphicCircle(0, 0, 100, "#008000");
        tickText = new GraphicText(0, 0, "", "#C0C0C0");
        curve1 = new GraphicCubicBezier(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        trace = new Tracer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Set the state of the threads
        gameThread.setRunning(true);
        audioThread.setRunning(true);
        soundPoolThread.setRunning(true);

        // Start the threads
        gameThread.start();
        audioThread.start();
        soundPoolThread.start();

        // Allow the view to update
        setWillNotDraw(false);

        // Set the objects
        pauseCircle.setCenter(getWidth() / 2, getHeight() / 2);
        startCircle.setCenter(getWidth() / 2, (getHeight() / 2) - 250);
        pauseCircle2.setCenter((getWidth() / 2) + 250, getHeight() / 2);
        startCircle2.setCenter((getWidth() / 2) + 250, (getHeight() / 2) - 250);
        tickText.setPosition(getWidth() / 4, getHeight() / 4);

        curve1.setControlPoints(0, 0, getWidth() / 2 - 250, getHeight() / 2, getWidth() / 2 + 250, getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                // wait for the threads to end
                gameThread.join();
                audioThread.join();
                soundPoolThread.join();

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
        drawGame(canvas);
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
                audioThread.setRunning(false);
                soundPoolThread.setRunning(false);
                ((Activity) getContext()).finish();

            } else {
                System.out.println("Coords: x=" + event.getX() + ", y=" + event.getY());

                if (startCircle.inCircle(event.getX(), event.getY())) {
                    soundPoolThread.startAudio(R.raw.metronome1_sound_file);
                    pauseCircle.setColor("#CD5C5C");
                }

                if (pauseCircle.inCircle(event.getX(), event.getY())) {
                    soundPoolThread.pauseAudio(R.raw.metronome1_sound_file);
                    pauseCircle.setColor("#C0C0C0");
                }

                if (startCircle2.inCircle(event.getX(), event.getY())) {
                    audioThread.startAudio();
                    pauseCircle2.setColor("#CD5C5C");

                }

                if (pauseCircle2.inCircle(event.getX(), event.getY())) {
                    audioThread.pauseAudio();
                    pauseCircle2.setColor("#C0C0C0");
                }

                trace.set(event.getX(), event.getY());
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            trace.eventUpdate(event.getX(), event.getY());

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            trace.reset();

        }

        return true;
    }

    private void drawGame(Canvas canvas) {
        canvas.drawRGB(100, 100, 255);

        curve1.draw(canvas);

        pauseCircle.draw(canvas);
        startCircle.draw(canvas);
        pauseCircle2.draw(canvas);
        startCircle2.draw(canvas);

        trace.draw(canvas);

        tickText.draw(canvas);
    }
}
