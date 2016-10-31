package teamdroid.com.speedtestarena.actor;

import android.graphics.Color;
import android.graphics.ColorMatrix;

import teamdroid.com.speedtestarena.graphics.Texture;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircleOverlay {
    private float endScale = 0f;
    private float startScale = 1f;
    private float scale = 1f;
    private Texture tex;

    private long prevTime = 0;
    private float contractionRate = 0;

    // Color Interpolation
    private int mEnd_Red = 255;
    private int mEnd_Green = 255;
    private int mEnd_Blue = 255;
    private int mEnd_Alpha = 255;
    private int mStart_Red = 255;
    private int mStart_Green = 165;
    private int mStart_Blue = 0;
    private int mStart_Alpha = 155;

    public HitCircleOverlay(int texID, float x, float y, long startTime, long endTime) {
        // Create the texture
        float[] colorTransform = {
                1f, 0, 0, 0, 0,
                0, 1f, 0, 0, 0,
                0, 0, 1f, 0, 0,
                0, 0, 0, 1f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        tex = new Texture(texID, x, y, 255, colorMatrix);
        tex.setRotation(180, tex.getWidth() / 2, tex.getHeight() / 2);
        tex.setTranslationCenter(x, y);
        tex.recomputeCoordinateMatrix();

        // Set the last update time
        prevTime = startTime;

        // Set the contraction rate
        float targetSize = 100;
        float startScale = 1f;
        endScale = targetSize / tex.getWidth();
        contractionRate = (endScale - startScale) / (endTime - startTime);
    }

    public void setCenter(float x, float y) {
        tex.setTranslationCenter(x, y);
        tex.recomputeCoordinateMatrix();
    }

    public void reset(float x, float y, long startTime, long endTime) {
        // Reset the location
        scale = 1f;
        tex.setScaleCenter(scale, scale);
        tex.setTranslationCenter(x, y);
        tex.recomputeCoordinateMatrix();

        // Reset the contraction rate
        float targetSize = 100;
        float startScale = 1f;
        endScale = targetSize / tex.getWidth();
        contractionRate = (endScale - startScale) / (endTime - startTime);

        prevTime = startTime;
    }

    // Getters
    public Texture getTex() { return this.tex; }

    // Update the object
    public void update(long songPos) {
        if (scale > endScale) {
            // Compute the new texture scale
            scale += contractionRate * (songPos - prevTime);
            tex.setScaleCenter(scale, scale);
            tex.recomputeCoordinateMatrix();
            prevTime = songPos;

            // Compute the new texture color filter *WIP
            tex.setPorterDuffColorFilter(getInterpolatedColor((scale - endScale) / (startScale - endScale)));
        }
    }

    // Color Interpolation
    private int getInterpolatedColor(float t) {
        if (t < 0) {
            t = 0f;
        } else if (t > 1) {
            t = 1f;
        }

        return Color.argb(
                interpolateInt(mStart_Alpha, mEnd_Alpha - mStart_Alpha, t),
                interpolateInt(mStart_Red, mEnd_Red, t),
                interpolateInt(mStart_Green, mEnd_Green, t),
                interpolateInt(mStart_Blue, mEnd_Blue, t));
    }

    private int interpolateInt(int a, int b, float t) {
        return (int) (a + (b - a) * t);
    }
}
