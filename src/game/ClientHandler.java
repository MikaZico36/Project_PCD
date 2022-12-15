package game;

import environment.Cell;
import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;
    private HumanPlayer player;


    public ClientHandler(Socket client, HumanPlayer player) {
        this.clientSocket = client;
        this.player = player;
    }

    @Override
    public void run() {
        makeConnections();      //Estabelece a ligacao com o cliente, assim como inicializar out e in
        Game game = Game.getGame();
        boolean verify = false;  //Tem como objetivo não repetir o primeiro if presente no ciclo while
        while (clientSocket != null) {

            try {
                if (player.getCurrentStrength() == Game.MAX_PLAYER_STRENGTH && !verify) {
                    player.setOnPodio();
                    verify = true;
                }
                out.reset();
                out.writeObject(new GameStatus(game.getBoard(), generatePlayerStatusMessage()));  //Recebe a Matriz de Cells do game e uma mensagem do Servidor
                if (!player.isDead() && player.getCurrentStrength() < Game.MAX_PLAYER_STRENGTH) {
                    String direction = in.readLine();
                    choosePlayerDirection(direction);
                    player.move();
                }
            } catch (SocketTimeoutException e) {
                //Aqui nao se faz nada, de modo a repetir o loop e passar a janela atualizada, mesmo quando o player nao envia direcoes
            } catch (IOException e) {
                /*Quando o jogador se desconecta, se este venceu ou morreu, ele permance no jogo como obstaculo.
                Se não, se saiu a meio sem morrer nem ganhar, ele simplesmente desaparece*/
                if (!(player.isDead() || player.getCurrentStrength() == Game.MAX_PLAYER_STRENGTH)) {
                    Cell playerCell = player.getCurrentCell();
                    if (playerCell != null) player.getCurrentCell().unsetPlayer();
                    player.unSetCell();

                }
            }

        }

        try {
            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException i) {
            System.err.println("Erro ao fechar");
        }

    }

    private void choosePlayerDirection(String direction) {
        switch(direction) {
            case "UP":
                player.setChosenDirection(Direction.UP);
                break;
            case "DOWN":
                player.setChosenDirection(Direction.DOWN);
                break;
            case "LEFT":
                player.setChosenDirection(Direction.LEFT);
                break;
            case "RIGHT":
                player.setChosenDirection(Direction.RIGHT);
                break;
        }

    }
    private void makeConnections() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.setSoTimeout((int) Game.REFRESH_INTERVAL);	      //Este método define um timeout para receber a direção do cliente
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Esta função gera as mesnagens que serão enviadas ao Cliente. Informam se o Cliente morreu ou em que lugar acabou no pódio
    private String generatePlayerStatusMessage() {
        if(player.isDead())
            return "O Player morreu";
        if(player.getCurrentStrength() == Game.MAX_PLAYER_STRENGTH)
            return "Ficaste em " + Game.getGame().getPodio().getPlayerPosition(player) + " lugar";
        return "";
    }

}
