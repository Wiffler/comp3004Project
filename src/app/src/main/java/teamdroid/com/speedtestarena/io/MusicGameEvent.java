package teamdroid.com.speedtestarena.io;

import android.view.MotionEvent;

/**
 * Created by Kenny on 2016-10-26.
 */

public class MusicGameEvent {
    public MotionEvent e;
    public long songTime;

    public MusicGameEvent(MotionEvent e, long songTime) {
        this.e = e;
        this.songTime = songTime;
    }
}
