package teamdroid.com.speedtestarena.sound;

import android.content.Context;
import android.media.MediaPlayer;

import teamdroid.com.speedtestarena.R;

/**
 * Created by Kenny on 2016-10-17.
 */

/* TODO:
    Figure out if playing multiple streams is possible.
 */

public class CanvasTestAudioThread extends Thread {
    // flag to hold run state
    private volatile boolean running = false;

    private volatile boolean play = false;
    MediaPlayer mp = null;

    public CanvasTestAudioThread(Context context) {
        super();
        mp =  MediaPlayer.create(context, R.raw.test_sound_file1);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void startAudio() {
        play = true;
    }

    public void pauseAudio() {
        if ((mp != null) && mp.isPlaying()) {
            mp.pause();
        }
    }

    @Override
    public void run() {
        while (this.running) {
            if (play) {
                if (mp != null) {
                    mp.start();
                }
                play = false;
            }
        }

        mp.release();
        mp = null;
    }
}
