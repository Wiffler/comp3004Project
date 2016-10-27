package teamdroid.com.speedtestarena.actor;

import android.graphics.ColorMatrix;

import teamdroid.com.speedtestarena.graphics.Texture;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircleOverlay {
    private float endScale = 0f;
    private float scale = 1f;
    private Texture tex;

    private long prevTime = 0;
    private float contractionRate = 0;

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
            scale += contractionRate * (songPos - prevTime);
            prevTime = songPos;

            tex.setScaleCenter(scale, scale);
            tex.recomputeCoordinateMatrix();

            /*
            float[] colorTransform = {
                    1f, 0, 0, 0, 0,
                    0, 1f, 0, 0, 0,
                    0, 0, 1f, 0, 0,
                    0, 0, 0, 1f, 0};
            tex.setColorMatrix(colorTransform);
            */
        } else {
            /*
            float[] colorTransform = {
                    1f, 0, 0, 0, 0,
                    0, 1f, 0, 0, 0,
                    0, 0, 1f, 0, 0,
                    0, 0, 0, 1f, 0};
            tex.setColorMatrix(colorTransform);
            */
        }
    }
}
