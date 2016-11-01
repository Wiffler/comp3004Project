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
    private Paint p, stkPaint;

    public Text(float x, float y, String t, int c) {
        locx = x;
        locy = y;
        text = t;

        // Setup the paint
        p = new Paint();
        p.setColor(c);
        p.setTextSize(35f);

        stkPaint = new Paint();
        stkPaint.setStyle(Paint.Style.STROKE);
        stkPaint.setTextSize(35f);
        stkPaint.setStrokeWidth(8);
        stkPaint.setColor(Color.BLACK);
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

    public Paint getStrokePaint() { return stkPaint; }
}
