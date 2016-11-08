package teamdroid.com.speedtestarena;

import android.app.Application;

/**
 * Created by Alan on 2016-10-24.
 */

/* purpose of this class is to debug the screen where all the buttons are stacking ontop of each other when rotating the
   android mobile device */

public class configchanges extends Application {

    private int i = 0;

    public int inc() {
        return ++i;
    }
}
