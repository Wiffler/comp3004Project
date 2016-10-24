package teamdroid.com.speedtestarena.actor;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.graphics.TextureLoader;
import teamdroid.com.speedtestarena.math.MathUtil;

/**
 * Created by Kenny on 2016-10-24.
 */

public class HitCircle {
    private float centerx;
    private float centery;
    private float radius;
    //private Paint p;

    private Texture tex;

    private HitCircleOverlay overlay;

    private long prevTime = 0;

    public HitCircle(TextureLoader loc, int id, float x, float y, float r) {
        centerx = x;
        centery = y;
        radius = r;

        //p = new Paint();
        //p.setColor(Color.BLUE);

        float[] colorTransform = {
                1f, 0, 0, 0, 0,
                0, 1f, 0, 0, 50,
                0, 0, 1f, 0, 0,
                0, 0, 0, 1.35f, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        tex = new Texture(loc, id, x, y, 255, colorMatrix);
        tex.setScale(0.28f, 0.28f);
        tex.setTranslationCenterScale(x, y);

        //p = new Paint();

        overlay = new HitCircleOverlay(x, y, 15, R.drawable.hitcircleoverlay, loc);
    }

    // Setters
    /*
    public void setColor(String c) {
        // Use Color.parseColor to define HTML colors
        p.setColor(Color.parseColor(c));
    }

    public void setColor(int c) {
        p.setColor(c);
    }*/

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

    /*
    public Paint getP() {
        return this.p;
    }
    */

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
        }
    }
}
