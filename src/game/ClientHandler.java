package game;

import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;
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

    public ClientHandler(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        makeConnections();      //Estabelece a ligacao com o cliente, assim como inicializar out e in
        Game game = Game.getGame();
        HumanPlayer player = new HumanPlayer(100, (byte) 3, game.getPodio(), clientSocket);
        while(true) { //TODO Quando player morrer fazemos como? talvez idk
            try {
                out.writeObject(GameGuiMain.getBoardGui());
                System.out.println("Esperando pela direçao");
                out.reset();
                String direction = in.readLine();

            } catch(SocketTimeoutException e) {
                //Aqui nao se faz nada, de modo a repetir o loop e passar a janela atualizada
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

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
