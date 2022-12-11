package game;

import environment.Cell;
import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.function.Predicate;

public class Client extends Thread {

    public static void main(String[] args) {
        Client client = new Client("localhost", Game.SERVER_PORT, true);
        client.start();
    }

    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;
    private JFrame frame = new JFrame("client");
    private BoardJComponent board;
    private boolean controller = true;

    //Connection Variables
    private final String address;
    private final int port;
    private final boolean alternativeKeys;

    public Client(String address, int port, boolean alternativeKeys) {
        this.address = address;
        this.port = port;
        this.alternativeKeys = alternativeKeys;
        this.board = new BoardJComponent(null, alternativeKeys);
    }

    @Override
    public void run() {
        makeConnections();
        createMainFrame();

        while (controller) { // O server trata de fechar a ligacao
            try {
                Cell[][] cellBoard = (Cell[][]) in.readObject();
                Game game = new Game(cellBoard);
                board.updateGame(game);         // Ao receber um novo game, temos que atualizar o nosso BoardJComponent board
                updateFrame(board);

                if (board.getLastPressedDirection() != null) {
                    out.println(board.getLastPressedDirection().toString());
                    board.clearLastPressedDirection();
                }

            } catch (IOException | ClassNotFoundException e) {
                closeSocket();
                System.exit(0);
            }
        }
        closeSocket();
    }

    private void makeConnections() {
        try {
            serverSocket = new Socket(address, port);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())), true);
            in = new ObjectInputStream(serverSocket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createMainFrame() {
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        jframeSupport();
    }
    private void updateFrame(BoardJComponent board) {
        frame.getContentPane().removeAll();
        frame.add(board);
        frame.addKeyListener(board);
        frame.setVisible(true);
        frame.repaint();
    }

    private void jframeSupport() {
        JFrame frameJ = new JFrame("Quer sair?");
        frameJ.setSize(100, 100);
        frameJ.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton button = new JButton("Sair");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller = false;
                System.exit(0);
            }
        });
        frameJ.add(button);
        frameJ.setVisible(true);
    }

    private void closeSocket(){
        try {
            in.close();
            out.close();
            serverSocket.close();
            System.out.println("JOGO FINALIZADO");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}