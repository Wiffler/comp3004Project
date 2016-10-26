package teamdroid.com.speedtestarena.actor;

import android.graphics.ColorMatrix;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.math.MathUtil;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircle {
    private Texture tex;
    private HitCircleOverlay overlay;

    private float centerx;
    private float centery;
    private float radius;

    private long startTime = 0;
    private long deathTime = 0;
    private long beatTime = 0;

    public HitCircle(int imageID, float x, float y, long startTime, long deathTime, long beatTime) {
        centerx = x;
        centery = y;

        this.startTime = startTime;
        this.deathTime = deathTime;
        this.beatTime = beatTime;

        float[] colorTransform = {
                1f, 0, 0, 0, 0,
                0, 1f, 0, 0, 50,
                0, 0, 1f, 0, 0,
                0, 0, 0, 1f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        tex = new Texture(imageID, x, y, 255, colorMatrix);
        tex.setScale(0.28f, 0.28f);
        tex.setTranslationCenterScale(x, y);
        tex.recomputeCoordinateMatrix();

        radius = tex.getWidth() / 2; // assuming square bitmap

        // Create the overlay object
        //float contract = (float) ((1f - 0.28f) / (deathTime - startTime));
        overlay = new HitCircleOverlay(R.drawable.hitcircleoverlay, x, y, startTime, beatTime);
    }

    // Setters
    public void setCenter(float px, float py) {
        centerx = px;
        centery = py;
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
    }

    // Getters
    public float getX() {
        return this.centerx;
    }

    public float getY() {
        return this.centery;
    }

    public long getBeatTime() {
        return beatTime;
    }

    public float getR() {
        return this.radius;
    }

    public Texture getT() {
        return this.tex;
    }

    public HitCircleOverlay getOverlay() { return this.overlay; }

    public boolean inCircle(float px, float py) {
        if (MathUtil.distanceSquare(centerx, centery, px, py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
    }

    public boolean update(long songPos) {
        if (songPos < deathTime) {
            overlay.update(songPos);
            return true;
        } else {
            return false;
        }
    }
}
