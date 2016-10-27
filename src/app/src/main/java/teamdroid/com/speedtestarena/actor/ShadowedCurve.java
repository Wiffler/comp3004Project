package teamdroid.com.speedtestarena.actor;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-24.
 */

public class ShadowedCurve {
    private Vector2f c1, c2, c3, c4;
    private Path path;
    private Paint shadow, solid;

    public ShadowedCurve(float cx1, float cy1, float cx2, float cy2,
                         float cx3, float cy3, float cx4, float cy4) {
        c1 = new Vector2f(cx1, cy1);
        c2 = new Vector2f(cx2, cy2);
        c3 = new Vector2f(cx3, cy3);
        c4 = new Vector2f(cx4, cy4);

        path = new Path();
        path.moveTo(cx1, cy1);
        path.cubicTo(cx2, cy2, cx3, cy3, cx4, cy4);

        solid = new Paint();
        solid.setAntiAlias(true);
        solid.setColor(Color.argb(248, 255, 255, 255));
        solid.setStrokeWidth(15f);
        solid.setStyle(Paint.Style.STROKE);
        solid.setStrokeJoin(Paint.Join.ROUND);
        solid.setStrokeCap(Paint.Cap.ROUND);

        shadow = new Paint();
        shadow.set(solid);
        shadow.setColor(Color.argb(235, 74, 138, 255));
        shadow.setStrokeWidth(40f);
    }

    // Setters
    public void setControlPoints(float cx1, float cy1, float cx2, float cy2,
                                 float cx3, float cy3, float cx4, float cy4) {
        c1.set(cx1, cy1);
        c2.set(cx2, cy2);
        c3.set(cx3, cy3);
        c4.set(cx4, cy4);

        path.reset();
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
        path.moveTo(c1.x, c1.y);
        path.cubicTo(c2.x, c2.y, c3.x, c3.y, c4.x, c4.y);
    }

    // Getters
    public Path getPath() {
        return this.path;
    }

    public Paint getShadowPaint() {
        return this.shadow;
    }
    public Paint getSolidPaint() {
        return this.solid;
    }
}
