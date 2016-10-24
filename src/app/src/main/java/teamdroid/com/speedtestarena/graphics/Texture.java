package teamdroid.com.speedtestarena.graphics;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Kenny on 2016-10-20.
 */

public class Texture {

    private Matrix coordinateMatrix;
    private ColorMatrix colorMatrix;
    private Paint p;

    private int textureID = -1;

    private float scaleX, scaleY, translateX, translateY, width, height;

    public Texture(int id, float px, float py, int alpha, ColorMatrix colorMatrix) {
        // Setup the texture
        textureID = id;
        width = Renderer.getBitmapWidth(textureID);
        height = Renderer.getBitmapHeight(textureID);

        // Setup the coordinate matrix
        scaleX = 1f;
        scaleY = 1f;
        translateX = 0f;
        translateY = 0f;
        coordinateMatrix = new Matrix();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);

        // Setup the color matrix and Paint object
        this.p = new Paint();

        if (colorMatrix != null) {
            this.colorMatrix = colorMatrix;
            p.setColorFilter(new ColorMatrixColorFilter(this.colorMatrix));
        }

        p.setAlpha(alpha);
    }

    // Setters
    public void setAlpha(int alpha) {
        if (p.getAlpha() > 0) {
            p.setAlpha(p.getAlpha() + alpha);
        }
    }

    public void setTranslation(float x, float y) {
        translateX = x;
        translateY = y;

        coordinateMatrix.reset();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    public void setTranslationCenter(float x, float y) {
        translateX = x - width / 2;
        translateY = y - height / 2;

        coordinateMatrix.reset();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    public void setTranslationCenterScale(float x, float y) {
        translateX = x - width * scaleX / 2;
        translateY = y - height * scaleY / 2;

        coordinateMatrix.reset();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    public void setScale(float x, float y) {
        scaleX = x;
        scaleY = y;

        coordinateMatrix.reset();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    public void setScaleCenter(float x, float y) {
        translateX = (translateX + (width * scaleX) / 2) - width * x / 2;
        translateY = (translateY + (height * scaleY) / 2) - height * y / 2;
        scaleX = x;
        scaleY = y;

        coordinateMatrix.reset();
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    public void setColorMatrix(float[] m) {
        colorMatrix.set(m);
        p.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    // Getters
    public Matrix getMatrix() {
        return coordinateMatrix;
    }

    /*
    public Bitmap getBitmap() {
        return loader.getTexture(textureID);
    }
    */

    public int getID() {
        return textureID;
    }

    public Paint getP() { return p; }

    public float getX() { return translateX; }

    public float getY() {
        return translateY;
    }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public int getAlpha() { return p.getAlpha(); }
}
