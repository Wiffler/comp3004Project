package teamdroid.com.speedtestarena.graphics;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
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

    private float scaleX, scaleY, translateX, translateY, angleDegrees, rotationX, rotationY, width, height;

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
        angleDegrees = 0f;
        rotationX = 0f;
        rotationY = 0f;
        coordinateMatrix = new Matrix();
        coordinateMatrix.postRotate(angleDegrees, rotationX, rotationY);
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);

        // Setup the color matrix and Paint object
        this.p = new Paint();

        if (colorMatrix != null) {
            this.colorMatrix = colorMatrix;
            p.setColorFilter(new ColorMatrixColorFilter(this.colorMatrix));
        }

        p.setAlpha(alpha);
        p.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
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

        //recomputeCoordinateMatrix();
    }

    public void setTranslationCenter(float x, float y) {
        translateX = x - width / 2;
        translateY = y - height / 2;

        //recomputeCoordinateMatrix();
    }

    public void setTranslationCenterScale(float x, float y) {
        translateX = x - width * scaleX / 2;
        translateY = y - height * scaleY / 2;

        //recomputeCoordinateMatrix();
    }

    public void setScale(float x, float y) {
        scaleX = x;
        scaleY = y;

        //recomputeCoordinateMatrix();
    }

    public void setScaleCenter(float x, float y) {
        //rotationX = (width * scaleX) / 2;
        //rotationY = (height * scaleY) / 2;
        translateX = (translateX + (width * scaleX) / 2) - width * x / 2;
        translateY = (translateY + (height * scaleY) / 2) - height * y / 2;
        scaleX = x;
        scaleY = y;

        //recomputeCoordinateMatrix();
    }

    public void setRotation(float degrees, float x, float y) {
        angleDegrees = degrees;
        rotationX = x;
        rotationY = y;

        //recomputeCoordinateMatrix();
    }

    public void recomputeCoordinateMatrix() {
        coordinateMatrix.reset();
        coordinateMatrix.postRotate(angleDegrees, rotationX, rotationY);
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
