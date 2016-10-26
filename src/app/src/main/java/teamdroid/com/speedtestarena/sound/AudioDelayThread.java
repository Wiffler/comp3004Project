package teamdroid.com.speedtestarena.sound;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Kenny on 2016-10-26.
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
        /*
        delayHandler = new Handler() {
            public void handleMessage(Message msg) {
                // process incoming messages here
                switch (msg.arg1) {
                    case 0:

                        break;
                }
            }
        };
        */

        Looper.loop();
    }
}
