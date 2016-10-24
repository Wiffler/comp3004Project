package teamdroid.com.speedtestarena.actor;

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
    private TextureLoader loader;

    public ParticleTracer(TextureLoader loader) {
        this.loader = loader;
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(loader, R.drawable.cursor, 0, 0, 255, null);
    }

    public ParticleTracer(TextureLoader loader, GameTest1MainThread thread) {
        this.loader = loader;
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(loader, R.drawable.cursor, 0, 0, 255, null);
        t = thread;
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
            tex.setTranslationCenter(px, py);
            // spawn particle
            t.particleList.add(new Particle(loader, px, py));
        }
    }

    public void set(float px, float py) {
        this.active = true;
        prevPoint.set(px, py);
        tex.setTranslationCenter(px, py);
    }

    public void reset() {
        this.active = false;
    }
}
