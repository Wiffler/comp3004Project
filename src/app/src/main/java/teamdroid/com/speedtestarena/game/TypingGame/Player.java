package teamdroid.com.speedtestarena.game.TypingGame;

/**
 * Created by Alan on 2016-10-26.
 */

import java.io.Serializable;
import java.util.HashMap;

public class Player implements Serializable{
    private String setting;
    private HashMap <String,Integer> score = new HashMap<String, Integer>();
    private HashMap <String,Double> bestTimeHash = new HashMap<String, Double>();
    private HashMap <String,Double> worstTimeHash = new HashMap<String, Double>();

    public String getLevel(){return setting;}
    public HashMap <String,Double> getBestTimeHash(){return bestTimeHash;}
    public HashMap <String,Double> getWorstTimeHash(){return worstTimeHash;}

    public void setLevel(String level){
        setting = level;
    }

    public void updateBestTime(int sentenceScore, Double bestTime){
        score.put(setting,sentenceScore);
        bestTimeHash.put(setting,bestTime);
    }

    public void updateWorstTime(Double worstTime){
        worstTimeHash.put(setting,worstTime);
    }
}
