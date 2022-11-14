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
		case 0 :
			d = Direction.UP;
			break;
		case 1 : 
			d = Direction.RIGHT;
			break;
		case 2 :
			d = Direction.LEFT;
			break;
		case 3 :
			d = Direction.DOWN;
			break;

		}
		return d;
	}





	@Override
	public void run() {
		// TODO Auto-generated method stub

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
