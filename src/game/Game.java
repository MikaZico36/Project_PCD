package game;


import java.util.Observable;
import environment.Cell;
import environment.Coordinate;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	public static final int NUM_PLAYERS = 90; //TODO ver se podemos deixar isto a public
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;

	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;

	protected Cell[][] board;

	private static Game game = null;
	
	private Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y),this);
	}
	
	/**
	 * De modo a obter sempre a mesma instância da Classe Game sem ter que passar por variáveis, implementou-se o padrão singleton
	 * @return Instância da classe Game
	 */
	public static Game getGame() {
		if(game == null)
			game = new Game();
		return game;
	}
	
	/** Adiciona os jogadores ao Board
	 * @param player O jogador que queremos adicionar
	 * @throws InterruptedException 
	 */
	
	public synchronized void addPlayerToGame(Player player) throws InterruptedException {
		Cell initialPos=getRandomCell();
		//Cell initialPos= getCell(new Coordinate(0,0));

		new BoardThread(initialPos, player).start();

	}

	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	/**	
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
}
