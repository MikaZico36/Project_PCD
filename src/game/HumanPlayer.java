package game;

import environment.Direction;
import gui.BoardJComponent;
import gui.GameGuiMain;

import java.io.*;
import java.net.Socket;

public class HumanPlayer extends Player{
    private Socket clientSocket; // Socket do cliente ao qual este HumanPlayer esta associado

    public HumanPlayer(int id, byte strength, Podio podio, Socket clientSocket) {
        super(id, strength, podio);
        this.clientSocket = clientSocket;
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


}
