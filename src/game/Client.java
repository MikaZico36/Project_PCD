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
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        while(true) { // O server trata de fechar a ligacao

            try {
                System.out.println("Esperando...");
                BoardJComponent boardGui = (BoardJComponent) in.readObject();
                frame.getContentPane().removeAll();
                frame.add(boardGui);
                frame.setVisible(true);
                frame.repaint();

            } catch (IOException | ClassNotFoundException e) {
               throw new RuntimeException(e);
            }
        }
    }
    private void makeConnections() {
        try {
            serverSocket = new Socket("localhost", Game.SERVER_PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())), true);
            in = new ObjectInputStream(serverSocket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
