package game;

import environment.Cell;
import environment.Direction;

import static java.lang.Thread.sleep;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class BotPlayer extends Player implements Runnable {
	public BotPlayer(int id, byte strength, Podio podio) {
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

	@Override
	public void run() {

		game.getRandomCell().spawnPlayer(this);

		try {
			sleep(Game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		int counter = 0;	// Usa-se esta variavel para determinar se o ciclo atual do while é quando o bot se deve mexer, de acordo com o seu original strength
		while (!isDead() && this.getCurrentStrength() < Game.MAX_PLAYER_STRENGTH && !podio.isFinished()) {
			counter++;
			if (counter == getOriginalStrength()) {
				counter = 0;
					move();
			}
			try {
				sleep(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
		if (this.getCurrentStrength() >= Game.MAX_PLAYER_STRENGTH) {
			System.out.println("Vou entrar no countdown" + this .getIdentification());
			podio.countDown(this);
			try {
				if(!isHumanPlayer())	podio.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}


	}
}
