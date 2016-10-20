package teamdroid.com.speedtestarena.math;

/**
 * Created by Kenny on 2016-10-19.
 */

public class Vector2f {

    private float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2f v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static float dotProduct(Vector2f v1, Vector2f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public static float distanceSquare(Vector2f v1, Vector2f v2) {
        return (v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY());
    }
}
