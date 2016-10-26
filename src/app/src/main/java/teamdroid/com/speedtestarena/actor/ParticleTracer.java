package teamdroid.com.speedtestarena.actor;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.game.GameTest1.GameTest1MainThread;
import teamdroid.com.speedtestarena.graphics.Particle;
import teamdroid.com.speedtestarena.graphics.Texture;
import teamdroid.com.speedtestarena.graphics.BitmapLoader;
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

    public ParticleTracer(BitmapLoader loader) {
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(R.drawable.cursor, 0, 0, 255, null);
    }

    public ParticleTracer(BitmapLoader loader, GameTest1MainThread thread) {
        this.active = false;
        prevPoint = new Vector2f(-1f, -1f);
        tex = new Texture(R.drawable.cursor, 0, 0, 255, null);
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
            tex.recomputeCoordinateMatrix();
            // spawn particle
            t.particleList.add(new Particle(px, py));
        }
    }

    public void set(float px, float py) {
        this.active = true;
        prevPoint.set(px, py);
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
    }

    public void reset() {
        this.active = false;
    }
}
