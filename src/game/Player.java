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

	protected transient Game game = Game.getGame();
	private int id;
	protected Cell position;
	private byte currentStrength;
	protected final byte originalStrength;
	protected transient Podio podio;

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
		// Verificar se o player esta atualmente associado a uma Cell
		Cell currentCell = this.getCurrentCell();
		if(currentCell == null)			return;

		Coordinate destinationCoordinate = chosenDirection().getVector();
		Cell destinationCell = game.getCell(currentCell.getPosition().translate(destinationCoordinate));  //Vou buscar a célula para onde o player quer se mover
		if(destinationCell == null)		return;//Verificar se a Cell destino existe

		if(destinationCell.isOccupied()) {
			if(destinationCell.getPlayer().getCurrentStrength() == 10 || destinationCell.getPlayer().getCurrentStrength() == 0){ // Se a Cell destino estiver ocupada com um obstaculo, perde-se este ciclo, aborta-se o movimento
				try {
					if(!isHumanPlayer())	Thread.sleep(Game.MAX_WAITING_TIME_FOR_MOVE);	//Se um BotPlayer se mover para o obstaculo, este tem que esperar
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				return;
			}
			// Caso a Cell esteja ocupada por um jogador vivo, resolve-se o conflito
				this.resolveConflict(destinationCell.getPlayer());
			game.notifyChange();
			return;
		}
		destinationCell.setPlayer(this);	//Digo que o "player" agora faz parte dessa célula
		game.getCell(currentCell.getPosition()).unsetPlayer(); // Por fim digo que a célula anteriormente ocupada pelo Player ficou livre, logo Player player = null
		this.setCell(destinationCell);		//Coloco a Cell position da classe Player = nova
		game.notifyChange();
	}

	public Podio getPodio() {
		return podio;
	}

	private void resolveConflict(Player enemy) {
		byte enemyPower = enemy.getCurrentStrength();
		if(enemyPower > this.getCurrentStrength())
			enemy.kill(this);
		else if(enemyPower < this.getCurrentStrength())
			this.kill(enemy);
		else {
			int random = (int) Math.round(Math.random());
			if(random == 0)		this.kill(enemy);
			else 				enemy.kill(this);
		}
	}

	private void kill(Player deadPlayer) {
		byte deadPlayerPoints = deadPlayer.getCurrentStrength();
		this.setCurrentStrength((byte) (getCurrentStrength() + deadPlayerPoints));
		if(this.getCurrentStrength() >=Game.MAX_PLAYER_STRENGTH ){
			this.setCurrentStrength(Game.MAX_PLAYER_STRENGTH);
		}
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
