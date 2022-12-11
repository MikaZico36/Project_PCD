package game;

import environment.Cell;

import java.io.Serializable;

public class GameStatus implements Serializable {
    private final Cell[][] board;
    private final String playerStatusMessage;

    public GameStatus(Cell[][] board, String playerStatusMessage) {
        this.board = board;
        this.playerStatusMessage = playerStatusMessage;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public String getPlayerStatusMessage() {
        return playerStatusMessage;
    }
}
