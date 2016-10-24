package teamdroid.com.speedtestarena.actor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-19.
 */

public class CubicBezier {
    private Vector2f c1, c2, c3, c4;
    private Path path;
    private Paint p;

    public CubicBezier(float cx1, float cy1, float cx2, float cy2, float cx3, float cy3, float cx4, float cy4) {
        c1 = new Vector2f(cx1, cy1);
        c2 = new Vector2f(cx2, cy2);
        c3 = new Vector2f(cx3, cy3);
        c4 = new Vector2f(cx4, cy4);

        path = new Path();
        path.moveTo(cx1, cy1);
        path.cubicTo(cx2, cy2, cx3, cy3, cx4, cy4);

        p = new Paint();
        p.setColor(Color.parseColor("#FFFFFF"));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(3.0f);
        p.setAntiAlias(true);
    }

    // Setters
    public void setControlPoints(float cx1, float cy1, float cx2, float cy2, float cx3, float cy3, float cx4, float cy4) {
        c1.set(cx1, cy1);
        c2.set(cx2, cy2);
        c3.set(cx3, cy3);
        c4.set(cx4, cy4);

        path.moveTo(cx1, cy1);
        path.cubicTo(cx2, cy2, cx3, cy3, cx4, cy4);
    }

    public void setStartPoint(float x, float y) {
        c1.set(x, y);
    }

    public void setControlPoint1(float x, float y) {
        c2.set(x, y);
    }

    public void setControlPoint2(float x, float y) {
        c3.set(x, y);
    }

    public void setEndPoint(float x, float y) {
        c4.set(x, y);
    }

    public void reconstruct() {
        path.reset();
        path.moveTo(c1.getX(), c1.getY());
        path.cubicTo(c2.getX(), c2.getY(), c3.getX(), c3.getY(), c4.getX(), c4.getY());
    }

    // Getters
    public Path getPath() {
        return this.path;
    }

    public Paint getP() {
        return this.p;
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
