package teamdroid.com.speedtestarena;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;


/**
 * Created by Kenny on 2016-10-11.
 */

public class CanvasTest extends SurfaceView implements SurfaceHolder.Callback {

    private CanvasTestMainThread thread;

    public CanvasTest(Context context) {
        super(context);
        getHolder().addCallback(this);

        // create the game loop thread
        thread = new CanvasTestMainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                thread.setRunning(false);
                ((Activity) getContext()).finish();
            } else {
                //Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
                System.out.println("Coords: x=" + event.getX() + ", y=" + event.getY());
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}
