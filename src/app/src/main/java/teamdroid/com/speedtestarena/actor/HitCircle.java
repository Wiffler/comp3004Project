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

    private long prevTime = 0;

    public HitCircle(int imageID, float x, float y, float r) {
        centerx = x;
        centery = y;
        radius = r;

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

        overlay = new HitCircleOverlay(R.drawable.hitcircleoverlay, x, y, 15);
    }

    // Setters
    public void setCenter(float px, float py) {
        centerx = px;
        centery = py;
        tex.setTranslationCenter(px, py);
    }

    // Getters
    public float getX() {
        return this.centerx;
    }

    public float getY() {
        return this.centery;
    }

    public float getR() {
        return this.radius;
    }

    public Texture getT() {
        return this.tex;
    }

    public HitCircleOverlay getOverlay() {return this.overlay;}

    public boolean inCircle(float px, float py) {
        if (MathUtil.distanceSquare(centerx, centery, px, py) <= radius * radius) {
            return true;
        } else {
            return false;
        }
    }

    public void update(long curTime, float x, float y) {
        if (curTime - prevTime > 2000) {
            centerx = x;
            centery = y;
            tex.setTranslationCenterScale(x, y);
            overlay.reset(x, y);

            //System.out.println("HitCircle   tX: " + tex.getX() + " tY: " + tex.getY());
            //System.out.println("Overlay     tX: " + overlay.getT().getX() + " tY: " + overlay.getT().getY());

            prevTime = curTime;
        } else {
            overlay.update();
        }
    }
}
