package game;

import environment.Cell;

public class BoardThread extends Thread {
	private Cell cell;
	private Player player;

	public BoardThread(Cell cell, Player player) {
		this.cell = cell;
		this.player = player;
	}

	@Override
	public void run() {
		cell.spawnPlayer(player);
		player.setCell(Game.getGame().getCell(cell.getPosition()));
		Game.getGame().notifyChange();
	}

}
