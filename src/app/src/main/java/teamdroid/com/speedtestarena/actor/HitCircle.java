package teamdroid.com.speedtestarena.actor;

import android.graphics.ColorMatrix;

import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.math.MathUtil;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircle {
    public volatile boolean active;

    private Texture tex;
    //private HitCircleOverlay overlay;
    private HitCircleNeonOverlay overlay;

    private float centerx;
    private float centery;
    private float radius;

    private long startTime = 0;
    private long deathTime = 0;
    private long beatTime = 0;

    public HitCircle(int imageID, float x, float y, long startTime, long deathTime, long beatTime) {
        // Create the texture
        float[] colorTransform = {
                1f, 0, 0, 0, 0,
                0, 1f, 0, 0, 50,
                0, 0, 1f, 0, 0,
                0, 0, 0, 1f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        tex = new Texture(imageID, x, y, 255, colorMatrix);
        tex.setTranslation(x, y);
        tex.recomputeCoordinateMatrix();

        // Set the fields
        centerx = x + tex.getWidth() / 2;
        centery = y + tex.getHeight() / 2;

        this.startTime = startTime;
        this.deathTime = deathTime;
        this.beatTime = beatTime;

        radius = tex.getWidth() / 2; // assuming square bitmap

        // Create the overlay object
        //overlay = new HitCircleOverlay(R.drawable.hitcircleoverlay, centerx, centery, startTime, beatTime);
        overlay = new HitCircleNeonOverlay(centerx, centery, tex.getWidth() + 50, startTime, beatTime);

        active = false;
    }

    // Setters
    public void setCenter(float px, float py) {
        centerx = px + tex.getWidth() / 2;
        centery = py + tex.getHeight() / 2;
        tex.setTranslation(px, py);
        tex.recomputeCoordinateMatrix();
    }

    public void setTime(long startTime, long deathTime, long beatTime) {
        this.startTime = startTime;
        this.deathTime = deathTime;
        this.beatTime = beatTime;
    }

    // Getters
    public float getX() {
        return centerx;
    }

    public float getY() {
        return centery;
    }

    public long getBeatTime() {
        return beatTime;
    }

    public float getRadius() {
        return radius;
    }

    public Texture getTexture() {
        return tex;
    }

    /*public HitCircleOverlay getOverlay() { return this.overlay; }*/
    public HitCircleNeonOverlay getOverlay() { return overlay; }

    public boolean inCircle(float px, float py) {
        if (MathUtil.distanceSquare(centerx, centery, px, py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
    }

    public void activate(float x, float y, long startTime, long deathTime, long beatTime) {
        setCenter(x, y);
        this.startTime = startTime;
        this.deathTime = deathTime;
        this.beatTime = beatTime;

        overlay.reset(centerx, centery, startTime, beatTime);

        active = true;
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
