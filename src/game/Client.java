package game;

import gui.BoardJComponent;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.function.Predicate;

public class Client extends Thread{
    // TODO CRIAR CONSTRUTOR COMO TA NO ENUNCIADO

    private JFrame frame = new JFrame("client");
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;
    /*private HumanPlayer player;

    /*public void associatePlayer(HumanPlayer player) {
        this.player = player;
    }
    public HumanPlayer getPlayer() { // TODO Ver se e preciso isto
        return player;
    }*/

    @Override
    public void run() {
        //TODO Human Player run() method
        makeConnections();
        while(true) { // O server trata de fechar a ligacao
            // TODO O que vai o player receber? O board todo? Perguntar a prof terça
            try {
                GameStatus status = (GameStatus) in.readObject();
                BoardJComponent boardGui = status.getBoard();
                HumanPlayer player = status.getPlayer();
                frame.add(boardGui);
                frame.setVisible(true);


            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void makeConnections() {
        try {
            serverSocket = new Socket("localhost", Game.SERVER_PORT);
            in = new ObjectInputStream(serverSocket.getInputStream());
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
