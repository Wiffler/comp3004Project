package teamdroid.com.speedtestarena.game.TypingGame;

/**
 * Created by Alan on 2016-10-21.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;

import teamdroid.com.speedtestarena.R;

public class TypingGame extends AppCompatActivity {
    TextView mainTextView;
    TextView msgTextView;
    EditText userInput;
    Button buttonSubmit;
    Button buttonQuit;
    Button buttonLevel;

    private String inputString;
    private String testString;

    public HashMap<String, Player> userList = new HashMap<String,Player>();
    // call Player.java
    Player player;

    private int level;

    // switch level in runtime, to reset track of lowest time
    private int score;

    // call SentenceGenerator.java
    SentenceGenerator sentence;

    // Count Time elapsed
    private long startTime;
    private long stopTime;

    // Initialize Dialog variable
    private static final int GAMESTART_DIALOG = 1;
    private static final int GAMEPLAY_DIALOG = 2;
    private static final int INCORRECT_DIALOG = 3;
    private static final int CHANGE_DIALOG = 4;

    // keep track of lowest time
    private double lowestTime = 1000.;
    // keep track of slowest time
    private double slowestTime = 0.;

    // keep track of times played
    private int[] count = new int[]{0,0,0,0,0,0,0,0,0,0,0};

    // Image switch
    ImageView configIcon;
    // Initialize timer
    Timer timer;

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == GAMESTART_DIALOG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            // this is the message to display
            builder.setMessage("Are you ready to type?");
            // this is the button to display
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                // this is the method to call when the button is clicked
                public void onClick(DialogInterface dialog, int id) {
                    // start counting
                    startTime = System.currentTimeMillis();

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            return builder.create();
        } else if (id == GAMEPLAY_DIALOG) {

            // get stop time
            stopTime = System.currentTimeMillis();

            //calculate time elapsed
            double timePeriod = (stopTime - startTime)/1000.;

            // indicator whether beat the best time so far
            boolean flag; // achieve best performance

            if(lowestTime > timePeriod) {
                slowestTime = timePeriod;
                // updateBestTime method is in Player.java
                player.updateBestTime(score, lowestTime);

                flag = true; // not beat the best time
            } else {
                flag = false;
            }

            // record slowest time
            if (timePeriod > slowestTime) {
                slowestTime = timePeriod;
                // updateWorseTime method is in Player.java
                player.updateWorstTime(slowestTime);
            }

            // round the result
            final String lowest = String.format("%.1f", lowestTime);
            String result = String.format("%.1f", timePeriod);
            msgTextView.setText("Correct! " + result + " seconds passed.");
            String outputMessage;

            // decided whether it's first time playing this game

            if(count[level] == 0) {
                // Output String on Dialog when achieve best performance
                outputMessage = "You achieve the best performance ever!\n" + "Your new record is " + result + " seconds."
                        + "\nPress Yes! to continue";
            } else {
                // beat
                if(flag) {
                    // Output String on Dialog when achieve best performance
                    outputMessage = "You achieve the best performance ever!\n" + "The new record is " + result + " seconds."
                            + "\n Press Yes! to continue";
                } else {
                    // Output String on Dialog when not beat
                    outputMessage = "Correct! But not fast enough. \nBest performance: " + lowest + " seconds.\n"
                            + "It took you " + result + " seconds." + "\n Press Yes! to continue";
                }
            }

            // build new dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // this is the message to display
            builder.setMessage(outputMessage);
            // this is the button to display
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // reset counting
                    startTime = System.currentTimeMillis();

                    // reset EditText, msgText
                    userInput.setText("");
                    userInput.setHint("Please input the sentence above! ");

                    // set next sentence
                    // sentenceGenerator method is in SentenceGenerator.java
                    String testSentence = sentence.sentenceGenerator(level);

                    mainTextView.setText(testSentence);

                    // terminate any timer
                    if(timer != null) {
                        timer.cancel();
                    }

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            return builder.create();
        } else if (id == INCORRECT_DIALOG) {
            // Output String on Dialog
            String outputIncorrectMessage = "Your Input is Incorrect.\n Click Yes! to continue playing. ";

            // build new dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // this is the message to display
            builder.setMessage(outputIncorrectMessage);
            // this is the button to display
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // reset counting
                    startTime = System.currentTimeMillis();

                    // reset EditText, msgText
                    userInput.setText("");
                    userInput.setHint("Please input the sentence above. ");

                    // set next sentence
                    // sentenceGenerator method is in SentenceGenerator.java
                    String testSentence = sentence.sentenceGenerator(level);
                    mainTextView.setText(testSentence);

                    // terminate any timer
                    if (timer != null) {
                        timer.cancel();
                    }

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            return builder.create();
        } else if (id == CHANGE_DIALOG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select a Level");

            // this is the button to display
            builder.setMessage("Which level would you like to switch to? ");
            // this is the button to display
            builder.setNegativeButton("Easy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    // choose easy level
                    // setLevel method is in Player.java
                    player.setLevel("easy");
                    // getLevel method is in Player.java
                    buttonLevel.setText(player.getLevel());

                    // reset counting
                    startTime = System.currentTimeMillis();

                    // reset EditText, msgText
                    userInput.setText("");
                    userInput.setHint("Please input the sentence above. ");
                    msgTextView.setText("Please input the sentence above. ");

                    // set next sentence
                    level = 1;

                    // load existing lowestTime, if null set 1000/0
                    // getBestTimeHash method is in Player.java
                    if(player.getBestTimeHash().get("easy") != null) {
                        lowestTime = player.getBestTimeHash().get("easy");
                        // getWorstTimeHash method is in Player.java
                        slowestTime = player.getWorstTimeHash().get("easy");
                        Toast.makeText(getApplicationContext(), "LEVEL CHANGE", Toast.LENGTH_LONG).show();
                    } else {
                        lowestTime = 1000.;
                        slowestTime = 0.;
                    }

                    // sentenceGenerator method is in SentenceGenerator.java
                    String testSentence = sentence.sentenceGenerator(level);

                    // terminate any timer
                    if(timer != null) {
                        timer.cancel();
                    }

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Medium", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    // choose easy level
                    // setLevel method is in Player.java
                    player.setLevel("medium");
                    // getLevel method is in Player.java
                    buttonLevel.setText(player.getLevel());

                    // reset counting
                    startTime = System.currentTimeMillis();

                    // reset EditText, msgText
                    userInput.setText("");
                    userInput.setHint("Please input the sentence above. ");
                    msgTextView.setText("Please input the sentence above. ");

                    // set next sentence
                    level = 2;

                    // load existing lowestTime, if null set 1000/0
                    // getBestTimeHash method is in Player.java
                    if(player.getBestTimeHash().get("medium") != null) {
                        lowestTime = player.getBestTimeHash().get("medium");
                        // getWorstTimeHash method is in Player.java
                        slowestTime = player.getWorstTimeHash().get("medium");
                        Toast.makeText(getApplicationContext(), "LEVEL CHANGE", Toast.LENGTH_LONG).show();
                    } else {
                        lowestTime = 1000.;
                        slowestTime = 0.;
                    }

                    // sentenceGenerator method is in SentenceGenerator.java
                    String testSentence = sentence.sentenceGenerator(level);

                    mainTextView.setText(testSentence);

                    // terminate any timer
                    if(timer != null) {
                        timer.cancel();
                    }

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Hard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    // choose easy level
                    // setLevel method is in Player.java
                    player.setLevel("hard");
                    // getLevel method is in Player.java
                    buttonLevel.setText(player.getLevel());

                    // reset counting
                    startTime = System.currentTimeMillis();

                    // reset EditText, msgText
                    userInput.setText("");
                    userInput.setHint("Please input the sentence above. ");
                    msgTextView.setText("Please input the sentence above. ");

                    // set next sentence
                    level = 3;

                    // load existing lowestTime, if null set 1000/0
                    // getBestTimeHash method is in Player.java
                    if(player.getBestTimeHash().get("hard") != null) {
                        lowestTime = player.getBestTimeHash().get("hard");
                        // getWorstTimeHash method is in Player.java
                        slowestTime = player.getWorstTimeHash().get("hard");
                        Toast.makeText(getApplicationContext(), "LEVEL CHANGE", Toast.LENGTH_LONG).show();
                    } else {
                        lowestTime = 1000.;
                        slowestTime = 0.;
                    }

                    // sentenceGenerator method is in SentenceGenerator.java
                    String testSentence = sentence.sentenceGenerator(level);

                    mainTextView.setText(testSentence);

                    // terminate any timer
                    if(timer != null) {
                        timer.cancel();
                    }

                    // this will hide the dialog
                    dialog.cancel();
                }
            });
            return builder.create();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typinggame);

        // Create player object
        player = new Player();

        // choose level
        // getLevel method is in Player.java
        player.setLevel("easy");

        if(player.getLevel().equals("easy")) {
            level = 1;
        } else if (player.getLevel().equals("medium")) {
            level = 2;
        } else {
            level = 3;
        }

        // initialize random sentence
        // create SentenceGenerator object
        sentence = new SentenceGenerator();

        // sentenceGenerator method is in SentenceGenerator.java
        final String testSentence = sentence.sentenceGenerator(level);

        count[level] = 0;

        // Access the TextView defined in layout XML and then set its text
        mainTextView = (TextView) findViewById(R.id.introduction);
        mainTextView.setText(testSentence);

        msgTextView = (TextView)findViewById(R.id.feedback);
        msgTextView.setText("Click the SUBMIT button when finish!");

        // Access player input defined in XML
        userInput = (EditText)findViewById(R.id.user_input);

        buttonSubmit = (Button)findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            // handler for clicking on submit button
            public void onClick(View view) {

                // get player input string
                inputString = userInput.getText().toString();

                // get selected string
                testString = mainTextView.getText().toString();

                // compare above two strings
                if (inputString.trim().equalsIgnoreCase(testString)) {
                    // show correct response
                    String outputMessage = " Correct";
                    Toast.makeText(getApplicationContext(), outputMessage, Toast.LENGTH_SHORT).show();
                    // enable player to reset timer
                    removeDialog(GAMEPLAY_DIALOG);
                    showDialog(GAMEPLAY_DIALOG);
                }

                // increase count
                count[level] += 1;
            }
        });

        buttonQuit = (Button)findViewById(R.id.button_quit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            // handler for clicking on quit button
            public void onClick(View view) {
                // exit program
                System.exit(0);
            }
        });

        // pick level during runtime
        buttonLevel = (Button)findViewById(R.id.button_level);
        buttonLevel.setText(player.getLevel());
        buttonLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDialog(CHANGE_DIALOG);
                showDialog(CHANGE_DIALOG);
            }
        });

        // show dialog on start of activity
        showDialog(GAMESTART_DIALOG);
    }
}