package teamdroid.com.speedtestarena.math;

/**
 * Created by Kenny on 2016-10-19.
 */

public class Vector2f {

    private float[] vector;

    public Vector2f(float x, float y) {
        vector = new float[2];
        vector[0] = x;
        vector[1] = y;
    }

    public Vector2f(Vector2f v) {
        vector = new float[2];
        vector[0] = v.getX();
        vector[1] = v.getY();
    }

    public void set(float x, float y) {
        vector[0] = x;
        vector[1] = y;
    }

    public void set(Vector2f v) {
        vector[0] = v.getX();
        vector[1] = v.getY();
    }

    public float getX() {
        return vector[0];
    }

    public float getY() {
        return vector[1];
    }

    public static float dotProduct(Vector2f v1, Vector2f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public static float distanceSquare(Vector2f v1, Vector2f v2) {
        return (v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY());
    }
}
