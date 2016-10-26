package teamdroid.com.speedtestarena.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import teamdroid.com.speedtestarena.utility.GameTimer;

import static java.lang.Math.min;

/**
 * Created by Kenny on 2016-10-21.
 */

public class GameAudio {
    private MediaPlayer mp = null;

    // Manually implement start delay
    private volatile boolean start = false;
    private volatile long startTime = 0;
    private long delay = 0;
    private GameTimer g = null;

    public GameAudio() {
        super();
    }

    public GameAudio(GameTimer time) {
        super();
        g = time;
    }

    public void createAudio(Context activity, int resID) {
        mp = MediaPlayer.create(activity, resID);
    }

    public void startAudio() {
        if (mp != null) {
            mp.start();
        }
    }

    public void startAudio(long delay, Handler delayHandler) {
        if (mp != null) {
            this.delay = delay;
            startTime = g.getTime();
            start = true;

            delayHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            mp.start();
                        }
                    },
                    delay);
        }
    }

    public void pauseAudio() {
        if (mp != null) {
            mp.pause();
        }
    }

    public void stopAudio() {
        if (mp != null) {
            mp.stop();
        }
    }

    public long getPosition() {
        if (mp != null) {
            if (mp.getCurrentPosition() <= 0 && start) {
                return min((g.getTime() - startTime) - delay, 0l);
            } else {
                return mp.getCurrentPosition();
            }
        } else {
            return -2;
        }
    }

    public long getDuration() {
        if (mp != null) {
            return mp.getDuration();
        } else {
            return -2;
        }
    }

    public void cleanup() {
        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
    }
}
