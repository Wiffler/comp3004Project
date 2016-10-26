package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kenny on 2016-10-25.
 */

public class SMParser {

    // state = 0 : searching for OFFSET
    // state = 1 : searching for BPMS
    // state = 2 : searching for NOTES
    // state = 3 : reading beats
    // state = 4 : termination state
    private int parseState = 0;

    private HitMap mapper = null;

    public SMParser() {
    }

    public void readSM(Context context, int resID, HitMap mapper) {
        InputStream in = context.getResources().openRawResource(resID);
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int measureCounter = 0;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#OFFSET:")) {
                    //System.out.println(line.substring(8, line.length() - 1));
                    mapper.setOffset((long) (Float.parseFloat(line.substring(8, line.length() - 1)) * 1000));
                    parseState += 1;
                }
                else if (line.startsWith("#BPMS:")) {
                    // hardcode for now
                    //System.out.println(line.substring(12, 19));
                    mapper.setBPMS(Double.parseDouble(line.substring(12, 19)));
                    parseState += 1;
                }
                else if (line.startsWith("#NOTES:")) {
                    parseState += 1;
                }
                else if (line.matches("^[0-9][0-9][0-9][0-9]")) {
                    /*
                    d -> 1, 2, 3, 4, 5, 6, 7, 8, 9
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
                        mapper.addSpawnMap(0);
                    } else if (line.matches("^[1-9][0][0][0]")) {
                        mapper.addSpawnMap(1);
                    } else if (line.matches("^[0][1-9][0][0]")) {
                        mapper.addSpawnMap(2);
                    } else if (line.matches("^[0][0][1-9][0]")) {
                        mapper.addSpawnMap(3);
                    } else if (line.matches("^[0][0][0][1-9]")) {
                        mapper.addSpawnMap(4);
                    } else if (line.matches("^[1-9][1-9][0][0]")) {
                        mapper.addSpawnMap(5);
                    } else if (line.matches("^[1-9][0][1-9][0]")) {
                        mapper.addSpawnMap(6);
                    } else if (line.matches("^[1-9][0][0][1-9]")) {
                        mapper.addSpawnMap(7);
                    } else if (line.matches("^[0][1-9][1-9][0]")) {
                        mapper.addSpawnMap(8);
                    } else if (line.matches("^[0][1-9][0][1-9]")) {
                        mapper.addSpawnMap(9);
                    } else if (line.matches("^[0][0][1-9][1-9]")) {
                        mapper.addSpawnMap(10);
                    } else if (line.matches("^[1-9][1-9][1-9][0]")) {
                        mapper.addSpawnMap(11);
                    } else if (line.matches("^[1-9][1-9][0][1-9]")) {
                        mapper.addSpawnMap(12);
                    } else if (line.matches("^[1-9][0][1-9][1-9]")) {
                        mapper.addSpawnMap(13);
                    } else if (line.matches("^[0][1-9][1-9][1-9]")) {
                        mapper.addSpawnMap(14);
                    } else if (line.matches("^[1-9][1-9][1-9][1-9]")) {
                        mapper.addSpawnMap(15);
                    }

                    measureCounter += 1;
                }
                else if (line.startsWith(",") && (parseState == 3)) {
                    mapper.addMeasureMap(measureCounter);
                    measureCounter = 0;
                }
                else if (line.startsWith(";") && (parseState == 3)) {
                    mapper.addMeasureMap(measureCounter);
                    measureCounter = 0;
                    parseState += 1;
                    //System.out.println(line);
                }

                if (parseState >= 4) {
                    break;
                }
            }

            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }
}
