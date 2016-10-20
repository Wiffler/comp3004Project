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
        setTextSizeForWidth(p, 100f, 100f, text);
    }

    // Setters
    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
    }

    public void setText(String t) {
        this.text = t;
        setTextSizeForWidth(p, 100f, 50f, text);
    }

    public void setPosition(float px, float py) {
        locx = px;
        locy = py;
    }

    // Getters
    public String getText() {
        return this.text;
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

    private static void setTextSizeForWidth(Paint paint, float desiredWidth, float textSize, String text) {
        /*
        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = textSize * desiredWidth / bounds.width();
        */

        // Set the paint for that size.
        paint.setTextSize(textSize);
    }

    // Drawing
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
