package teamdroid.com.speedtestarena.actor;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Shader;

/**
 * Created by Kenny on 2016-10-30.
 */

public class HitCircleNeonOverlay {
    // Constants
    private static float TARGET_SIZE = 75;
    private static int[] colors ={0x00000000, 0xDD00CFFF, 0x00000000};
    private static float[] distances ={0.35f, 0.50f, 0.65f};

    // Circle Properties
    private float endScale, startScale, scale;
    private float centerx, centery, radius;

    private long prevTime = 0;
    private float contractionRate = 0;

    private Paint p;

    public HitCircleNeonOverlay(float x, float y, float r, long startTime, long endTime) {
        // Get the circle info
        centerx = x;
        centery = y;
        radius = r;

        // Set the last update time
        prevTime = startTime;

        // Set the contraction rate
        scale = 1f;
        startScale = 1f;
        endScale = TARGET_SIZE / r;
        contractionRate = (endScale - startScale) / (endTime - startTime);

        // Create the paint object
        p = new Paint();
        p.setDither(true);
        p.setFilterBitmap(true);
        // Set the shader
        p.setShader(new RadialGradient(x, y, r, colors, distances, Shader.TileMode.CLAMP));
    }

    // Setters
    public void setCenter(float x, float y) {
        centerx = x;
        centery = y;
    }

    public void reset(float x, float y, long startTime, long endTime) {
        // Reset the location
        centerx = x;
        centery = y;

        // Reset the contraction rate
        scale = 1f;
        startScale = 1f;
        endScale = TARGET_SIZE / radius;
        contractionRate = (endScale - startScale) / (endTime - startTime);

        prevTime = startTime;
    }

    // Getters
    public float getX() { return centerx; }
    public float getY() { return centery; }
    public float getRadius() { return scale * radius; }
    public Paint getPaint() { return p; }

    // Update the object
    public void update(long songPos) {
        if (scale > endScale) {
            // Compute the new texture scale
            scale += contractionRate * (songPos - prevTime);
            prevTime = songPos;

            // Update the shader
            p.setShader(new RadialGradient(centerx, centery, scale * radius, colors, distances, Shader.TileMode.CLAMP));
        }
    }
}
