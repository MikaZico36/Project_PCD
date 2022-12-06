package game;

import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import java.io.*;
import java.net.Socket;

public class HumanPlayer extends Player{
    private ObjectInputStream in;
    private PrintWriter out;
    private Socket serverSocket;

    public static void main(String[] args){

    }
    public HumanPlayer(int id, byte strength, Podio podio) {
        super(id, strength, podio);
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    @Override
    public Direction chosenDirection() {
        //TODO Human Player chosenDirection() method
        Direction d = GameGuiMain.getBoardGui().getLastPressedDirection();
        return d;
    }

    @Override
    public void run() {
        //TODO Human Player run() method
        makeConnections();
        while(!isDead()) {
            // TODO O que vai o player receber? O board todo? Perguntar a prof terça
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
