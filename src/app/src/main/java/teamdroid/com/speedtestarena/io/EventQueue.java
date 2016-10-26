package teamdroid.com.speedtestarena.io;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kenny on 2016-10-20.
 */

public class EventQueue<T> {

    private volatile LinkedList<T> list;
    private final Lock _mutex = new ReentrantLock(true);

    public EventQueue() {
        list = new LinkedList<T>();
    }

    // Queue operations
    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void enqueue(T obj) {
        _mutex.lock();
        list.add(obj);
        _mutex.unlock();
    }

    public T dequeue() {
        T head = null;
        _mutex.lock();
        if (list.size() > 0) {
            head = list.remove(0);
        }
        _mutex.unlock();
        return head;
    }
}
