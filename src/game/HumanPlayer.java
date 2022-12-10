package game;

import environment.Direction;
import gui.GameGuiMain;

import java.net.Socket;

public class HumanPlayer extends Player{
    private Socket clientSocket; // Socket do cliente ao qual este HumanPlayer esta associado
    private Direction chosenDirection;

    public HumanPlayer(int id, byte strength, Podio podio) {
        super(id, strength, podio);
        this.clientSocket = clientSocket;
    }

    @Override
    public boolean isHumanPlayer() {
        return true;
    }

    @Override
    public Direction chosenDirection() {
        return chosenDirection;
    }

    public void setChosenDirection(Direction direction) {
        this.chosenDirection = direction;
    }
}
