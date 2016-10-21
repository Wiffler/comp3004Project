package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;

import static java.security.AccessController.getContext;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Texture {

    //Bitmap tex;
    Matrix m;
    Paint p;
    TextureLoader loader;

    int texID = -1;

    int width;
    int height;
    float x;
    float y;

    public Texture(TextureLoader loc, int id, float px, float py, int alpha) {
        //tex = BitmapFactory.decodeResource(context.getResources(), id);
        texID = id;
        loader = loc;
        x = px;
        y = py;
        width = loader.getTexture(texID).getWidth();
        height = loader.getTexture(texID).getHeight();
        m = new Matrix();
        this.p = new Paint();
        p.setAlpha(alpha);
        //System.out.println(p.getAlpha());
    }

    public void setPos(float px, float py) {
        x = px;
        y = py;
    }

    // Setters
    public void setAlpha(int alpha) {
        if (p.getAlpha() > 0) {
            //System.out.println("BEFORE: " + p.getAlpha());
            //System.out.println("RATE: " + alpha);
            p.setAlpha(p.getAlpha() + alpha);
            //System.out.println("AFTER: " + p.getAlpha());
        }
    }

    // Getters
    public Bitmap getTex() {
        return loader.getTexture(texID);
    }

    public Matrix getMatrix() {
        return m;
    }

    public Paint getP() { return p; }

    public float getX() { return x; }

    public float getY() {
        return y;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getAlpha() { return p.getAlpha(); }
}
