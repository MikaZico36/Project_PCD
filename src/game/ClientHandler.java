package game;

import environment.Direction;
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
        HumanPlayer player = new HumanPlayer(100, (byte) 3, game.getPodio());
        while(true) { //TODO Quando player morrer fazemos como? talvez idk
            try {
                out.reset();
                out.writeObject(game);

                String direction = in.readLine();
                System.out.println(direction);
                choosePlayerDirection(direction);
                player.move();      // TODO ver se podemos integrar isto no choosePlayerDirection()
            } catch(SocketTimeoutException e) {
                //Aqui nao se faz nada, de modo a repetir o loop e passar a janela atualizada
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void choosePlayerDirection(String direction) {
        switch(direction) { //TODO Ver se podemos usar : em vez de ->
            case "UP"->      player.setChosenDirection(Direction.UP);
            case "DOWN"->    player.setChosenDirection(Direction.DOWN);
            case "LEFT"->    player.setChosenDirection(Direction.LEFT);
            case "RIGHT"->   player.setChosenDirection(Direction.RIGHT);
        }
    }
    private void makeConnections() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.setSoTimeout((int) Game.REFRESH_INTERVAL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
