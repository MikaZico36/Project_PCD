package game;

import environment.Cell;
import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Predicate;

public class Client {

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", Game.SERVER_PORT, true);
        client.runClient();
    }

    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;
    private JFrame frame = new JFrame("client");
    private BoardJComponent board;
    private boolean controller = true;
    private boolean hasPrintedStatus = false;

    public Client(String address, int port, boolean alternativeKeys) throws IOException {
        serverSocket = new Socket(InetAddress.getByName(address) , port);
        this.board = new BoardJComponent(null, alternativeKeys);
    }


    public void runClient() {
        makeConnections();
        createMainFrame();

        while (controller) { // O server trata de fechar a ligacao
            try {
                GameStatus status = (GameStatus) in.readObject();
                printStatusMessage(status);
                Cell[][] cellBoard = status.getBoard();
                Game game = new Game(cellBoard);
                board.updateGame(game);         // Ao receber um novo game, temos que atualizar o nosso BoardJComponent board
                updateFrame(board);
//quando premimos uma tecla do lado do cliente, a Thread envia para o Servidor a direção escolhida na forma de String através de um canal de texto
                if (board.getLastPressedDirection() != null) {
                    out.println(board.getLastPressedDirection().toString());
                    board.clearLastPressedDirection();/*Por fim voltamos a colocar a direção selecionada de novo a null para evitar um
                                                        movimento contínuo*/
                }
//Quando o Server termina antes do cliente, o cliente fecha os seus canais e termina o seu processo
            } catch (IOException | ClassNotFoundException e) {
                closeSocket();
                System.exit(0);
            }
        }
        closeSocket();
    }

    private void printStatusMessage(GameStatus status) {
        if(status.getPlayerStatusMessage().isBlank())   return;
        if(!hasPrintedStatus) {
            System.out.println(status.getPlayerStatusMessage());
            hasPrintedStatus = true;
        }
    }
    private void makeConnections() {
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream())), true);
            in = new ObjectInputStream(serverSocket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//Criamos um JFrame do lado do cliente
    private void createMainFrame() {
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        jframeSupport();
    }
    //Permite substituir a BoardJComponent anterior por uma mais recente recebida pelo Servidor
    private void updateFrame(BoardJComponent board) {
        frame.getContentPane().removeAll();
        frame.add(board);
        frame.addKeyListener(board);
        frame.setVisible(true);
        frame.repaint();
    }
//JFrame com um botão de sair que permite ao cliente desconectar-se do jogo e ainda fechar os canais de ligação
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