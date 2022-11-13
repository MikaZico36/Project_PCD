package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

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

	public static Direction ChooseMove() {
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


	public void move() {
		Cell current =getCurrentCell();
		Direction d = ChooseMove();
		Coordinate c = d.getVector();
		
		current.getPosition().translate(c);
		System.out.println("depois translate" + current.getPosition());
		
		
		setCell(current);
		game.notifyChange();
		System.out.println(position.getPosition());
	}





	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
		synchronized(this) {
			move();
			try {
				wait(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(position);
		}
		}
	}
}
