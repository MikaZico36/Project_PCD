package game;


import java.util.ArrayList;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
import gui.GameGuiMain;

import static java.lang.System.exit;

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

	//TODO Verificar se isto está correto
	private static ArrayList<Player> winners;

	//Cores da mensagem de Vitória
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_RESET = "\u001B[0m";

	private Game() {
		board = new Cell[Game.DIMX][Game.DIMY];
	
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(new Coordinate(x, y), this);
	winners =new ArrayList<>();
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
		//Cell initialPos= getCell(new Coordinate(15 + player.getIdentification(),15));

		new BoardThread(initialPos, player).start();

	}
	//APENAS USADA EM TESTES
	/*public synchronized void addPlayerToGame(Player player) throws InterruptedException {
		//Cell initialPos=getRandomCell();
		Cell initialPos= getCell(new Coordinate(10,10));

		new BoardThread(initialPos, player).start();

	}*/

	//TODO Verificar se faz sentido isto
	//Adiciona vencedores ao Vetor que regista os vencedores.
	public synchronized void addWinner(Player winner) {

		if (winners.size() < NUM_FINISHED_PLAYERS_TO_END_GAME) {
			winners.add(winner);
		}
		//TODO Termina o jogo Verificar se está certo
		if (winners.size() >= NUM_FINISHED_PLAYERS_TO_END_GAME) {
			StringBuilder winnersList = new StringBuilder(new String("Os vencedores são os Players "));

			for (Player p : winners) {
				String ap = new String (String.valueOf(p.getIdentification()) + ", ");
				winnersList.append(ap);
			}
			System.out.println( ANSI_GREEN + winnersList + ANSI_RESET);

			exit(0);	//Termina o jogo
		}
	}

	//Retorna a Lista de Players
	public synchronized ArrayList<Player> getWinners(){
		return winners;
	}
	//Retorna o número de vencedores
	public synchronized int numberWinner(){
		return winners.size();
	}

	public Cell getCell(Coordinate at) {
		if(at.getX() >= DIMX || at.getX() < 0 || at.getY() >= DIMY || at.getY() < 0)
			return null;
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
