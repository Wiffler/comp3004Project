package teamdroid.com.speedtestarena.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import teamdroid.com.speedtestarena.utility.GameTimer;

import static java.lang.Math.min;

/**
 * Created by Kenny on 2016-10-21.
 */

public class GameAudio {
    private final Lock audioLock = new ReentrantLock(true);

    private volatile MediaPlayer mp = null;

    // song info
    private int songDuration;

    // Manually implement start delay
    private volatile boolean start = false;
    private volatile long startTime = 0;
    private long delay = 0;
    private GameTimer g = null;

    // Constructors
    public GameAudio() {
        super();
    }

    public GameAudio(GameTimer time) {
        super();
        g = time;
    }

    public void createAudio(Context activity, int resID) {
        loadAudio(activity, resID);
    }

    public void loadAudio(Context activity, int resID) {
        mp = MediaPlayer.create(activity, resID);
        songDuration = mp.getDuration();
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
        long retVal;

        if (mp != null) {
            audioLock.lock();
            if (mp.getCurrentPosition() <= 0 && start) {
                retVal = min((g.getTime() - startTime) - delay, 0l);
            } else {
                retVal = mp.getCurrentPosition();
            }
            audioLock.unlock();
            return retVal;
        } else {
            return -1;
        }
    }

    public long getDuration() {
        if (mp != null) {
            return songDuration;
        } else {
            return -1;
        }
    }

    public void cleanup() {
        if (mp != null) {
            audioLock.lock();
            mp.reset();
            mp.release();
            mp = null;
            audioLock.unlock();
        }
    }
}
