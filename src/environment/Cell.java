package environment;

import game.Game;
import game.Lock;
import game.Player;

import java.io.Serializable;

public class Cell implements Serializable {
	private Coordinate position;
	private Game game;
	private Player player=null;

	private Lock lock;
	
	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
		lock = new Lock();
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOccupied() {
		return player!=null;
	}


	public Player getPlayer() {
		return player;
	}


	public synchronized void setPlayer(Player player) {
		lock.tryUnLock();
		lock.setLocked();
		this.player = player;
		player.setCell(this);
		game.notifyChange();
		lock.unLocked();
	}

	public synchronized void spawnPlayer(Player player) {
		while(this.isOccupied()) {
			try {
				System.out.println("Jogador " + player.getIdentification() + " nao pode ser colocado, esperando..." + getPosition());
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		setPlayer(player);
		System.out.println("O jogador " + player.getIdentification() + " foi adicionado.");
	}
	public Coordinate getCoordinate() {
		return position;
	}
	
	public synchronized void unsetPlayer() {
		lock.tryUnLock();
		if(!isOccupied())
			return;

		player = null;
		game.notifyChange();
		notifyAll();
		lock.unLocked();

	}

	
	public void setCoordinate(Coordinate c) {
		position = c;
	}
	
}
