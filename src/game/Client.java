package game;

import environment.Direction;
import gui.BoardJComponent;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.function.Predicate;

public class Client extends Thread{
    // TODO CRIAR CONSTRUTOR COMO TA NO ENUNCIADO


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;
    private JFrame frame = new JFrame("client");

    @Override
    public void run() {
        //TODO Human Player run() method
        makeConnections();
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        while(true) { // O server trata de fechar a ligacao

            try {
                Game game = (Game) in.readObject();
                BoardJComponent board = new BoardJComponent(game, false);
                updateFrame(board);

                if(board.getLastPressedDirection() != null) {
                    System.out.println(board.getLastPressedDirection().toString());
                    out.println(board.getLastPressedDirection().toString());

                }

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

    private void updateFrame(BoardJComponent board) {
        frame.getContentPane().removeAll();
        frame.add(board);
        frame.addKeyListener(board);
        frame.setVisible(true);
        frame.repaint();
    }
}
