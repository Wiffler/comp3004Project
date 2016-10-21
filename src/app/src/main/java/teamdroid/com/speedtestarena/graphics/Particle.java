package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Paint;

import teamdroid.com.speedtestarena.R;
import teamdroid.com.speedtestarena.math.Vector2f;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Particle {

    private int decayRate;
    private Texture tex;
    private Vector2f pos;

    public Particle(Context context, float px, float py) {
        decayRate = 5;
        pos = new Vector2f(px, py);
        tex = new Texture(context, R.drawable.cursortrail, 0, 0, 255);
        tex.setPos(px - tex.getWidth() / 2, py - tex.getHeight() / 2);
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
