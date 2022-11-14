package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class PhoneyHumanPlayer extends Player implements Runnable{
	public PhoneyHumanPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public boolean isHumanPlayer() {
		return true;
	}

	//Função que calcula de forma random para onde o player se vai mover 
	
	public static Direction ChooseMove() {
		Direction d = null;
		int prob = (int) Math.round((Math.random()*3));
		System.out.println(prob);
		switch (prob) {
		case 0 :
			d = Direction.UP;
			break;
		case 1 : 
			d = Direction.RIGHT;
			break;
		case 2 :
			d = Direction.LEFT;
			break;
		case 3 :
			d = Direction.DOWN;
			break;

		}
		return d;
	}


	public void move() {
		Cell current =getCurrentCell();
		Direction d = ChooseMove();
		Coordinate c = d.getVector();
		System.out.println("Cell " + current.getPosition() + " " + "Direction " + c);
		System.out.println("Mudança " + current.getPosition().translate(c));
		
		Cell nova = game.getCell(current.getPosition().translate(c));  //Vou buscar a célula para onde o player quer se mover
		
		nova.setPlayer(this);  //Digo que o player agora faz parte dessa célula
		this.setCell(nova); //Coloco a Cell position da classe Player = nova
		
		game.getCell(current.getPosition()).unsetPlayer(); // Por fim digo que a célula anteriormente ocupada pelo Player ficou livre, logo Player player = null
		
		
		Game.getGame().notifyChange();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
		synchronized(this) {
			move();
			try {
				wait(Game.REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
}
