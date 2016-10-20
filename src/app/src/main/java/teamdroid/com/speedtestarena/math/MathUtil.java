package teamdroid.com.speedtestarena.math;

/**
 * Created by Kenny on 2016-10-19.
 */

public class MathUtil {

    public static float distanceSquare(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

}
