package teamdroid.com.speedtestarena.actor;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.graphics.Texture;

import static java.lang.Math.max;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Particle {

    public volatile boolean active;
    private int decayRate;
    private Texture tex;

    public Particle(float px, float py) {
        decayRate = -10;
        tex = new Texture(R.drawable.cursortrail, 0, 0, 255, null);
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
        active = false;
    }

    public void activate(float px, float py) {
        active = true;
        tex.setAlpha(255);
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
    }

    public boolean update() {
        if (tex.getAlpha() <= 0) {
            active = false;
        } else {
            tex.setAlpha(max(tex.getAlpha() + decayRate, 0));
        }

        return active;
    }

    public Texture getTexture() {
        return tex;
    }
}
