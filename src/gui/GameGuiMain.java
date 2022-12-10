package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import environment.Direction;
import game.ClientHandler;
import game.Game;
import game.BotPlayer;

import javax.swing.JFrame;

public class GameGuiMain implements Observer, Serializable {
	private JFrame frame = new JFrame("pcd.io");
	private static BoardJComponent boardGui;
	private Game game;



	public GameGuiMain() {
		super();
		game = Game.getGame();
		game.addObserver(this);

		buildGui();

	}

	public static BoardJComponent getBoardGui(){
		return boardGui;
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game, true);
		frame.add(boardGui);


		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException, IOException {
			new GameServer().start();
			frame.setVisible(true);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//game.getPodio().start();

			for (int i = 0; i < Game.NUM_PLAYERS; i++) {
				byte power = (byte) Math.round(Math.random() * Game.MAX_INITIAL_STRENGTH);
				if (power == 0) power = 1;

				BotPlayer bot = new BotPlayer(i, power, game.getPodio());
				game.addPlayerToGame(bot);

			}

	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		GameGuiMain game = new GameGuiMain();
		game.init();


	}

	public class GameServer extends Thread{ //TODO Verificar se faz sentido isto ser uma Thread
		private final Game game = Game.getGame();
		private final ServerSocket ss = new ServerSocket(Game.SERVER_PORT);

		public GameServer() throws IOException {
		}

		@Override
		public void run() {
			while(true) {
				try {
					Socket clientSocket = ss.accept();		// Este metodo faz um wait() enquanto espera por pedidos
					new ClientHandler(clientSocket, boardGui).start();

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
