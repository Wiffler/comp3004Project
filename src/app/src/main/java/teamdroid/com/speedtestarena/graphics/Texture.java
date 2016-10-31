package teamdroid.com.speedtestarena.graphics;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Shader;

/**
 * Created by Kenny on 2016-10-20.
 * Holds information on how to draw a given bitmap.
 */

public class Texture {

    // Matrices and Paint objects
    private Matrix coordinateMatrix;
    private ColorMatrix colorMatrix;
    private Paint p;

    // Key value to access the bitmap stored in the BitmapLoader
    private int textureID = -1;

    // Transformation values
    private float scaleX, scaleY, translateX, translateY, angleDegrees, rotationX, rotationY, width, height;

    // Constructor(s)
    public Texture(int id) {
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
        p.setAlpha(255);
    }

    public Texture(int id, float px, float py, int alpha) {
        // Setup the texture
        textureID = id;
        width = Renderer.getBitmapWidth(textureID);
        height = Renderer.getBitmapHeight(textureID);

        // Setup the coordinate matrix
        scaleX = 1f;
        scaleY = 1f;
        translateX = px;
        translateY = py;
        angleDegrees = 0f;
        rotationX = 0f;
        rotationY = 0f;
        coordinateMatrix = new Matrix();
        coordinateMatrix.postRotate(angleDegrees, rotationX, rotationY);
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);

        // Setup the color matrix and Paint object
        this.p = new Paint();
        p.setAlpha(alpha);
    }

    public Texture(int id, float px, float py, int alpha, ColorMatrix colorMatrix) {
        // Setup the texture
        textureID = id;
        width = Renderer.getBitmapWidth(textureID);
        height = Renderer.getBitmapHeight(textureID);

        // Setup the coordinate matrix
        scaleX = 1f;
        scaleY = 1f;
        translateX = px;
        translateY = py;
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
    }

    public Texture(int id, float px, float py, int w, int h, int alpha, ColorMatrix colorMatrix) {
        // Setup the texture
        textureID = id;
        width = w;
        height = h;

        // Setup the coordinate matrix
        scaleX = 1f;
        scaleY = 1f;
        translateX = px;
        translateY = py;
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
    }

    // Transformation Setters
    public void setTranslation(float x, float y) {
        translateX = x;
        translateY = y;
    }

    public void setTranslationCenter(float x, float y) {
        translateX = x - width / 2;
        translateY = y - height / 2;
    }

    public void setTranslationCenter(float x, float y, float w, float h) {
        translateX = x - w / 2;
        translateY = y - h / 2;
    }

    public void setTranslationCenterScale(float x, float y) {
        translateX = x - width * scaleX / 2;
        translateY = y - height * scaleY / 2;
    }

    public void setTranslationCenterScale(float x, float y, float w, float h) {
        translateX = x - w * scaleX / 2;
        translateY = y - h * scaleY / 2;
    }

    public void setScale(float x, float y) {
        scaleX = x;
        scaleY = y;
    }

    public void setScaleCenter(float x, float y) {
        translateX = (translateX + (width * scaleX) / 2) - width * x / 2;
        translateY = (translateY + (height * scaleY) / 2) - height * y / 2;
        scaleX = x;
        scaleY = y;
    }

    public void setRotation(float degrees, float x, float y) {
        angleDegrees = degrees;
        rotationX = x;
        rotationY = y;
    }

    public void recomputeCoordinateMatrix() {
        coordinateMatrix.reset();
        coordinateMatrix.postRotate(angleDegrees, rotationX, rotationY);
        coordinateMatrix.postScale(scaleX, scaleY);
        coordinateMatrix.postTranslate(translateX, translateY);
    }

    // Paint Setters
    public void setAlpha(int alpha) {
        p.setAlpha(alpha);
    }

    public void setPorterDuffColorFilter(int color) {
        p.setFilterBitmap(true);
        p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
    }

    public void setLightingColorFilter(int color) {
        p.setFilterBitmap(true);
        p.setColorFilter(new LightingColorFilter(Color.WHITE, color));
    }

    public void setShader(float centerX, float centerY, float radius,
                          int centerColor, int edgeColor, Shader.TileMode tileMode) {
        p.setShader(new RadialGradient(centerX, centerY, radius, centerColor, edgeColor, tileMode));
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

    public Paint getPaint() { return p; }

    public float getX() { return translateX; }

    public float getY() {
        return translateY;
    }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public int getAlpha() { return p.getAlpha(); }
}
