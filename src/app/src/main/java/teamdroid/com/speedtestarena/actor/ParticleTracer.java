package teamdroid.com.speedtestarena.actor;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.game.GameTest1.GameTest1MainThread;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.graphics.TextureLoader;
import teamdroid.com.speedtestarena.math.MathUtil;
import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-20.
 */

public class ParticleTracer {

    public volatile boolean active;
    private Vector2f prevPoint;
    private Texture tex;
    private GameTest1MainThread t;
    TextureLoader loader;
    //private Context c;

    public ParticleTracer(TextureLoader loader) {
        this.loader = loader;
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(loader, R.drawable.cursor, 0, 0, 255);
    }

    public ParticleTracer(TextureLoader loader, GameTest1MainThread thread) {
        this.loader = loader;
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(loader, R.drawable.cursor, 0, 0, 255);
        t = thread;
        //c = context;
    }

    public Texture getTex() {
        return tex;
    }

    public boolean isActive() {
        return active;
    }

    // Updates
    public void eventUpdate(float px, float py) {
        if (MathUtil.distanceSquare(prevPoint.getX(), prevPoint.getY(), px, py) > 25) {
            prevPoint.set(px, py);
            tex.setPos(px - tex.getWidth() / 2, py - tex.getHeight() / 2);
            // spawn particle (not implemented)
            t.particleList.add(new Particle(loader, px, py));
        }
    }

    public void set(float px, float py) {
        this.active = true;
        prevPoint.set(px, py);
        tex.setPos(px - tex.getWidth() / 2, py - tex.getHeight() / 2);
    }

    public void reset() {
        this.active = false;
    }

    // Drawable
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    public void setAlpha(int arg0)
    {
    }

    public void setColorFilter(ColorFilter arg0)
    {
    }
}
