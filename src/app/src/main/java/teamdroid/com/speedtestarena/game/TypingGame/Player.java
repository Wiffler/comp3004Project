package teamdroid.com.speedtestarena.game.TypingGame;

/**
 * Created by Alan on 2016-10-26.
 */

import java.io.Serializable;
import java.util.HashMap;

public class Player implements Serializable{
    private String levelSetting;
    private HashMap <String,Integer> score = new HashMap<String, Integer>();
    private HashMap <String,Double> fastestTimeHash = new HashMap<String, Double>();
    private HashMap <String,Double> slowestTimeHash = new HashMap<String, Double>();

    public String getLevel(){return levelSetting;}
    public HashMap <String,Double> getBestTimeHash(){return fastestTimeHash;}
    public HashMap <String,Double> getWorstTimeHash(){return slowestTimeHash;}

    public void setLevel(String level){
        levelSetting = level;
    }

    public void updateBestTime(int sentenceScore, Double bestTime){
        score.put(levelSetting,sentenceScore);
        fastestTimeHash.put(levelSetting,bestTime);
    }

    public void updateWorstTime(Double worstTime){
        slowestTimeHash.put(levelSetting,worstTime);
    }
}
