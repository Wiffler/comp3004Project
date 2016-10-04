package teamdroid.com.speedtestarena;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView gameSelection = (ListView) findViewById(R.id.GameSelection_ListView);
        String[] mylist = {"hi", "bye"};
        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, ,mylist);
        //gameSelection.setAdapter(myAdapter);
    }
}
