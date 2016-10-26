package teamdroid.com.speedtestarena.io;

import android.view.MotionEvent;

/**
 * Created by Kenny on 2016-10-26.
 */

public class GameTest1Event {
    public MotionEvent e;
    public long songTime;

    public GameTest1Event(MotionEvent e, long songTime) {
        this.e = e;
        this.songTime = songTime;
    }
}
