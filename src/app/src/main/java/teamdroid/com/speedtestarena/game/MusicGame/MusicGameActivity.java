package teamdroid.com.speedtestarena.game.MusicGame;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import teamdroid.com.speedtestarena.graphics.Background;

public class MusicGameActivity extends AppCompatActivity {

    private int songID, simID, bgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the arguments from the bundle
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            songID = b.getInt("AudioID");
            simID = b.getInt("SimID");
            bgID = b.getInt("BGID");
        }

        setFullScreen();
        setContentView(new MusicGameView(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setGameBG(final MusicGameView surface, final Background bg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    surface.setBackground(bg);
                } else {
                    surface.setBackgroundDrawable(bg);
                }
            }
        });
    }

    // Getters
    public int getSongID() {
        return songID;
    }

    public int getSimID() {
        return simID;
    }

    public int getBGID() {
        return bgID;
    }

    public void setFullScreen() {
        // Get rid of title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get rid of action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        // get rid of navigation bar
        if ((Build.VERSION.SDK_INT > 11) && (Build.VERSION.SDK_INT < 19)) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);

        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
