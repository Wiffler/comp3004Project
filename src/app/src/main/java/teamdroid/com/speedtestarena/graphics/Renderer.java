package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Canvas;

import teamdroid.com.speedtestarena.actor.Button;
import teamdroid.com.speedtestarena.actor.HitCircleNeonOverlay;
import teamdroid.com.speedtestarena.actor.Particle;
import teamdroid.com.speedtestarena.actor.ShadowedCurve;
import teamdroid.com.speedtestarena.actor.Circle;
import teamdroid.com.speedtestarena.actor.CubicBezier;
import teamdroid.com.speedtestarena.actor.HitCircle;
import teamdroid.com.speedtestarena.actor.ParticleTracer;
import teamdroid.com.speedtestarena.actor.Text;
import teamdroid.com.speedtestarena.actor.LineTracer;
import teamdroid.com.speedtestarena.actor.HitCircleOverlay;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Renderer {

    private static BitmapLoader bitmaps = new BitmapLoader();

    // Constructor
    public Renderer() {
    }

    // Bitmap loading
    public static void loadBitmaps(Context context, int[] idList) {
        bitmaps.loadBitmapList(context, idList);
    }

    public static void loadBitmap(Context context, int id) {
        bitmaps.loadBitmap(context, id);
    }

    public static void loadBitmap(Context context, int id, int desW, int desH, boolean adjRatio) {
        bitmaps.loadBitmap(context, id, desW, desH, adjRatio);
    }

    public static float getBitmapHeight(int id) {
        return bitmaps.getBitmap(id).getHeight();
    }

    public static float getBitmapWidth(int id) {
        return bitmaps.getBitmap(id).getWidth();
    }

    // Actor rendering
    public static void render(Canvas canvas, Text obj) {
        canvas.drawText(obj.getText(), obj.getX(), obj.getY(), obj.getStrokePaint());
        canvas.drawText(obj.getText(), obj.getX(), obj.getY(), obj.getP());
    }

    public static void render(Canvas canvas, Circle obj) {
        canvas.drawCircle(obj.getX(), obj.getY(), obj.getR(), obj.getP());
    }

    public static void render(Canvas canvas, CubicBezier obj) {
        canvas.drawPath(obj.getPath(), obj.getP());
    }

    public static void render(Canvas canvas, LineTracer obj) {
        canvas.drawPath(obj.getPath(), obj.getP());
    }

    public static void render(Canvas canvas, ParticleTracer obj) {
        if (obj.isActive()) {
            render(canvas, obj.getTex());
        }

        // Draw the particles
        Particle p;
        for (int i = 0; i < obj.particleList.size(); i++) {
            p = obj.particleList.get(i);
            if (p.active) {
                render(canvas, p);
            }
        }
    }

    public static void render(Canvas canvas, Texture obj) {
        canvas.drawBitmap(bitmaps.getBitmap(obj.getID()), obj.getMatrix(), obj.getPaint());
    }

    public static void render(Canvas canvas, Particle obj) {
        render(canvas, obj.getTexture());
    }

    public static void render(Canvas canvas, HitCircleOverlay obj) {
        render(canvas, obj.getTex());
    }

    public static void render(Canvas canvas, HitCircle obj) {
        render(canvas, obj.getTexture());
        render(canvas, obj.getOverlay());
    }

    public static void render(Canvas canvas, ShadowedCurve obj) {
        canvas.drawPath(obj.getPath(), obj.getShadowPaint());
        canvas.drawPath(obj.getPath(), obj.getSolidPaint());
    }

    public static void render(Canvas canvas, Button obj) {
        render(canvas, obj.getTexture());
    }

    public static void render(Canvas canvas, HitCircleNeonOverlay obj) {
        canvas.drawCircle(obj.getX(), obj.getY(), obj.getRadius(), obj.getPaint());
    }
}
