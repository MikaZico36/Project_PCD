package game;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class Podio implements Serializable {
    private int count;

    private ArrayList<Player> podio;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";


    public Podio(int count){
        this.count = count;
        podio = new ArrayList<>();
    }

    public synchronized void countDown(Player player){
        podio.add(player);
        count--;
        if(isFinished())
            notifyAll();
        System.out.println("Entrei no podio");
    }

    public boolean isFinished() {
        return count == 0;
    }

    public synchronized void await() throws InterruptedException{
        while(!isFinished())
            wait();
        lugaresPodio();
    }

    private void lugaresPodio() {
        if (isFinished()) {
            for (int i = 1; i <= podio.size(); i++) {
                switch (i) {
                    case 1:
                        System.out.println(ANSI_RED + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                        break;
                    case 2:
                        System.out.println(ANSI_GREEN + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                        break;
                    case 3:
                        System.out.println(ANSI_BLUE + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                        break;
                }
            }
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exit(0);
    }
}
