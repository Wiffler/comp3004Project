package teamdroid.com.speedtestarena.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.SoundPool;
import android.util.SparseIntArray;

import teamdroid.com.speedtestarena.R;

/**
 * Created by Kenny on 2016-10-17.
 */

/* TODO:
    See if checking the bit rate is necessary.
 */

public class CanvasTestSoundPoolThread extends Thread {
    // flag to hold run state
    private volatile boolean running = false;

    private static SoundPool soundPool;
    private static SparseIntArray soundPoolMap;
    private static SparseIntArray streamIDMap;
    //private static float maxRate;

    public static final int S1 = R.raw.metronome1_sound_file, S2 = R.raw.click1_sound_file;

    public CanvasTestSoundPoolThread(Context context) {
        super();

        initSounds(context);
    }

    @TargetApi(21)
    public static void initSounds(Context context) {
        // Create the SoundPool
        if((android.os.Build.VERSION.SDK_INT) == 21){
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(5);
            builder.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            soundPool = builder.build();

        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        // Create the array to store the audio
        soundPoolMap = new SparseIntArray(2);
        soundPoolMap.put(S1, soundPool.load(context, R.raw.metronome1_sound_file, 1));
        soundPoolMap.put(S2, soundPool.load(context, R.raw.click1_sound_file, 2));

        // Create the array to store the streamIds
        streamIDMap = new SparseIntArray(2);
        streamIDMap.put(S1, 0);
        streamIDMap.put(S2, 0);

        // Get the hardware supported rates
        /*
        maxRate = 0;
        for (int rate : new int[] {8000, 11025, 16000, 22050, 44100}) {  // add the rates you wish to check against
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                // buffer size is valid, Sample rate supported
                if (rate > maxRate) {
                    maxRate = rate;
                }
            }
        }
        */
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void startAudio(int soundID) {
        float volume = 1;
        if(soundPool != null && soundPoolMap != null) {
            /* arguments:
                int soundID i.e. R.raw.test_sound_file1
                float leftVolume
                float rightVolume
                int priority
                int loop
                float rate *need to get hardware supported rate
            */
            streamIDMap.put(soundID, soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f));
        }
    }

    public void pauseAudio(int soundID) {
        if(soundPool != null && soundPoolMap != null) {
            soundPool.pause(streamIDMap.get(soundID));
        }
    }

    @Override
    public void run() {
        while (this.running) {

        }

        soundPool.release();
        soundPool = null;
    }
}
