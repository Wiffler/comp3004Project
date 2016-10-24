package teamdroid.com.speedtestarena.actor;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;

import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.graphics.TextureLoader;
import teamdroid.com.speedtestarena.math.MathUtil;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircleOverlay {
    private float centerx;
    private float centery;
    private int radius;
    private float scale = 1;
    private Paint p;
    private Texture t;

    private long prevTime = 0;

    public HitCircleOverlay(float x, float y, int r, int texID, TextureLoader loader) {
        centerx = x;
        centery = y;
        radius = r;

        p = new Paint();
        p.setColor(Color.BLUE);

        float[] colorTransform = {
                0, 0f, 0, 0, 0,
                0, 0, 0f, 0, 0,
                0, 0, 0, 1f, 0,
                0, 0, 0, 1f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        t = new Texture(loader, texID, x, y, 255, colorMatrix);
    }

    // Setters
    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
    }

    public void setColor(int c) {
        p.setColor(c);
    }

    public void setCenter(float x, float y) {
        centerx = x;
        centery = y;
        t.setTranslationCenter(x, y);
    }

    public void reset(float x, float y) {
        centerx = x;
        centery = y;
        scale = 1f;
        t.setScaleCenter(scale, scale);
        t.setTranslationCenter(x, y);
    }

    // Getters
    public float getX() {
        return this.centerx;
    }

    public float getY() {
        return this.centery;
    }

    public int getR() {
        return this.radius;
    }

    public Paint getP() {
        return this.p;
    }

    public Texture getT() { return this.t; }

    public void update() {
        if (scale > 0.30) {
            scale -= 0.01;
            t.setScaleCenter(scale, scale);

            float[] colorTransform = {
                    0, 0f, 0, 0, 0,
                    0, 0, 0f, 0, 0,
                    0, 0, 0, 1f, 0,
                    0, 0, 0, 1f, 0};
            t.setColorMatrix(colorTransform);
        } else {
            float[] colorTransform = {
                    0, 1f, 0, 0, 0,
                    0, 0, 0f, 0, 0,
                    0, 0, 0, 0f, 0,
                    0, 0, 0, 1f, 0};
            t.setColorMatrix(colorTransform);
        }
    }
}