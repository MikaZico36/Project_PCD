package game;

import java.io.Serializable;
/*A nossa classe Lock corresponde aos nossos cadeados que usamos em cada Cell do jogo*/

public class Lock implements Serializable {
    private boolean locked;

    public Lock(){
        locked = false;
    }
//Desbloqueia o lock e notifica todos os processos
    public synchronized void unLocked(){
        locked= false;
        synchronized (this) {
            notifyAll();
        }
    }
//Fecha o lock
    public synchronized void setLocked() {
        locked = true;
    }
//Verifica se o Lock está aberto, se não está fica em espera
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
