package teamdroid.com.speedtestarena.graphics;

import android.graphics.Canvas;

import teamdroid.com.speedtestarena.actor.Circle;
import teamdroid.com.speedtestarena.actor.CubicBezier;
import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.actor.Tracer;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Renderer {

    public Renderer() {}

    public static void render(Canvas canvas, Text obj) {
        canvas.drawText(obj.getText(), obj.getX(), obj.getY(), obj.getP());
    }

    public static void render(Canvas canvas, Circle obj) {
        canvas.drawCircle(obj.getX(), obj.getY(), obj.getR(), obj.getP());
    }

    public static void render(Canvas canvas, CubicBezier obj) {
        canvas.drawPath(obj.getPath(), obj.getP());
    }

    public static void render(Canvas canvas, Tracer obj) {
        canvas.drawPath(obj.getPath(), obj.getP());
    }

    public static void render(Canvas canvas, ParticleTracer obj) {
        if (obj.isActive()) {
            render(canvas, obj.getTex());
        }
    }

    public static void render(Canvas canvas, Texture obj) {
        //canvas.drawBitmap(obj.getTex(), obj.getMatrix(), null);
        canvas.drawBitmap(obj.getTex(), obj.getX(), obj.getY(), obj.getP());
    }

    public static void render(Canvas canvas, Particle obj) {
        render(canvas, obj.getTex());
    }
}
