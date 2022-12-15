package game;

import environment.Cell;
import environment.Direction;

import static java.lang.Thread.sleep;

public class BotPlayer extends Player implements Runnable {
	public BotPlayer(int id, byte strength, Podio podio) {
		super(id, strength, podio);
	}

	@Override
	public boolean isHumanPlayer() {
		return false;
	}

	//Função que calcula de forma random para onde o playerBot se vai mover
	@Override
	public Direction chosenDirection() {
		Direction d = null;
		int prob = (int) Math.round((Math.random() * 3));

		switch (prob) {
			case 0:
				d = Direction.UP;
				break;
			case 1:
				d = Direction.RIGHT;
				break;
			case 2:
				d = Direction.LEFT;
				break;
			case 3:
				d = Direction.DOWN;
				break;
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
		// Usa-se esta variavel para determinar se o ciclo atual do while é quando o bot se deve mexer, de acordo com o seu original strength
		int counter = 0;
		while (!isDead() && this.getCurrentStrength() < Game.MAX_PLAYER_STRENGTH) {
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
			setOnPodio();
			try {
				podio.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/*Fazemos Override deste método pois no HumanPlayer tem um funcionamento diferente.
	 Aqui precisamos que o botPlayer seja colocado em espera assim que ganha o jogo*/
	@Override
	public void setOnPodio(){
		super.setOnPodio();
		try {
			if(!isHumanPlayer())	podio.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


}
