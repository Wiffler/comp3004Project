package teamdroid.com.speedtestarena.game.GameTest1;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import teamdroid.com.speedtestarena.R;

/**
 * Created by Kenny on 2016-10-25.
 */

public class HitMap {
    // Delay times
    public static long startDelay = -1750;
    public static long endDelay = 100;

    // Parser object
    private static SMParser parser = new SMParser();

    // Sound offset
    private long offset = 0;

    // Total possible score
    public int maxScore = 0;

    // Only used during map generation
    private ArrayList<Integer> spawnMap;
    private ArrayList<Integer> measureMap;
    private HashMap<Integer, Float> bpmsMap;

    // Generated mappings
    public float[][] spawnLoc = new float[2][64];
    public ArrayList<HitInfo> spawnInfoList;
    public volatile int spawnTimeIndex;

    public HitMap(float screenWidth, float screenHeight, float objWidth) {
        spawnMap = new ArrayList<Integer>();
        measureMap = new ArrayList<Integer>();
        bpmsMap = new HashMap<Integer, Float>();

        spawnInfoList = new ArrayList<HitInfo>();

        generateLocationArray(screenWidth, screenHeight, objWidth);
    }

    private void generateLocationArray(float width, float height, float objWidth) {
        int widthCount = 4;
        int heightCount = 16;

        float horizontalBorderSize = 50;
        float horizontalGapSize = (width - (2 * horizontalBorderSize + widthCount * objWidth)) / (widthCount - 1);

        float verticalTopBorder = 50;
        float verticalBottomBorder = 75;
        float verticalGap = (height - (verticalTopBorder + verticalBottomBorder + heightCount * objWidth)) / (heightCount - 1);

        // Generate spawnLoc matrix
        for (int i = 0; i < heightCount; i++) {
            spawnLoc[0][4 * i] = horizontalBorderSize;
            spawnLoc[0][4 * i + 1] = horizontalBorderSize + objWidth + horizontalGapSize;
            spawnLoc[0][4 * i + 2] = horizontalBorderSize + 2 * (objWidth + horizontalGapSize);
            spawnLoc[0][4 * i + 3] = horizontalBorderSize + 3 * (objWidth + horizontalGapSize);

            spawnLoc[1][4 * i] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 1] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 2] = verticalTopBorder + (objWidth + verticalGap) * i;
            spawnLoc[1][4 * i + 3] = verticalTopBorder + (objWidth + verticalGap) * i;
        }
    }

    // Setters
    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void addSpawnMap(int code) {
        spawnMap.add(code);
    }

    public void addMeasureMap(int code) {
        measureMap.add(code);
    }

    public void addBPMSMap(int noteNumber, float bpms) {
        bpmsMap.put(noteNumber, bpms);
    }

    // Initialization and map generation
    public void initialise(Context context, int smID) {
        // Parse the sound map
        parser.readSM(context, smID, this);

        /*
        for (Map.Entry<Integer, Float> entry : bpmsMap.entrySet()) {
            Integer key = entry.getKey();
            Float value = entry.getValue();
            System.out.println("Note: " + key + " BPMS: " + value);
        }
        */

        // Tracking the measure
        int measureIndex = 0;
        int measureCounter = 0;
        int measureLength = measureMap.get(measureIndex);

        // Computing the location index
        int vinterval = 0;
        //int vintervalMax = (measureLength / 4) - 1;
        int vintervalMax = 16 - 1;

        // Tracking the song position at a given beat
        double bpms = 0;
        double interval = 0;
        long musicTime = 0;

        // Computing the life time of a hitbox
        long spawnTime, deathTime, beatTime;


        // offset needs sign switched
        // ex. offset = -0.567 means start at 567ms point on the audio file
        //offset = -1 * offset;
        offset = Math.abs(offset);

        //System.out.println("Beat count: " + spawnMap.size());

        // Iterate through all the notes
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

            // Check if there is a more accurate bpms number
            if (bpmsMap.containsKey(i)) {
                bpms = bpmsMap.get(i);
                // Recompute the interval using the updated bpms
                interval = 60000 / bpms;
            }
            musicTime += ((long) (interval / (measureLength / 4)));
            //System.out.println(musicTime);

            // Check if we changed measures
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

        maxScore = spawnInfoList.size() * 100;
        //System.out.println(measureMap.size());
    }

    private int updateVInterval(int vinterval, int vintervalMax) {
        if (vinterval == vintervalMax) {
            return 0;
        } else {
            return vinterval + 1;
        }
    }
}
