package teamdroid.com.speedtestarena.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Hashtable;

/**
 * Created by Kenny on 2016-10-21.
 */

public class TextureLoader {

    private Hashtable<Integer, Bitmap> bitmapTable;

    public TextureLoader() {
        bitmapTable = new Hashtable<Integer, Bitmap>();
    }

    public int loadTexture(Context context, int id) {
        bitmapTable.put(id, BitmapFactory.decodeResource(context.getResources(), id));
        return id;
    }

    public void unloadTexture(int id) {
        bitmapTable.remove(id);
    }

    public Bitmap getTexture(int id) {
        return bitmapTable.get(id);
    }

    public void clear() {
        bitmapTable.clear();
    }
}
