package game;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Podio extends Thread{
    private int position;

    private static Podio this_Podio = null;
    private boolean isFinished = false;
    private ArrayList<Player> podio;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";


    private Podio(){
        position = 0;
        podio = new ArrayList<>();
    }

    public static Podio getPodio(){
        if(this_Podio == null)
            this_Podio = new Podio();
    return this_Podio;
    }



    public synchronized void acabei(Player player){
        podio.add(position,player); //TODO MUDAR
        position++;
        System.out.println("Podio " + isFinished  + " " + position);
        if(position== 3)
            isFinished=true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getPosition(){
        return position;
    }
    public synchronized void await() throws InterruptedException{
        while(position < Game.NUM_FINISHED_PLAYERS_TO_END_GAME)
            wait();
    }

    private void lugaresPodio() {
        System.out.println("Entrei no Lugar");
        if (position >= Game.NUM_FINISHED_PLAYERS_TO_END_GAME) {
            for (int i = 1; i <= podio.size(); i++) {
                switch (i) {
                    case 1 -> System.out.println(ANSI_RED + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                    case 2 -> System.out.println(ANSI_GREEN + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                    case 3 -> System.out.println(ANSI_BLUE + "O " + i + " lugar é o Player... " + podio.get(i-1).getIdentification() + ANSI_RESET);
                }
            }
        }
    }

@Override
    public void run(){
        while(!isFinished ) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("saí do wait do run");
    lugaresPodio();
    }





}
