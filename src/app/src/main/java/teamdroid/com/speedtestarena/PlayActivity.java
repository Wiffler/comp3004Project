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

    ListView gameSelection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create array adapter
        String[] mylist = {"Canvas Test", "Game2"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_play_textview, mylist);

        // Create the list view and set the adapter
        gameSelection = (ListView) findViewById(R.id.GameSelection_ListView);
        gameSelection.setAdapter(adapter);

        // Set listener for the listview
        gameSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = gameSelection.getItemAtPosition(position);

                if ((String) listItem == "Canvas Test") {
                    startActivity(new Intent(PlayActivity.this, CanvasTestActivity.class));
                }
            }
        });
    }
}
