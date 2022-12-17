package game;



import environment.Cell;
import environment.Coordinate;
import environment.Direction;

import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

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

	public void move() {
		// Verificar se o player esta atualmente associado a uma Cell
		Cell currentCell = this.getCurrentCell();
		if(currentCell == null)			return;

		Coordinate destinationCoordinate = chosenDirection().getVector();
		Cell destinationCell = game.getCell(currentCell.getPosition().translate(destinationCoordinate));  //Vou buscar a célula para onde o player quer se mover

		if (destinationCell == null) return;//Verificar se a Cell destino existe
		destinationCell.getLock().lock();

		try {
			if (destinationCell.isOccupied()) {
				if (destinationCell.getPlayer().getCurrentStrength() == 10 || destinationCell.getPlayer().getCurrentStrength() == 0) { // Se a Cell destino estiver ocupada com um obstaculo, perde-se este ciclo, aborta-se o movimento
					try {
						if (!isHumanPlayer())
							Thread.sleep(Game.MAX_WAITING_TIME_FOR_MOVE);    //Se um BotPlayer se mover para o obstaculo, este tem que esperar
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
			destinationCell.setPlayer(this);    //Digo que o "player" agora faz parte dessa célula
			game.getCell(currentCell.getPosition()).unsetPlayer(); // Por fim digo que a célula anteriormente ocupada pelo Player ficou livre, logo Player player = null
			this.setCell(destinationCell);        //Coloco a Cell position da classe Player = nova
			game.notifyChange();
		}finally {
			destinationCell.getLock().unlock();
		}
	}

	public Podio getPodio() {
		return podio;
	}
//Coloca o player no podio
	public void setOnPodio(){
		podio.countDown(this);

	}

	private void resolveConflict(Player enemy) {
		byte enemyPower = enemy.getCurrentStrength(); //Strength do inimigo
		if(enemyPower > this.getCurrentStrength()) //Se o player for mais fraco que o inimigo perde a batalha e chama o método kill() e mata o player
 			enemy.kill(this);
		else if(enemyPower < this.getCurrentStrength())//Se o inimigo é mais fraco que o player, então será o inimigo a morrer pelo método kill()
			this.kill(enemy);
		else { //Em caso de empate, o vencedor da batalha é decidido de forma aleatória
			int random = (int) Math.round(Math.random());
			if(random == 0)		this.kill(enemy);
			else 				enemy.kill(this);
		}
	}

	private void kill(Player deadPlayer) {
		byte deadPlayerPoints = deadPlayer.getCurrentStrength(); //Força do player que morreu
		this.setCurrentStrength((byte) (getCurrentStrength() + deadPlayerPoints)); //Acrescenta a força do player que morreu ao vencedor
		if(this.getCurrentStrength() >=Game.MAX_PLAYER_STRENGTH ){
			this.setCurrentStrength(Game.MAX_PLAYER_STRENGTH); //Se a força ultrapassar os 10 pontos então é colocada a 10.
		}
		deadPlayer.setCurrentStrength((byte) 0); //O player morto tem  a sua força alterada para 0 pontos
	}
	public void setCell(Cell c) {
		if(this.isDead() || this.getCurrentStrength() == Game.MAX_PLAYER_STRENGTH) return;
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
