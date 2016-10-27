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

    // Only used during map generation
    private ArrayList<Integer> spawnMap;
    private ArrayList<Integer> measureMap;

    public float[][] spawnLoc = new float[2][64];
    public ArrayList<HitInfo> spawnInfoList;
    public volatile int spawnTimeIndex;

    public HitMap(float screenWidth, float screenHeight, float objWidth) {
        spawnMap = new ArrayList<Integer>();
        measureMap = new ArrayList<Integer>();
        spawnInfoList = new ArrayList<HitInfo>();
        generateLocationArray(screenWidth, screenHeight, objWidth);
    }

    private void generateLocationArray(float width, float height, float objWidth) {
        float horizontalBorderSize = 100;
        float horizontalGapSize = (width - (2 * horizontalBorderSize + 4 * objWidth)) / 3;

        float verticalTopBorder = 175;
        float verticalBottomBorder = 100;
        float verticalGap = (height - (verticalTopBorder + verticalBottomBorder + 16 * objWidth)) / 15;

        // Generate spawnLoc matrix
        for (int i = 0; i < 16; i++) {
            spawnLoc[0][4 * i] = horizontalBorderSize;
            spawnLoc[0][4 * i + 1] = horizontalBorderSize + objWidth + horizontalGapSize;
            spawnLoc[0][4 * i + 2] = horizontalBorderSize + 2 * (objWidth + horizontalGapSize);
            spawnLoc[0][4 * i + 3] = horizontalBorderSize + 3 * (objWidth + horizontalGapSize);

            spawnLoc[1][4 * i] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 1] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 2] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 3] = verticalTopBorder + (objWidth + verticalGap) * i;

            /*
            System.out.println("x1: " + spawnLocX[i] + " x2: " + spawnLocX[i + 1] +
                    " x3: " + spawnLocX[i + 2] + " x4: " + spawnLocX[i + 3]);
            System.out.println("y1: " + spawnLocY[i] + " y2: " + spawnLocY[i + 1] +
                    " y3: " + spawnLocY[i + 2] + " y4: " + spawnLocY[i + 3]);
            */
        }
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

    public void addMeasureMap(int code) {
        measureMap.add(code);
    }

    // Initialization and map generation
    public void initialise(Context context) {
        // Parse the sound map
        parser.readSM(context, R.raw.test_sound_file2_sm, this);

        // set the spawn times
        int vinterval = 0;
        int measureIndex = 0;
        int measureCounter = 0;
        int measureLength = measureMap.get(measureIndex);
        int vintervalMax = (measureLength / 4) - 1;
        double bmpsInterval = 60000 / bpms;
        long musicTime = 0;

        long spawnTime, deathTime, beatTime;
        long startDelay = -1000;
        long endDelay = 100;

        // offset needs sign switched
        // ex. offset = -0.567 means start at 567ms point on the audio file
        offset = -1 * offset;

        System.out.println("Beat count: " + spawnMap.size());

        // go through the notes
        for (int i = 0; i < spawnMap.size(); i++) {
            int code = spawnMap.get(i);

            //System.out.println("i: " + i + " BPMS: " + bpms + " musicTime: " + musicTime + " Offset: " + offset + " Start Time : " + (musicTime + offset));

            // Compute the spawn and death time for a given line in the #NOTES section of the .sm file
            spawnTime = musicTime + offset + startDelay;
            deathTime = musicTime + offset + endDelay;
            beatTime = musicTime + offset;

            switch (code) {
                case 0: // 0000
                    break;
                case 1: // 1000
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 2: // 0100
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 3: // 0010
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 4: // 0001
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 5: // 1100
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 6: // 1010
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 7: // 1001
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 8: // 0110
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 9: // 0101
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 10: // 0011
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 11: // 1110
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 12: // 1101
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 13: // 1011
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 14: // 0111
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
                case 15: // 1111
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 0 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 1 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 2 + 4 * vinterval));
                    spawnInfoList.add(new HitInfo(spawnTime, deathTime, beatTime, 3 + 4 * vinterval));
                    vinterval = updateVInterval(vinterval, vintervalMax);
                    break;
            }

            // check the interval
            /*
            if (vinterval == vintervalMax) {
                vinterval = 0;
            } else {
                vinterval++;
            }
            */

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

    private int updateVInterval(int vinterval, int vintervalMax) {
        if (vinterval == vintervalMax) {
            return 0;
        } else {
            return vinterval + 1;
        }
    }
}
