package teamdroid.com.speedtestarena.actor;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;

import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.graphics.BitmapLoader;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircleOverlay {
    private static float START_SCALE = 1f;
    private static float END_SCALE = 0.28f;

    private float centerx;
    private float centery;
    private float scale = 1;
    private Texture t;

    private long prevTime = 0;

    private float contractionRate = 0;

    public HitCircleOverlay(int texID, float x, float y, long startTime, long endTime) {
        centerx = x;
        centery = y;

        prevTime = startTime;
        contractionRate = (END_SCALE - START_SCALE) / (endTime - startTime);

        float[] colorTransform = {
                1f, 0, 0, 0, 0,
                0, 1f, 0, 0, 0,
                0, 0, 1f, 0, 0,
                0, 0, 0, 1f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        t = new Texture(texID, x, y, 255, colorMatrix);
        t.setRotation(180, t.getWidth() / 2, t.getHeight() / 2);
        t.setTranslationCenter(x, y);
        t.recomputeCoordinateMatrix();
    }

    public void setCenter(float x, float y) {
        centerx = x;
        centery = y;
        t.setTranslationCenter(x, y);
        t.recomputeCoordinateMatrix();
    }

    public void reset(float x, float y) {
        centerx = x;
        centery = y;
        scale = 1f;
        t.setScaleCenter(scale, scale);
        t.setTranslationCenter(x, y);
        t.recomputeCoordinateMatrix();
    }

    // Getters
    public float getX() {
        return this.centerx;
    }

    public float getY() {
        return this.centery;
    }

    public Texture getT() { return this.t; }

    public void update(long songPos) {
        if (scale > END_SCALE) {
            scale += contractionRate * (songPos - prevTime);
            prevTime = songPos;

            t.setScaleCenter(scale, scale);
            t.recomputeCoordinateMatrix();

            float[] colorTransform = {
                    1f, 0, 0, 0, 0,
                    0, 1f, 0, 0, 0,
                    0, 0, 1f, 0, 0,
                    0, 0, 0, 1f, 0};
            t.setColorMatrix(colorTransform);
        } else {
            float[] colorTransform = {
                    1f, 0, 0, 0, 0,
                    0, 1f, 0, 0, 0,
                    0, 0, 1f, 0, 0,
                    0, 0, 0, 1f, 0};
            t.setColorMatrix(colorTransform);
        }
    }
}
