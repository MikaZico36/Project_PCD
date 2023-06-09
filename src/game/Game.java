package game;



import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;


public class Game extends Observable  {
	public static final int SERVER_PORT = 2438;

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	public static final int NUM_PLAYERS = 90;
	public static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;
	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final byte MAX_PLAYER_STRENGTH = 10;
	public static final byte INITIAL_HUMAN_STRENGTH = 5;
	public static final long INITIAL_WAITING_TIME = 10000;

	protected Cell[][] board;
	private static Game game = null;
	private final Podio podio = new Podio(NUM_FINISHED_PLAYERS_TO_END_GAME);

	private Game() {
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y), this);
	}
	public Game(Cell[][] board) {
		this.board = board;
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
	 */
	public void addPlayerToGame(Player player)  {
		if(!player.isHumanPlayer()) { //Se o player for um bot terá um tratamento diferente do cliente
			Thread botPlayer = new Thread((BotPlayer) player);
			botPlayer.start();		//Na thread do BotPlayer, este ja se coloca numa Cell aleatoria
		}
		else {
			getRandomCell().spawnPlayer(player);
		}
	}

	public Podio getPodio() {
		return podio;
	}



	public Cell getCell(Coordinate at) {
		if(at.getX() >= DIMX || at.getX() < 0 || at.getY() >= DIMY || at.getY() < 0)
			return null;
		return board[at.x][at.y];
	}

	public Cell[][] getBoard() {
		return board;
	}  //Permite aceder à matriz de Cells do Game
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
