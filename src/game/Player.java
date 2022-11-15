package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player  {

	protected Game game = Game.getGame();
	private int id;
	protected Cell position;

	private byte currentStrength;
	protected byte originalStrength;

	// TODO: get player position from data in game

	public Player(int id, byte strength) {	//TODO aplicar solitao
		super();
		this.id = id;
		currentStrength=strength;
		originalStrength=strength;
	}

	public abstract boolean isHumanPlayer();
	public abstract Direction chosenDirection();

	public synchronized void move() {
		Cell currentCell = getCurrentCell();

		Coordinate destinationCoordinate = chosenDirection().getVector();
		Cell destinationCell = game.getCell(currentCell.getPosition().translate(destinationCoordinate));  //Vou buscar a célula para onde o player quer se mover

		if(currentCell == null || destinationCell == null)
			return;

		System.out.println(destinationCell.getCoordinate());

		destinationCell.setPlayer(this);	//Digo que o player agora faz parte dessa célula
		this.setCell(destinationCell);		//Coloco a Cell position da classe Player = nova

		game.getCell(currentCell.getPosition()).unsetPlayer(); // Por fim digo que a célula anteriormente ocupada pelo Player ficou livre, logo Player player = null


		Game.getGame().notifyChange();
	}

	public void setCell(Cell c) {
		position = c;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public Cell getCurrentCell() {
		return position;
	}

	public int getIdentification() {
		return id;
	}
	
	
	/*private void changePosition(Coordinate c) {
		position.setCoordinate(c);
	}*/ //TODO MAYBE REMOVE THIS


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
