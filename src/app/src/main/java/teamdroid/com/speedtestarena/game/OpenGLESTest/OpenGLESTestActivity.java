package teamdroid.com.speedtestarena.game.OpenGLESTest;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class OpenGLESTestActivity extends AppCompatActivity {

    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        glView = new OpenGLESTestView(this);
        setContentView(glView);
    }

    @Override
    protected void onDestroy() {
        System.out.println("Exiting game activity.");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        System.out.println("Pausing game activity.");
        super.onStop();
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
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
