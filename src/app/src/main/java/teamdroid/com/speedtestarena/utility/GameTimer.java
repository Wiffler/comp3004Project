package teamdroid.com.speedtestarena.utility;

/**
 * Created by Kenny on 2016-10-20.
 */

public class GameTimer extends Thread {

    private volatile long curTime;
    private volatile long ticks;
    private volatile boolean running;

    private long prevTime;

    public GameTimer() {
        curTime = 0;
        ticks = 0;
        prevTime = 0;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getTime() {
        return curTime;
    }

    public long getTicks() {
        return ticks;
    }

    public void decrementTicks() {
        ticks--;
    }

    public void run() {
        while (running) {
            curTime = System.currentTimeMillis();
            if (curTime - prevTime > 15) {
                ticks++;
                prevTime = curTime;
                //System.out.println("timer: " + curTime + " " + ticks);
            } else {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
