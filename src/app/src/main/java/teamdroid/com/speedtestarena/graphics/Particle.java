package teamdroid.com.speedtestarena.graphics;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Particle {

    private int decayRate;
    private Texture tex;
    private Vector2f pos;

    public Particle(float px, float py) {
        decayRate = 5;
        pos = new Vector2f(px, py);
        tex = new Texture(R.drawable.cursortrail, 0, 0, 255, null);
        tex.setTranslationCenter(px, py);
        tex.recomputeCoordinateMatrix();
    }

    public Texture getTex() {
        return tex;
    }

    public int getAlpha() {
        return tex.getAlpha();
    }

    public void update() {
        tex.setAlpha(-1 * decayRate);
    }
}
