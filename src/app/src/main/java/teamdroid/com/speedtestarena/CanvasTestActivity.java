package teamdroid.com.speedtestarena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CanvasTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new CanvasTest(this));

        //setContentView(R.layout.activity_canvas_test);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //startActivity(new Intent(MainActivity.this, PlayActivity.class));
    }

    @Override
    protected void onStop() {
        //Log.d(TAG, "Stopping...");
        super.onStop();
    }
}
