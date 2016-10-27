package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Kenny on 2016-10-26.
 */

public class Background extends Drawable {

    private Bitmap b;
    private Texture tex;

    public Background(Context context, int id, int screenWidth, int screenHeight) {
        b = loadBitmap(context, id, screenWidth, screenHeight, true);

        tex = new Texture(id, 0, 0, b.getWidth(), b.getHeight(), 255, null);
        tex.setTranslation(0, (screenHeight - tex.getHeight()) / 2);
        tex.recomputeCoordinateMatrix();
    }

    private Bitmap loadBitmap(Context context, int id, int dstWidth, int dstHeight, boolean adjustRatio) {
        int targetWidth, targetHeight;

        // Decode the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);

        if (adjustRatio) {
            // Adjust the aspect ratio
            float imageRatio = bitmap.getWidth() / bitmap.getHeight();

            if (dstWidth > dstHeight) {
                targetWidth = (int) (imageRatio * dstHeight);
                targetHeight = dstHeight;
            } else {
                targetWidth = dstWidth;
                targetHeight = (int) (imageRatio * (dstWidth / imageRatio));
            }
        } else {
            targetWidth = dstWidth;
            targetHeight = dstHeight;
        }

       return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(b, tex.getMatrix(), tex.getP());
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
