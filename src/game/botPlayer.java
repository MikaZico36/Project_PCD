package game;

import environment.Cell;
import environment.Direction;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class botPlayer extends Player {
	public botPlayer(int id, byte strength, Podio podio) {
		super(id, strength, podio);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

	//Função que calcula de forma random para onde o player se vai mover 

	@Override
	public Direction chosenDirection() {
		Direction d = null;
		int prob = (int) Math.round((Math.random() * 3));

		switch (prob) {
			case 0 -> d = Direction.UP;
			case 1 -> d = Direction.RIGHT;
			case 2 -> d = Direction.LEFT;
			case 3 -> d = Direction.DOWN;
		}
		return d;
	}

	public void setCurrentCell(Cell c) {
		this.position = c;
	}

	@Override
	public void run() {

		game.getRandomCell().spawnPlayer(this);

		try {
			sleep(Game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		int counter = 0;
		while (!isDead() && this.getCurrentStrength() < Game.MAX_PLAYER_STRENGTH && !podio.isFinished()) {
			counter++;
			//System.out.println("Counter = " + counter + " Player ID = " + this.getIdentification());
			if (counter == getOriginalStrength()) {
				counter = 0;
				//System.out.println("Counter dentro do if = " + counter);
				synchronized (this) {
					move();
				}
			}
			try {
				sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
		if (this.getCurrentStrength() >= Game.MAX_PLAYER_STRENGTH) {
			//game.addWinner(this);
			//System.out.println(this.getCurrentCell().getCoordinate());
			//	this.setCurrentStrength((byte) 0);    //Coloco a pontuação a 0 depois de ganhar para que não possa comer outros players
			//}

			podio.acabei(this);
			try {
				podio.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		synchronized (this) {
			notifyAll();
		}
	}
}
