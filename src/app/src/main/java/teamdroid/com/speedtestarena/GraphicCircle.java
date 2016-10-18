package teamdroid.com.speedtestarena;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GraphicCircle extends Drawable {
    private float centerx;
    private float centery;
    private float radius;
    private Paint p;

    public GraphicCircle(float x, float y, float r, String c) {
        centerx = x;
        centery = y;
        radius = r;

        p = new Paint();
        p.setColor(Color.parseColor(c));
    }

    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
        System.out.println(c);
    }

    public void setCenter(float px, float py) {
        centerx = px;
        centery = py;
    }

    public boolean inCircle(float px, float py) {
        if ((centerx - px) * (centerx - px) + (centery - py) * (centery - py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
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
        canvas.drawCircle(centerx, centery, radius, p);
    }
}
