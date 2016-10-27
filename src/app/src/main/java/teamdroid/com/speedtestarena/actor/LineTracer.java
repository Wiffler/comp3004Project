package teamdroid.com.speedtestarena.actor;

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

public class LineTracer {
    private volatile boolean ready = false;

    private volatile Path path;
    private Paint p;
    private Vector2f p1, p2;

    private ArrayList<Vector2f> pointList;

    public LineTracer() {
        p1 = new Vector2f(-1f, -1f);
        p2 = new Vector2f(-1f, -1f);

        path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);

        p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(15.0f);
        p.setAntiAlias(true);

        pointList = new ArrayList<Vector2f>();
    }

    // Setters
    public void set(float px, float py) {
        p2.set(px, py);
        pointList.add(new Vector2f(p2));
        ready = true;
    }

    // Getters
    public Path getPath() {
        return this.path;
    }

    public Paint getP() {
        return this.p;
    }

    // Updates
    public void eventUpdate(float px, float py) {
        if (MathUtil.distanceSquare(p2.x, p2.y, px, py) > 25) {
            p1.set(p2);
            p2.set(px, py);

            pointList.add(new Vector2f(p2));

            path.moveTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
        }
    }

    public void tickUpdate() {
        if (ready) {
            if (pointList.size() > 2) {
                pointList.remove(0);
                path.reset();

                for (int i = 0; i <= pointList.size() - 2; i++) {
                    path.moveTo(pointList.get(i).x, pointList.get(i).y);
                    path.lineTo(pointList.get(i + 1).x, pointList.get(i + 1).y);
                }
            }
        }
    }

    public void reset() {
        ready = false;

        path.reset();

        p1.set(-1f, -1f);
        p2.set(-1f, -1f);

        pointList.clear();
    }

    // Drawable
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    public void setAlpha(int arg0)
    {
    }

    public void setColorFilter(ColorFilter arg0)
    {
    }
}
