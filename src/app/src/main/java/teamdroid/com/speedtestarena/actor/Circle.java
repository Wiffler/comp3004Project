package teamdroid.com.speedtestarena.actor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Kenny on 2016-10-17.
 */

public class Circle {
    private float centerx;
    private float centery;
    private float radius;
    private Paint p;

    private long prevTime = 0;

    public Circle(float x, float y, float r, String c) {
        centerx = x;
        centery = y;
        radius = r;

        p = new Paint();
        p.setColor(Color.parseColor(c));
    }

    // Setters
    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
        System.out.println(c);
    }

    public void setCenter(float px, float py) {
        centerx = px;
        centery = py;
    }

    // Getters
    public float getX() {
        return this.centerx;
    }

    public float getY() {
        return this.centery;
    }

    public float getR() {
        return this.radius;
    }

    public Paint getP() {
        return this.p;
    }

    public boolean inCircle(float px, float py) {
        if ((centerx - px) * (centerx - px) + (centery - py) * (centery - py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
    }

    public void update(long curTime, float x, float y) {
        if (curTime - prevTime > 2000) {
            centerx = x;
            centery = y;
            prevTime = curTime;
        }
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
