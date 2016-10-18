package teamdroid.com.speedtestarena;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Kenny on 2016-10-17.
 */

public class GraphicText {
    private float locx;
    private float locy;
    private String text;
    private int color;

    public GraphicText(float x, float y, String t, String c) {
        locx = x;
        locy = y;
        text = t;
        color = Color.parseColor(c);
    }

    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        this.color = Color.parseColor(c);
    }

    public void setText(String t) {
        this.text = t;
    }

    private static void setTextSizeForWidth(Paint paint, float desiredWidth, float textSize, String text) {
        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = textSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        setTextSizeForWidth(paint, 100f, 48f, text);
        canvas.drawText(text, locx, locy, paint);
    }
}
