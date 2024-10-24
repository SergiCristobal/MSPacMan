package es.ucm.fdi.ici.c2324.practica1.grupo12;

import java.awt.Color;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController{
	 Color[] colours = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
	 private Random rnd = new Random();
	 private MOVE[] allMoves = MOVE.values();
	 
	 
	 public String getName() {
		 return "MsPacMan 12";
	 }
		@Override
		public MOVE getMove(Game game, long timeDue) {
			double limit = 50;
			MOVE previousMove = game.getPacmanLastMoveMade();
	        GHOST cercano = Actions.getNearestGhost(game, limit);
	        
	    	MOVE move = allMoves[rnd.nextInt(allMoves.length)];
	    	int mspacman = game.getPacmanCurrentNodeIndex();
	    	
	    	if (cercano != null) {
	    		int ghost = game.getGhostCurrentNodeIndex(cercano);
		    	
		    	
		    	if(game.getGhostLairTime(cercano)<=0)
		    	GameView.addPoints(game,colours[cercano.ordinal()],game.getShortestPath(ghost,mspacman));
	    		
		    	// comprobar si se lo puede comer o no 
	    		
	    		if (game.getGhostEdibleTime(cercano) > 0) { //vulnerable ir a por el
	    			move = game.getApproximateNextMoveTowardsTarget(mspacman,ghost ,
							previousMove, DM.PATH);
	    		}
	    		else { //huir si no es vulnerable
	    			move = Actions.huirPacman(game);
	    		}
	    		
	    	}
	    	else {
	    		// ir a por pills  int[] getActivePillIndices() , mirar  si tiene cerca
	    		move = Actions.goToP(game);
	    		
	    		
	    	}
			
			return move;
			
		}
		
		
}

