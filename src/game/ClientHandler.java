package game;

import gui.BoardJComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;
    private BoardJComponent boardGui;
    public ClientHandler(Socket client, BoardJComponent boardGui) {
        this.clientSocket = client;
        this.boardGui = boardGui;
    }

    @Override
    public void run() {
        makeConnections();      //Estabelece a ligacao com o cliente, assim como inicializar out e in
        Game game = Game.getGame();
        HumanPlayer player = new HumanPlayer(100, (byte) 3, game.getPodio(), clientSocket);
        while(true) { //TODO Quando player morrer fazemos como? talvez idk
            try {
                out.writeObject(new GameStatus(boardGui, player));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void makeConnections() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
