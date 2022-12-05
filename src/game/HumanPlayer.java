package game;

import environment.Direction;

public class HumanPlayer extends Player{

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
        return null;
    }

    @Override
    public void run() {
        //TODO Human Player run() method
    }
}
