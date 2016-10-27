package teamdroid.com.speedtestarena.math;

/**
 * Created by Kenny on 2016-10-19.
 */

public class Vector2f {

    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    public static float dotProduct(Vector2f v1, Vector2f v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static float distanceSquare(Vector2f v1, Vector2f v2) {
        return (v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y);
    }
}
