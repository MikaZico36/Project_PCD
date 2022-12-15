package environment;

import game.Game;
import game.Locks;
import game.Player;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cell implements Serializable {
	private Coordinate position;
	//Colocamos o Game como transient pois não precisamos que seja enviado para o cliente
	private transient Game game;
	private Player player=null;
	//O Cadeado que cada Cell irá utilizar
	private Lock lock = new ReentrantLock();
	
	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
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


	public void setPlayer(Player player) {
		lock.lock();
		try{
			this.player = player;
			player.setCell(this);
			game.notifyChange();

		}finally {
			lock.unlock();
		}


	}

	//É usado do Player estar no jogo e permite dar Spawn aos Players do jogo
	public void spawnPlayer(Player player) {
			while(this.isOccupied()) {
				//Caso o player selecione uma Cell onde está um player morto, então escolhe outra Cell
				if (this.getPlayer().isDead()) {
					game.addPlayerToGame(player);

					return;
				}
			try {
				synchronized (this) {
					wait();
				}
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
	
	public void unsetPlayer() {
		lock.lock();
		try {
			if (!isOccupied())
				return;
			player = null;
			game.notifyChange();
		}finally {
			lock.unlock();
		}

	}

	
	public void setCoordinate(Coordinate c) {
		position = c;
	}
	
}
