package teamdroid.com.speedtestarena;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

        // Create array adapter
        String[] mylist = {"Game1", "Game2"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_play_textview, mylist);

        //System.out.println("Hi");

        // Create the list view
        final ListView gameSelection = (ListView) findViewById(R.id.GameSelection_ListView);
        gameSelection.setAdapter(adapter);


        // Set listener
        gameSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = gameSelection.getItemAtPosition(position);
                startActivity(new Intent(PlayActivity.this, CanvasTestActivity.class));
            }
        });
    }

    /*protected void onResume() {
        //gameSelection.myTextView.setFocusable(true);
    }*/
}
