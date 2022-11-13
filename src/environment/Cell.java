package environment;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player=null;
	
	public Cell(Coordinate position,Game g) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player!=null;
	}


	public Player getPlayer() {
		return player;
	}

	// Should not be used like this in the initial state: cell might be occupied, must coordinate this operation
	public synchronized void setPlayer(Player player) {
		if(this.isOcupied())
			try {
				System.out.println("Jogador " + player.getIdentification() + " não foi colocado, esperando...");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		this.player = player;
		System.out.println("O jogador " + player.getIdentification() + " foi adicionado.");
	}
	
	public synchronized void unsetPlayer() {
		if(!isOcupied())
			return;
		
		player = null;
		Game.getGame().notifyChange();
		notifyAll();
		
		
		
	}

}
