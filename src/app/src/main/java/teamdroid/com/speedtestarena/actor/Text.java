package teamdroid.com.speedtestarena.actor;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Kenny on 2016-10-17.
 */

public class Text {
    private float locx;
    private float locy;
    private String text;
    private Paint p;

    public Text(float x, float y, String t, String c) {
        locx = x;
        locy = y;
        text = t;

        p = new Paint();
        p.setColor(Color.parseColor(c));
        p.setLinearText(true);
        p.setTextSize(50f);
    }

    // Setters
    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
    }

    public void setText(String t) {
        text = t;
    }

    public void setPosition(float px, float py) {
        locx = px;
        locy = py;
    }

    // Getters
    public String getText() {
        return text;
    }

    public float getX() {
        return locx;
    }

    public float getY() {
        return locy;
    }

    public Paint getP() {
        return p;
    }
}
