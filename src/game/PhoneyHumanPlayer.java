package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import static java.lang.Thread.sleep;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player implements Runnable{
	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	//Função que calcula de forma random para onde o player se vai mover 

	@Override
	public Direction chosenDirection() {
		Direction d = null;
		int prob = (int) Math.round((Math.random()*3));
		System.out.println(prob);
		switch (prob) {
			case 0 -> d = Direction.UP;
			case 1 -> d = Direction.RIGHT;
			case 2 -> d = Direction.LEFT;
			case 3 -> d = Direction.DOWN;
		}
		return d;
	}





	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			sleep(4000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		while(true) {

		synchronized(this) {
			move();
			/*try {
				wait(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

			try {
				sleep(game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
	}
}
