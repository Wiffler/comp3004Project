package teamdroid.com.speedtestarena.game.MusicGame;

/**
 * Created by Kenny on 2016-10-26.
 * Tracks the spawn information for a single hit circle.
 * Is created by the hit map when parsing the .sm file.
 */

public class HitInfo {
    public long spawnTime;
    public long deathTime;
    public long beatTime;
    public int spawnLocation;

    public HitInfo(long spawn, long death, long beat, int location) {
        spawnTime = spawn;
        deathTime = death;
        beatTime = beat;
        spawnLocation = location;
    }
}
