package gui;

import java.util.Observable;
import java.util.Observer;
import game.Game;
import game.Player;
import game.botPlayer;

import javax.swing.JFrame;

public class GameGuiMain implements Observer {
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	public GameGuiMain() {
		super();
		game = Game.getGame();
		game.addObserver(this);

		buildGui();

	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);


		frame.setSize(800,800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() throws InterruptedException  {
		frame.setVisible(true);

		// Demo players, should be deleted
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		botPlayer player = new botPlayer(1, (byte)3);
		botPlayer playerO = new botPlayer(2, (byte)1);

		// TODO no fim tirar comentarios para ter 90 jogadores (so fazer depois de implementar confronto)
		for(int i = 0; i < Game.NUM_PLAYERS; i++) {
			byte power = (byte) Math.round(Math.random()*3);
			if(power == 0)	power = 1;

			botPlayer bot = new botPlayer(i, power);
			game.addPlayerToGame(bot);
			new Thread(bot).start();

		}
		//game.addPlayerToGame(player);
		//game.addPlayerToGame(playerO);

		//TODO Quando colocamos dois players na mesma Cell o primeiro a ser colocado anda mas o segundo fica parado

		//game.addPlayerToGame(new PhoneyHumanPlayer(2, game, (byte)2));
		//game.addPlayerToGame(new PhoneyHumanPlayer(3, game, (byte)1));


		//Thread playert = new Thread(player);
		//Thread playerOne = new Thread(playerO);

		//playerOne.start();
		//playert.start();

		
	/*	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO Temporario, remover depois de implementar movimento
		for(int x = 0; x < Game.DIMX; x++) {
			 for(int y = 0; y < Game.DIMY; y++) {
				 Cell c = game.getCell(new Coordinate(x,y));
				 if(c.isOcupied()) {
					 c.unsetPlayer();
				 }
			 }
		}*/

	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public static void main(String[] args) throws InterruptedException {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}
}
