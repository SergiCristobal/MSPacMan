package es.ucm.fdi.ici.c2324.practica1.grupo12;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class Ghosts extends GhostController {
	// clase con metodos estaticos

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();

	public String getName() {
		return "Ghost A12";
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		int limitToPill = 50;
		int limitToPacMan = 50;
		moves.clear();

		for (GHOST ghostType : GHOST.values()) {

			if (game.doesGhostRequireAction(ghostType)) {
				int ghost = game.getGhostCurrentNodeIndex(ghostType);
				MOVE previousMove = game.getGhostLastMoveMade(ghostType);
				MOVE move = null;
				if (!game.isGhostEdible(ghostType) && !Actions.pacManCloseToPPill(game, limitToPill)) {

					MOVE[] ghostPMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), previousMove);
					MOVE[] pacManPMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),
							game.getPacmanLastMoveMade());

					switch (ghostType) {
					case BLINKY:

						move = Actions.perseguir(previousMove == null ? MOVE.NEUTRAL : previousMove,
								game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game);
						break;
					case INKY:
						move = Actions.cubrirPPill(previousMove == null ? MOVE.NEUTRAL : previousMove,
								game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game);
						break;
					case PINKY:
						move = Actions.acorralar(previousMove == null ? MOVE.NEUTRAL : previousMove,
								game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game);
						break;
					case SUE:
						move = Actions.acorralar(previousMove == null ? MOVE.NEUTRAL : previousMove,
								game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game);
						break;
					default:
						break;

					}
					moves.put(ghostType, move);
					game.getNeighbour(limitToPacMan, previousMove == null ? MOVE.NEUTRAL : previousMove);
				} else {
					move = Actions.huir(previousMove == null ? MOVE.NEUTRAL : previousMove,
							game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game);
				}
//				if (Actions.pacManCloseToPPill(game, limitToPacMan)) {
//					if (Actions.getDistanceToPacMan(game, ghost) <= limitToPacMan) {
//						// Si está "cerca" de pacman
//
//					} else if (Actions.getDistanceToPacMan(game, ghost) < limitToPacMan * 2) {
//
//					} else {
//
//					}
			}

		}

		return moves;
	}

}
