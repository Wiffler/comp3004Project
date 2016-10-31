package teamdroid.com.speedtestarena.actor;

import teamdroid.com.speedtestarena.graphics.Texture;

/**
 * Created by Kenny on 2016-10-28.
 */

public class Button {
    private float x, y, width, height;
    private Texture tex;

    public Button(int id, float x, float y) {
        this.x = x;
        this.y = y;
        tex = new Texture(id, x, y, 255);
        width = tex.getWidth();
        height = tex.getHeight();
    }

    public Texture getTexture() {
        return tex;
    }

    public boolean inButton(float px, float py) {
        if ((px > x + width) || (px < x) || (py > y + height) || (py < y)) {
            return false;
        } else {
            return true;
        }
    }
}
