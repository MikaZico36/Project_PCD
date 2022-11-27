package game;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static java.lang.System.exit;

public class Podio implements Runnable{
    private int position;
    private boolean isFinished = false;
    private ArrayList<Player> podio;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";


    public Podio(){
        position = 0;
        podio = new ArrayList<>();
    }

    public synchronized void acabei(Player player){
        podio.add(position,player);
        position++;
        System.out.println("Podio " + isFinished  + " " + position);
        if(position== 3)
            isFinished=true;
    }

    public synchronized boolean isFinished() {
        return isFinished;
    }

    public int getPosition(){
        return position;
    }
    public synchronized void await() throws InterruptedException{
        while(position < Game.NUM_FINISHED_PLAYERS_TO_END_GAME)
            wait();
    }

    public synchronized void LugaresPodio() {
        System.out.println("Entrei no Lugar");
        if (position >= Game.NUM_FINISHED_PLAYERS_TO_END_GAME) {
            for (int i = 0; i < podio.size(); i++) {
                int lugar = i + 1;
                switch (lugar) {
                    case 1 -> System.out.println(ANSI_RED + "O " + lugar + " lugar é o Player... " + podio.get(i).getIdentification() + ANSI_RESET);
                    case 2 -> System.out.println(ANSI_GREEN + "O " + lugar + " lugar é o Player... " + podio.get(i).getIdentification() + ANSI_RESET);
                    case 3 -> System.out.println(ANSI_BLUE + "O " + lugar + " lugar é o Player... " + podio.get(i).getIdentification() + ANSI_RESET);
                }
            }
            exit(0);
        }
    }

@Override
    public void run(){
        while(!isFinished) {
            try {
                System.out.println("Vou esperar");
                wait(2000);
                System.out.println("Acordei");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("saí do wait do run");
    LugaresPodio();
    isFinished = true;
    }





}
