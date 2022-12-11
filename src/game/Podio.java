package game;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class Podio implements Serializable {
    //Classe Podio corresponde à nossa barreira CountDownLatch()
    private int count;
//ArrayList que guarda os vencedores do jogo
    private ArrayList<Player> podio;
    //Variáveis que permitem alterar a cor do texto imprimido na consola
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";


    public Podio(int count){
        this.count = count;
        podio = new ArrayList<>();
    }
//Subtrai o valor do contador e adiciona o player vencedor ao ArrayList
    public synchronized void countDown(Player player){
        podio.add(player);
        count--;
        if(isFinished()) {
            notifyAll();
            lugaresPodio();
        }
    }
//Verifica se o jogo terminou, ou seja se o count está a 0
    public boolean isFinished() {
        return count == 0;
    }
//Método await() que coloca os playersBot em espera assim que são adicionados ao Podio
    public synchronized void await() throws InterruptedException{
        while(!isFinished())
            wait();
    }

    private void lugaresPodio() {
        if (isFinished()) {
            for (int i = 1; i <= podio.size(); i++) {
                switch (i) {  //Depois de verificar se o jogo terminou, imprime o ID dos players e a sua posição na consola
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
        //Permite que todas as Threads terminem as suas funções para não serem interrompidas
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exit(0); //Permite terminar todas as Threads que estão ligadas ao Game
    }

    //Função usada na criação de mensagens para o cliente
    public int getPlayerPosition(Player player) {
        return podio.indexOf(player)+1;
    }
}