package teamdroid.com.speedtestarena.sound;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Kenny on 2016-10-26.
 * Thread dies when the parent thread terminates.
 * Stays alive in the background until the main game thread dies.
 */

public class AudioDelayThread extends Thread {
    public Handler delayHandler;

    public volatile boolean ready = false;

    public AudioDelayThread() {}

    @Override
    public void run() {
        Looper.prepare();

        delayHandler = new Handler();
        ready = true;

        Looper.loop();
    }
}
