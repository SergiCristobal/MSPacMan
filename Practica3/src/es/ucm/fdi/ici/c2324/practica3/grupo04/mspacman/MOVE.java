package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

public class MOVE implements TypeAdaptor{
	
	pacman.game.Constants.MOVE m;
	String move;
	@Override
	public void fromString(String content) throws Exception {
		// TODO Auto-generated method stub
	
		switch(content) {
		case "UP": m = pacman.game.Constants.MOVE.UP;
		case "DOWN": m = pacman.game.Constants.MOVE.DOWN;
		case "LEFT":m = pacman.game.Constants.MOVE.LEFT;
		case "RIGHT":m = pacman.game.Constants.MOVE.RIGHT;
			default: break;
		}
	}
	
	public void toString(pacman.game.Constants.MOVE move) {
		switch(move) {
		case UP: this.move = "UP";
		case DOWN: this.move = "DOWN";
		case LEFT:this.move = "LEFT";
		case RIGHT :this.move = "RIGHT";
			default: break;
		}
	}
	

}
