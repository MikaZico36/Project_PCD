package game;

import java.io.Serializable;
/*A nossa classe Lock corresponde aos nossos cadeados que usamos em cada Cell do jogo*/

public class Locks implements Serializable {
    private boolean locked = false;

    public Locks(){
    }
//Desbloqueia o lock e notifica todos os processos
    public synchronized void unLock(){
        if(isLocked()) {
            locked = false;
            synchronized (this) {
                notifyAll();
            }
        }
    }
//Fecha o lock
    public synchronized void lock() {
        if (isLocked()) {
            try {
                System.out.println("Vou ficar bloqueado");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

    private synchronized boolean isLocked(){
        return locked;
    }

}
