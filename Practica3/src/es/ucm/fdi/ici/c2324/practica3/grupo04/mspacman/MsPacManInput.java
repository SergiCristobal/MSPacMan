package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import java.util.ArrayList;
import java.util.Collections;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);

	}

	// Ghost mas cercana
	int nearestGhost;
	// Hay edibleGhost?
	Boolean edible;
	// Indice de PP mas cercana
	int nearestPP;
	// Puntuacion actual
	int score;
	// Tiempo transcurrido
	int time;
	// Entero que representa cuantos fantasmas están modo edible.
	int edibleGhosts;
	// 4 Double con la distancia de Ms Pacman a los cuatro fantasmas
	Double[]  distGhosts ;
	
	// 4 Double con la distancia de Ms Pacman a los cuatro fantasmas edible
	Double[]  distGhostsEdible ;
	// Entero que represente el número de PP restantes
	int remainingPP;
	// Double para la distancia de pacman a la PP más cercana
	Double closestPPDist;
	// Double con la distancia de Ms Pacman a la pill más cercana
	Double closestPillDist;
	// Número de vidas de Ms Pacman
	int remainingLives;
	// 4 movimientos que son el primer movimiento que Pacman debe hacer para
	// acercarse a cada fantasma.
	MOVE[] movTowardsGhost;
	// acercarse a cada fantasma.
	MOVE[] movTowardsGhostEdible; // moves a fantasma edibles
	// Lista de movimientos libres
	MOVE[] movesLibres;

	@Override
	public void parseInput() {
		computeNearestGhost(game);
		computeEdible(game);
		computeNearestPP(game);
		time = game.getTotalTime();
		score = game.getScore();
		computeEdibleGhosts(game);
		computeDistGhostsEdible();
		computeDistGhosts(game);
		computeRemainingPP(game);
		if(game.getNumberOfActivePowerPills() > 0)
		computeClosestPPDist(game);
		if(game.getNumberOfActivePills() > 0)
		computeClosestPillDist(game);
		computeRemainingLives(game);
		computeMovesToGhostsEdible();
		computeMovesToGhosts();
		computeMovesLibres();
		

	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setEdible(edible);
		description.setNearestGhost(nearestGhost);
		description.setNearestPP(nearestPP);
		description.setScore(score);
		description.setTime(time);
		description.setClosestPillDist(closestPillDist);
		description.setClosestPPDist(closestPPDist);
		description.setDistGhosts(distGhosts);
		description.setDistGhostsEdible(distGhostsEdible);
		description.setEdibleGhost(edibleGhosts);
		description.setRemainingLives(remainingLives);
		description.setRemainingPP(remainingPP);
		description.setMovTowardsGhost(movTowardsGhost);
		description.setMovTowardsGhostEdible(movTowardsGhostEdible);
		description.setMovLibres(movesLibres);

		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}

	public void computeMovesToGhosts() {

		int pacMan = game.getPacmanCurrentNodeIndex();
		MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos
		int contador = 0;
		// ver tamben lastmove de los fantasmas
		for (GHOST ghostType : GHOST.values()) {

			if (game.getGhostLairTime(ghostType) <= 0 && getDistanceToPacMan1(ghostType) < 60
					&& game.getGhostLastMoveMade(ghostType) != game.getPacmanLastMoveMade()
					&& game.getGhostEdibleTime(ghostType) <= 0) {
				MOVE movePE = null;

				int node = game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType), pacMan)[0];
				movePE = game.getNextMoveTowardsTarget(pacMan, node, DM.PATH);
				boolean repe = false;

				for (MOVE m : movesEvitar) {
					if (m == movePE) {
						repe = true;
					}
				}
				if (!repe) {// si ya esta metido que no se meta;
					movesEvitar[contador] = movePE;
					contador++;
				}
			}
		}
		movTowardsGhost = movesEvitar;
	}

	public void computeMovesToGhostsEdible() {

		int pacMan = game.getPacmanCurrentNodeIndex();
		MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos
		int contador = 0;
		// ver tamben lastmove de los fantasmas
		for (GHOST ghostType : GHOST.values()) {

			if (game.getGhostLairTime(ghostType) <= 0 && getDistanceToPacMan1(ghostType) < 60
					&& game.getGhostLastMoveMade(ghostType) != game.getPacmanLastMoveMade()
					&& game.getGhostEdibleTime(ghostType) > 0) {
				MOVE movePE = null;

				int node = game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType), pacMan)[0];
				movePE = game.getNextMoveTowardsTarget(pacMan, node, DM.PATH);
				boolean repe = false;

				for (MOVE m : movesEvitar) {
					if (m == movePE) {
						repe = true;
					}
				}
				if (!repe) {// si ya esta metido que no se meta;
					movesEvitar[contador] = movePE;
					contador++;
				}
			}
		}
		movTowardsGhostEdible = movesEvitar;
	}

	public double getDistanceToPacMan1(GHOST ghostType) {
		int pacManNodeIndex = game.getPacmanCurrentNodeIndex();
		int ghostNodeIndex = game.getGhostCurrentNodeIndex(ghostType);
		return game.getDistance(pacManNodeIndex, ghostNodeIndex, DM.PATH);
	}

	public void computeMovesLibres() {
		// si ya esta metido que no se meta;
		MOVE[] movesEvitar = movTowardsGhost;
		MOVE[] libres = new MOVE[4];
		MOVE[] posiblesMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

		int contador = 0;
		boolean igual;

		for (MOVE moveP : posiblesMoves) {

			igual = false;
			for (MOVE moveE : movesEvitar) {
				if (moveP == moveE) {
	                igual = true;
	                break;
	            }
			}
			if (!igual) {
	            libres[contador] = moveP;
	            contador++;
	        }
			
		}
		movesLibres = libres;
	}

	private void computeNearestGhost(Game game) {
		nearestGhost = Integer.MAX_VALUE;
		edible = false;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int distance;
			if (pos != -1)
				distance = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			else
				distance = Integer.MAX_VALUE;
			if (distance < nearestGhost) {
				nearestGhost = distance;
				nearest = g;
			}
		}
		if (nearest != null)
			edible = game.isGhostEdible(nearest);
	}

	private void computeNearestPP(Game game) {
		nearestPP = Integer.MAX_VALUE;
		for (int pos : game.getPowerPillIndices()) {
			int distance = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if (distance < nearestGhost)
				nearestPP = distance;
		}
	}

	private void computeEdible(Game game) {
		edible = false;
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g))
				edible = true;
		}
	}

	private void computeEdibleGhosts(Game game) {
		edibleGhosts = 0;
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g))
				edibleGhosts++;
		}
	}

	private void computeDistGhosts(Game game) {
		int i = 0;
		distGhosts = new Double[GHOST.values().length];
		for (GHOST g : GHOST.values()) {
			if (game.getGhostLairTime(g) == 0 && game.getGhostEdibleTime(g) <= 0) {
				distGhosts[i] = game.getDistance(game.getGhostCurrentNodeIndex(g),
						game.getPacmanCurrentNodeIndex(), DM.PATH);
			}
			
			i++;
		}
	}

	private void computeDistGhostsEdible() {
		distGhostsEdible = new Double[GHOST.values().length];
		
		int i = 0;
		for (GHOST g : GHOST.values()) {
			if (game.getGhostLairTime(g) == 0 && game.getGhostEdibleTime(g) > 0) {
				distGhostsEdible[i] =  game.getDistance(game.getGhostCurrentNodeIndex(g),
						game.getPacmanCurrentNodeIndex(), DM.PATH);
			}
			i++;
		}
	}

	private void computeRemainingPP(Game game) {
		remainingPP = game.getNumberOfActivePowerPills();
	}

	private void computeClosestPPDist(Game game) {
		if(game.getNumberOfActivePowerPills() > 0)
		closestPPDist = (double) game.getShortestPathDistance(getNearestPP(game.getPacmanCurrentNodeIndex()),
				game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
	}

	private void computeClosestPillDist(Game game) {
		if(game.getNumberOfActivePills() > 0)
		closestPillDist = (double) game.getShortestPathDistance(getNearestPill(game.getPacmanCurrentNodeIndex()),
				game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
	}

	private void computeRemainingLives(Game game) {
		remainingLives = game.getPacmanNumberOfLivesRemaining();
	}

	private void computeMovTowardsGhost(Game game) {
		movTowardsGhost = getMovesToGhosts();
	}

	private void computeMovTowardsGhostEdible(Game game) {
		movTowardsGhostEdible = getMovesToGhostsEdible();
	}

	// FUNCIONES AUXILIARES PARA COMPUTE

	public int getNearestPP(int pos) {

		int[] activePowerPills = game.getActivePowerPillsIndices();

		double nearestPill = Double.MAX_VALUE;
		double minDistance = Double.MAX_VALUE;

		for (int pillNodeIndex : activePowerPills) {

			double distance = game.getDistance(pos, pillNodeIndex, DM.PATH);

			if (distance < minDistance) {
				minDistance = distance;
				nearestPill = pillNodeIndex;
			}

		}

		return (int) nearestPill;
	}

	public int getNearestPill(int pos) {

		int[] activePills = game.getActivePillsIndices();

		double nearestPill = Double.MAX_VALUE;
		double minDistance = Double.MAX_VALUE;

		for (int pillNodeIndex : activePills) {

			double distance = game.getDistance(pos, pillNodeIndex, DM.PATH);

			if (distance < minDistance) {
				minDistance = distance;
				nearestPill = pillNodeIndex;

			}
		}

		return (int) nearestPill;
	}

	public MOVE[] getMovesToGhosts() {

		int pacMan = game.getPacmanCurrentNodeIndex();
		MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos
		int contador = 0;
		// ver tamben lastmove de los fantasmas
		for (GHOST ghostType : GHOST.values()) {

			if (game.getGhostLairTime(ghostType) <= 0 && getDistanceToPacMan1(ghostType) < 60
					&& game.getGhostLastMoveMade(ghostType) != game.getPacmanLastMoveMade()
					&& game.getGhostEdibleTime(ghostType) <= 0) {
				MOVE movePE = null;

				int node = game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType), pacMan)[0];
				movePE = game.getNextMoveTowardsTarget(pacMan, node, DM.PATH);
				boolean repe = false;

				for (MOVE m : movesEvitar) {
					if (m == movePE) {
						repe = true;
					}
				}
				if (!repe) {// si ya esta metido que no se meta;
					movesEvitar[contador] = movePE;
					contador++;
				}
			}
		}
		return movesEvitar;
	}

	public MOVE[] getMovesToGhostsEdible() {

		int pacMan = game.getPacmanCurrentNodeIndex();
		MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos
		int contador = 0;
		// ver tamben lastmove de los fantasmas
		for (GHOST ghostType : GHOST.values()) {

			if (game.getGhostLairTime(ghostType) <= 0 && getDistanceToPacMan1(ghostType) < 60
					&& game.getGhostLastMoveMade(ghostType) != game.getPacmanLastMoveMade()
					&& game.getGhostEdibleTime(ghostType) > 0) {
				MOVE movePE = null;

				int node = game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType), pacMan)[0];
				movePE = game.getNextMoveTowardsTarget(pacMan, node, DM.PATH);
				boolean repe = false;

				for (MOVE m : movesEvitar) {
					if (m == movePE) {
						repe = true;
					}
				}
				if (!repe) {// si ya esta metido que no se meta;
					movesEvitar[contador] = movePE;
					contador++;
				}
			}
		}
		return movesEvitar;
	}

	public double getDistanceToPacMan(GHOST ghostType) {
		int pacManNodeIndex = game.getPacmanCurrentNodeIndex();
		int ghostNodeIndex = game.getGhostCurrentNodeIndex(ghostType);
		return game.getDistance(pacManNodeIndex, ghostNodeIndex, DM.PATH);
	}

	public MOVE[] movesLibres() {
		// si ya esta metido que no se meta;
		MOVE[] movesEvitar = movTowardsGhost;
		MOVE[] libres = new MOVE[4];
		MOVE[] posiblesMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

		// ARREGLAR
		int posM = -1, posE = -1;
		int contador = 0;
		int i = 0, j = 0;
		boolean igual;

		for (MOVE moveP : posiblesMoves) {

			igual = false;

			j = 0;
			for (MOVE moveE : movesEvitar) {

				if (moveP == moveE) {
					igual = true;
					if (posM == -1) {
						posM = i;
						posE = j;
					} else {
						if (posE < j) {
							posM = i;
							posE = j;
						}
					}
				}
				j++;
			}
			if (!igual) {
				libres[contador] = moveP;
				contador++;
			}
			i++;
		}
		return libres;
	}

}
