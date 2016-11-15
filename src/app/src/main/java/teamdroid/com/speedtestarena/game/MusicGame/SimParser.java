package teamdroid.com.speedtestarena.game.MusicGame;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kenny on 2016-10-25.
 */

public class SimParser {

    // state = 0 : searching for OFFSET
    // state = 1 : searching for BPMS
    // state = 2 : searching for NOTES
    // state = 3 : reading beats
    // state = 4 : termination state
    private int parseState = 0;

    public SimParser() {}

    public void readSM(Context context, int resID, HitMap mapper) {
        // Reset the parser state
        parseState = 0;

        // Read text from .sm file
        InputStream in = context.getResources().openRawResource(resID);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int measureCounter = 0;

            //System.out.println("Starting parser...");

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#OFFSET:")) {
                    // Check the parser state
                    if (parseState == 0) {
                        mapper.setOffset((long) (Float.parseFloat(line.substring(8, line.length() - 1)) * 1000));
                        parseState += 1;
                        //System.out.println(line.substring(8, line.length() - 1));
                        //System.out.println("PARSE STATE: " + parseState);
                    }
                }
                else if (line.startsWith("#BPMS:")) {
                    // Check the parser state
                    if (parseState == 1) {
                        parseBPMS(line, mapper);
                        parseState += 1;
                        //System.out.println("PARSE STATE: " + parseState);
                    }
                }
                else if (line.startsWith("#NOTES:")) {
                    parseState += 1;
                    //System.out.println("PARSE STATE: " + parseState);
                }
                else if (line.matches("^[01234MKLF][01234MKLF][01234MKLF][01234MKLF]") && (parseState == 3)) {
                    readNoteCode(line, mapper);
                    measureCounter += 1;
                }
                else if (line.startsWith(",") && (parseState == 3)) {
                    //System.out.println(measureCounter);
                    mapper.addMeasureMap(measureCounter);
                    measureCounter = 0;
                }
                else if (line.startsWith(";") && (parseState == 3)) {
                    mapper.addMeasureMap(measureCounter);
                    measureCounter = 0;
                    parseState += 1;
                }

                if (parseState >= 4) {
                    break;
                }
            }

            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseBPMS(String line, HitMap map) {
        /*
         0 -> reading note number, start state
         1 -> reading bpms
         2 -> termination  state
         Read '=' -> transition from 0 to 1
         Read ',' -> transition from 1 to 0
         Read ';' -> transition to 2
         Read [0-9\.] -> no state transition
         */
        int state = 0;

        // Parsing variables
        String input = line.substring(6);
        String num = "";
        char c;
        int noteNum = 0;
        float bpms = 0f;

        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if ((c >= '0' && c <= '9') || (c == '.')) {
                num += c;
            } else if (c == '=') {
                noteNum = (int) Float.parseFloat(num);
                num = "";
                state = 1;
            } else if (c == ',') {
                bpms = Float.parseFloat(num);
                num = "";
                map.addBPMSMap(noteNum, bpms);
                state = 0;
            } else if (c == ';') {
                bpms = Float.parseFloat(num);
                num = "";
                map.addBPMSMap(noteNum, bpms);
                state = 2;
            }

            if (state == 2) {
                break;
            }
        }
    }

    private void readNoteCode(String line, HitMap map) {
        /*
         d -> 1, 2, 3, 4, M, K, L, F
         0000 -> 0
         d000 -> 1
         0d00 -> 2
         00d0 -> 3
         000d -> 4
         dd00 -> 5
         d0d0 -> 6
         d00d -> 7
         0dd0 -> 8
         0d0d -> 9
         00dd -> 10
         ddd0 -> 11
         dd0d -> 12
         d0dd -> 13
         0ddd -> 14
         dddd -> 15
         */
        if (line.matches("^[0][0][0][0]")) {
            map.addSpawnMap(0);
        } else if (line.matches("^[1234MKLF][0][0][0]")) {
            map.addSpawnMap(1);
        } else if (line.matches("^[0][1234MKLF][0][0]")) {
            map.addSpawnMap(2);
        } else if (line.matches("^[0][0][1234MKLF][0]")) {
            map.addSpawnMap(3);
        } else if (line.matches("^[0][0][0][1234MKLF]")) {
            map.addSpawnMap(4);
        } else if (line.matches("^[1234MKLF][1234MKLF][0][0]")) {
            map.addSpawnMap(5);
        } else if (line.matches("^[1234MKLF][0][1234MKLF][0]")) {
            map.addSpawnMap(6);
        } else if (line.matches("^[1234MKLF][0][0][1-9]")) {
            map.addSpawnMap(7);
        } else if (line.matches("^[0][1234MKLF][1234MKLF][0]")) {
            map.addSpawnMap(8);
        } else if (line.matches("^[0][1234MKLF][0][1234MKLF]")) {
            map.addSpawnMap(9);
        } else if (line.matches("^[0][0][1234MKLF][1234MKLF]")) {
            map.addSpawnMap(10);
        } else if (line.matches("^[1234MKLF][1234MKLF][1234MKLF][0]")) {
            map.addSpawnMap(11);
        } else if (line.matches("^[1234MKLF][1234MKLF][0][1234MKLF]")) {
            map.addSpawnMap(12);
        } else if (line.matches("^[1234MKLF][0][1234MKLF][1234MKLF]")) {
            map.addSpawnMap(13);
        } else if (line.matches("^[0][1234MKLF][1234MKLF][1234MKLF]")) {
            map.addSpawnMap(14);
        } else if (line.matches("^[1234MKLF][1234MKLF][1234MKLF][1234MKLF]")) {
            map.addSpawnMap(15);
        }
    }
}
