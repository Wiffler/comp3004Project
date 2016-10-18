package teamdroid.com.speedtestarena;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GraphicCircle {
    private float centerx;
    private float centery;
    private float radius;
    //private volatile int color;
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

    public boolean inCircle(float px, float py) {
        if ((centerx - px) * (centerx - px) + (centery - py) * (centery - py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        //p.setColor(color);
        canvas.drawCircle(centerx, centery, radius, p);
    }
}
