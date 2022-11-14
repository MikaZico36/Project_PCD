package game;



import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player  {


	protected  Game game = Game.getGame();

	private int id;
	protected Cell position;
	
	private byte currentStrength;
	protected byte originalStrength;

	// TODO: get player position from data in game
	public Cell getCurrentCell() {
		return position;
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		currentStrength=strength;
		originalStrength=strength;
	}

	public abstract boolean isHumanPlayer();
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

	public void setCell(Cell c) {
		position = c;
	}
	

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
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

	public byte getCurrentStrength() {
		return currentStrength;
	}


	public int getIdentification() {
		return id;
	}
	
	
	public void changePosition(Coordinate c) {
		position.setCoordinate(c);
	}
	
	
}
