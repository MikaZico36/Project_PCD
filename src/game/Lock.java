package game;

import java.io.Serializable;

public class Lock implements Serializable {
    private boolean locked;

    public Lock(){
        locked = false;
    }

    public synchronized void unLocked(){
        locked= false;
        synchronized (this) {
            notifyAll();
        }
    }

    public synchronized void setLocked() {
        locked = true;
    }

    public synchronized void tryUnLock()  {
        while (locked){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}