package game;

import gui.BoardJComponent;

import java.io.Serializable;

public class GameStatus implements Serializable {
    private BoardJComponent board;
    private HumanPlayer player;

    public GameStatus(BoardJComponent board, HumanPlayer player) {
        this.board = board;
        this.player = player;
    }

    public BoardJComponent getBoard() {
        return board;
    }

    public HumanPlayer getPlayer() {
        return player;
    }
}
