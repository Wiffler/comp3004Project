package teamdroid.com.speedtestarena.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import teamdroid.com.speedtestarena.R;

/**
 * Created by Kenny on 2016-10-21.
 */

public class GameAudio {
    MediaPlayer mp = null;

    public GameAudio() {
        super();
    }

    public void createAudio(Context activity, int resID) {
        mp = MediaPlayer.create(activity, resID);
    }

    public void startAudio() {
        if (mp != null) {
            mp.start();
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

    public void cleanup() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
