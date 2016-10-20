package teamdroid.com.speedtestarena.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import teamdroid.com.speedtestarena.math.MathUtil;
import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-19.
 */

public class Tracer extends Drawable {

    private Path path;
    private Paint p;
    private Vector2f p1, p2;
    private volatile boolean ready = false;

    private ArrayList<Vector2f> pointList;

    private long t0;
    private long t1;

    public Tracer() {
        p1 = new Vector2f(-1f, -1f);
        p2 = new Vector2f(-1f, -1f);

        path = new Path();
        path.moveTo(p1.getX(), p1.getY());
        path.lineTo(p2.getX(), p2.getY());

        p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(15.0f);
        p.setAntiAlias(true);

        pointList = new ArrayList<Vector2f>();
    }

    public void set(float px, float py) {
        p2.set(px, py);

        pointList.add(new Vector2f(p2));

        t0 = System.currentTimeMillis();

        ready = true;
    }

    public void eventUpdate(float px, float py) {
        if (MathUtil.distanceSquare(p2.getX(), p2.getY(), px, py) > 25) {
            p1.set(p2);
            p2.set(px, py);

            pointList.add(new Vector2f(p2));

            path.moveTo(p1.getX(), p1.getY());
            path.lineTo(p2.getX(), p2.getY());
            //System.out.println(px1 + " " + py1 + " " + px2 + " " + py2);
        }
    }

    public void tickUpdate() {
        if (ready) {
            t1 = System.currentTimeMillis();

            if ((t1 - t0 > 25) && (pointList.size() > 2)) {
                t0 = t1;

                pointList.remove(0);
                path.reset();

                //System.out.println("Removing...");

                for (int i = 0; i <= pointList.size() - 2; i++) {
                    path.moveTo(pointList.get(i).getX(), pointList.get(i).getY());
                    path.lineTo(pointList.get(i + 1).getX(), pointList.get(i + 1).getY());
                }
            }
        }
    }

    public void reset() {
        ready = false;

        path.reset();

        p1.set(-1f, -1f);
        p2.set(-1f, -1f);

        t0 = 0;
        t1 = 0;

        pointList.clear();
    }

    // Drawable
    @Override
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int arg0)
    {
    }

    @Override
    public void setColorFilter(ColorFilter arg0)
    {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, p);
    }
}
