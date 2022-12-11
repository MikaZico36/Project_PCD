package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player implements Serializable {

	protected Game game = Game.getGame();
	private int id;
	protected Cell position;
	private byte currentStrength;
	protected final byte originalStrength;
	protected Podio podio;

	public Player(int id, byte strength, Podio podio) {
		super();
		this.id = id;
		currentStrength=strength;
		originalStrength=strength;
		this.podio = podio;
	}

	public abstract boolean isHumanPlayer();
	public abstract Direction chosenDirection();

	public synchronized void move() {

		Cell currentCell = getCurrentCell();
		if(currentCell == null)			return;

		Coordinate destinationCoordinate = chosenDirection().getVector();
		Cell destinationCell = game.getCell(currentCell.getPosition().translate(destinationCoordinate));  //Vou buscar a célula para onde o player quer se mover
		if(destinationCell == null)		return;

		//System.out.println(destinationCell.getCoordinate());

		if(destinationCell.isOccupied()) {
			if(destinationCell.getPlayer().getCurrentStrength() == 10 ||
					destinationCell.getPlayer().getCurrentStrength() == 0){
				try {
					Thread.sleep(Game.MAX_WAITING_TIME_FOR_MOVE);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				return;
			}
			resolveConflict(destinationCell.getPlayer());
			game.notifyChange();	//TODO ver se e mesmo necessario esta linha
			return;
		}

		destinationCell.setPlayer(this);	//Digo que o "player" agora faz parte dessa célula
		this.setCell(destinationCell);		//Coloco a Cell position da classe Player = nova
		game.getCell(currentCell.getPosition()).unsetPlayer(); // Por fim digo que a célula anteriormente ocupada pelo Player ficou livre, logo Player player = null
		game.notifyChange();

		System.out.println("Sou o move e a tua strength é " + this.getCurrentStrength());

	}

	private void resolveConflict(Player enemy) {
		byte enemyPower = enemy.getCurrentStrength();
		if(enemyPower > getCurrentStrength())
			enemy.kill(this);
		else if(enemyPower < getCurrentStrength())
			this.kill(enemy);
		else {
			int random = (int) Math.round(Math.random());
			if(random == 0)		this.kill(enemy);
			else 				enemy.kill(this);
		}
		if (this.getCurrentStrength() == Game.MAX_PLAYER_STRENGTH) {

			podio.countDown(this);	// TODO Maybe mandar isto para o player geral se fizer sentido
			try {
				podio.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void kill(Player deadPlayer) {
		byte deadPlayerPoints = deadPlayer.getCurrentStrength();

		this.setCurrentStrength((byte) (getCurrentStrength() + deadPlayerPoints));
		if(this.getCurrentStrength() >Game.MAX_PLAYER_STRENGTH )
			this.setCurrentStrength(Game.MAX_PLAYER_STRENGTH);
		deadPlayer.setCurrentStrength((byte) 0);
	}
	public void setCell(Cell c) {
		position = c;
	}

	public void unSetCell(){
		position = null;
	}

	public byte getOriginalStrength() {
		return originalStrength;
	}
	public byte getCurrentStrength() {
		return currentStrength;
	}
	public void setCurrentStrength(byte strength) {
		currentStrength = strength;
	}

	public Cell getCurrentCell() {
		return position;
	}

	public int getIdentification() {
		return id;
	}

	public boolean isDead() {
		return getCurrentStrength() == 0;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
				+ "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
}
