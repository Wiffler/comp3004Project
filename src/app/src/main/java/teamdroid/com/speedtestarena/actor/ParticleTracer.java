package teamdroid.com.speedtestarena.actor;

import java.util.ArrayList;
import java.util.Iterator;

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

    private static int PARTICLE_COUNT = 32;

    private volatile boolean active;
    private Vector2f prevPoint;
    private Texture tex;

    // Particles
    public volatile ArrayList<Particle> particleList;

    public ParticleTracer() {
        active = false;
        prevPoint = new Vector2f(0f, 0f);
        tex = new Texture(R.drawable.cursor, 0, 0, 255, null);

        // Setup the particles
        particleList = new ArrayList<Particle>(PARTICLE_COUNT);
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particleList.add(new Particle(0, 0));
        }
    }

    public Texture getTex() {
        return tex;
    }

    public boolean isActive() {
        return active;
    }

    // Updates
    public void eventUpdate(float px, float py) {
        if (MathUtil.distanceSquare(prevPoint.x, prevPoint.y, px, py) > 25) {
            // Update the cursor location
            prevPoint.set(px, py);
            tex.setTranslationCenter(px, py);
            tex.recomputeCoordinateMatrix();

            // Activate a particle
            Particle p;
            for (int i = 0; i < particleList.size(); i++) {
                p = particleList.get(i);
                if (p.active == false) {
                    p.activate(px, py);
                    break;
                }
            }
        }
    }

    public boolean update() {
        boolean dirtyFlag = false;

        // Update the particle list
        Particle p;
        for (int i = 0; i < particleList.size(); i++) {
            p = particleList.get(i);
            if (p.active) {
                dirtyFlag = p.update() || dirtyFlag;
            }
        }

        return dirtyFlag;
    }

    public void set(float px, float py) {
        active = true;
        prevPoint.set(px, py);
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
    }

    public void reset() {
        active = false;
    }
}
