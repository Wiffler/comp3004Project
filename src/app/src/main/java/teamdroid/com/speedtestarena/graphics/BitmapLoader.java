package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Hashtable;

/**
 * Created by Kenny on 2016-10-21.
 */

public class BitmapLoader {

    private Hashtable<Integer, Bitmap> bitmapTable;

    public BitmapLoader() {
        bitmapTable = new Hashtable<Integer, Bitmap>();
    }

    public int loadBitmap(Context context, int id) {
        bitmapTable.put(id, BitmapFactory.decodeResource(context.getResources(), id));
        return id;
    }

    public int loadBitmap(Context context, int id, int dstWidth, int dstHeight, boolean adjustRatio) {
        int targetWidth, targetHeight;

        // Decode the bitmap
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id);

        if (adjustRatio) {
            // Adjust the aspect ratio
            float imageRatio = b.getWidth() / b.getHeight();

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

        bitmapTable.put(id, Bitmap.createScaledBitmap(b, targetWidth, targetHeight, true));
        return id;
    }

    public void loadBitmapList(Context context, int[] id) {
        for (int i = 0; i < id.length; i++) {
            bitmapTable.put(id[i], BitmapFactory.decodeResource(context.getResources(), id[i]));
        }
    }

    public void unloadBitmap(int id) {
        bitmapTable.remove(id);
    }

    public Bitmap getBitmap(int id) {
        return bitmapTable.get(id);
    }

    public void clear() {
        bitmapTable.clear();
    }
}
