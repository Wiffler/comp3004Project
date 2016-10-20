package teamdroid.com.speedtestarena.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Kenny on 2016-10-19.
 */

public class GraphicCubicBezier extends Drawable {

    private Path path;
    private Paint p;

    public GraphicCubicBezier(float cx1, float cy1, float cx2, float cy2, float cx3, float cy3, float cx4, float cy4) {
        path = new Path();
        path.moveTo(cx1, cy1);
        path.cubicTo(cx2, cy2, cx3, cy3, cx4, cy4);

        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(3.0f);
        p.setAntiAlias(true);
    }

    public void setControlPoints(float cx1, float cy1, float cx2, float cy2, float cx3, float cy3, float cx4, float cy4) {
        path.moveTo(cx1, cy1);
        path.cubicTo(cx2, cy2, cx3, cy3, cx4, cy4);
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
