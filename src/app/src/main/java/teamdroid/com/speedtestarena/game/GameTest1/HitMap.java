package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;

import java.util.ArrayList;

import teamdroid.com.speedtestarena.R;

/**
 * Created by Kenny on 2016-10-25.
 */

public class HitMap {

    private static SMParser parser = new SMParser();

    private double bpms = 0;
    private long offset = 0;

    public float[] spawnLocX = {200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800,
                                200, 400, 600, 800, 200, 400, 600, 800};
    public float[] spawnLocY = {300, 300, 300, 300,
                                400, 400, 400, 400,
                                500, 500, 500, 500,
                                600, 600, 600, 600,
                                700, 700, 700, 700,
                                800, 800, 800, 800,
                                900, 900, 900, 900,
                                1000, 1000, 1000, 1000,
                                1100, 1100, 1100, 1100,
                                1200, 1200, 1200, 1200,
                                1300, 1300, 1300, 1300,
                                1400, 1400, 1400, 1400,
                                1500, 1500, 1500, 1500,
                                1600, 1600, 1600, 1600,
                                1700, 1700, 1700, 1700,
                                1800, 1800, 1800, 1800};

    private ArrayList<Integer> spawnMap;
    public int spawnMapIndex = 0;

    private ArrayList<Integer> measureMap;
    public volatile int measureMapIndex = 0;

    public ArrayList<Long> hitcircleSpawnTimes;
    public ArrayList<Long> hitcircleDeathTimes;
    public ArrayList<Integer> hitcircleLocation;
    public volatile int spawnTimeIndex;

    public HitMap() {
        spawnMap = new ArrayList<Integer>();
        measureMap = new ArrayList<Integer>();
        hitcircleSpawnTimes = new ArrayList<Long>();
        hitcircleDeathTimes = new ArrayList<Long>();
        hitcircleLocation = new ArrayList<Integer>();
    }

    // Setters
    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setBPMS(double bpms) {
        this.bpms = bpms;
    }

    public void addSpawnMap(int code) {
        spawnMap.add(code);
    }

    public int readSpawnMap() {
        // improve later
        int result = spawnMap.get(spawnMapIndex);
        spawnMapIndex++;
        return result;
    }

    // Measure Map
    public void addMeasureMap(int code) {
        measureMap.add(code);
    }

    public int readMeasureMap(int index) {
        // improve later
        return measureMap.get(index);
    }

    public int measureMapSize() {
        return measureMap.size();
    }

    // Initialization
    public void initialise(Context context) {
        // Parse the sound map
        parser.readSM(context, R.raw.test_sound_file2_sm, this);

        // print the measure map:
        /*
        for (int i = 0; i < measureMap.size(); i++) {
            System.out.println("i: " + i + " measurecount: " + measureMap.get(i));
        }
        */

        // set the spawn times
        int vinterval = 0;
        int measureIndex = 0;
        int measureCounter = 0;
        int measureLength = measureMap.get(measureIndex);
        int vintervalMax = (measureLength / 4) - 1;
        double bmpsInterval = 60000 / bpms;
        long musicTime = 0;

        // test
        offset = -1 * offset;

        System.out.println("Beat count: " + spawnMap.size());

        // go through the notes
        for (int i = 0; i < spawnMap.size(); i++) {
            int code = spawnMap.get(i);

            //System.out.println("i: " + i + " BPMS: " + bpms + " musicTime: " + musicTime + " Offset: " + offset + " Start Time : " + (musicTime + offset));

            switch (code) {
                case 0: // 0000
                    break;
                case 1: // 1000
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    break;
                case 2: // 0100
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    break;
                case 3: // 0010
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    break;
                case 4: // 0001
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 5: // 1100
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    break;
                case 6: // 1010
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    break;
                case 7: // 1001
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 8: // 0110
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    break;
                case 9: // 0101
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 10: // 0011
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 11: // 1110
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    break;
                case 12: // 1101
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 13: // 1011
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 14: // 0111
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
                case 15: // 1111
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(0 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(1 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(2 + 4 * vinterval);
                    hitcircleSpawnTimes.add(musicTime + offset);
                    hitcircleDeathTimes.add(musicTime + 2 * offset);
                    hitcircleLocation.add(3 + 4 * vinterval);
                    break;
            }

            // check the interval
            if (vinterval == vintervalMax) {
                vinterval = 0;
            } else {
                vinterval++;
            }

            musicTime += ((long) (bmpsInterval / (measureLength / 4)));

            // check the measure
            if (measureCounter == measureLength - 1) {
                measureCounter = 0;
                measureIndex++;
                if (measureIndex < measureMap.size()) {
                    measureLength = measureMap.get(measureIndex);
                }
            } else {
                measureCounter++;
            }
        }

        System.out.println(measureMap.size());
    }
}
