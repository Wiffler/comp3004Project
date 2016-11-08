package teamdroid.com.speedtestarena.game.MusicGame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import teamdroid.com.speedtestarena.R;

public class MusicGameMenuActivity extends AppCompatActivity {

    private ListView songSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_game_menu);

        // Create array adapter
        String[] mylist = {"test_sound_file2", "test_sound_file3", "test_sound_file4"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.textview_play_menu, mylist);

        // Create the listview
        createListView(adapter);
    }

    private void createListView(ArrayAdapter selectionList) {
        // Create the list view and set the adapter
        songSelection = (ListView) findViewById(R.id.musicgameSelection);
        songSelection.setAdapter(selectionList);

        // Set listener for the listview
        songSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = (String) songSelection.getItemAtPosition(position);

                if (listItem == "test_sound_file2") {
                    startMusicGameActivity(R.raw.test_sound_file2,
                            R.raw.test_sound_file2_sm,
                            R.drawable.test_sound_file2_bg);

                } else if (listItem == "test_sound_file3") {
                    startMusicGameActivity(R.raw.test_sound_file3,
                            R.raw.test_sound_file3_sm,
                            R.drawable.test_sound_file3_bg);

                } else if (listItem == "test_sound_file4") {
                    startMusicGameActivity(R.raw.test_sound_file4,
                            R.raw.test_sound_file4_sm,
                            R.drawable.test_sound_file4_bg);
                }
            }
        });
    }

    private void startMusicGameActivity(int audioID, int simID, int bgID) {
        // Create the Bundle
        Bundle b = new Bundle();
        b.putInt("AudioID", audioID);
        b.putInt("SimID", simID);
        b.putInt("BGID", bgID);

        // Create the Intent
        Intent intent = new Intent(MusicGameMenuActivity.this, MusicGameActivity.class);
        intent.putExtras(b);

        // Start the activity
        startActivity(intent);
    }
}
