package es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends CBRInput {

	public GhostsInput(Game game) {
		super(game);

	}

	GHOST ghostToMove;
	Boolean ghostToMoveEdible;
	Integer score;
	Integer time;
	Integer PacmanLives;
	Integer numPPLeft;

	Double DistPacmanINKY;
	Double DistPacmanBLINKY;
	Double DistPacmanSUE;
	Double DistPacmanPINKY;
	Double distPacmanNearestPP;

	MOVE inkyMoveToPacman;
	MOVE blinkyMoveToPacman;
	MOVE sueMoveToPacman;
	MOVE pinkyMoveToPacman;

	MOVE lastPacmanMove;

	Boolean inkyEdible;
	Boolean blinkyEdible;
	Boolean sueEdible;
	Boolean pinkyEdible;

	// Boolean pillsLeft;
	// Boolean ppLeft;

	Boolean pp1left = false;
	Boolean pp2left = false;
	Boolean pp3left = false;
	Boolean pp4left = false;

	@Override
	public void parseInput() {
		// computeNearestGhost(game);
		// computeNearestPPill(game);

		computeDistancesGhostPacmanMain(game);
		computeDistPacmanNEarestPP(game);
		computePacmanLives(game);
		computeMoveToPacmanMain(game);
		computeIsGhostEdible(game);
		lastPacmanMove = game.getPacmanLastMoveMade();
		// pillsLeft = (game.getActivePillsIndices().length == 0);
		// ppLeft = (game.getActivePowerPillsIndices().length == 0);
		int[] PPills = game.getPowerPillIndices();
		for (int pp : game.getActivePowerPillsIndices()) {
			if (pp == PPills[0]) {
				pp1left = true;
			} else if (pp == PPills[1]) {
				pp2left = true;
			} else if (pp == PPills[2]) {
				pp3left = true;
			} else if (pp == PPills[3]) {
				pp4left = true;
			}
		}
		numPPLeft = game.getActivePowerPillsIndices().length;
		time = game.getTotalTime();
		score = game.getScore();
		ghostToMoveEdible = game.isGhostEdible(ghostToMove);
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		// description.setEdibleGhost(edible);
		// description.setNearestGhost(nearestGhost);
		// description.setNearestPPill(nearestPPill);
		description.setScore(score);
		description.setTime(time);
		description.setGhostToMove(ghostToMove);
		description.setGhostToMoveEdible(ghostToMoveEdible);

		description.setBlinkyEdible(blinkyEdible);
		description.setSueEdible(sueEdible);
		description.setInkyEdible(inkyEdible);
		description.setPinkyEdible(pinkyEdible);

		description.setBlinkyMoveToPacman(blinkyMoveToPacman);
		description.setSueMoveToPacman(sueMoveToPacman);
		description.setPinkyMoveToPacman(pinkyMoveToPacman);
		description.setInkyMoveToPacman(inkyMoveToPacman);

		description.setPacmanLives(PacmanLives);
		description.setNumPPLeft(numPPLeft);

		description.setLastPacmanMove(lastPacmanMove);

		description.setDistPacmanBLINKY(DistPacmanBLINKY);
		description.setDistPacmanINKY(DistPacmanINKY);
		description.setDistPacmanPINKY(DistPacmanPINKY);
		description.setDistPacmanSUE(DistPacmanSUE);
		description.setDistPacmanNearestPP(distPacmanNearestPP);

		// description.setPpLeft(ppLeft);
		// description.setPillsLeft(pillsLeft);

		description.setPp1left(pp1left);
		description.setPp2left(pp2left);
		description.setPp3left(pp3left);
		description.setPp4left(pp4left);

		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}

	// --------------------------------------------------------------------------------------------------------------------------------->

	private void computeIsGhostEdible(Game game) {
		blinkyEdible = edibleGhost(game, GHOST.BLINKY);
		inkyEdible = edibleGhost(game, GHOST.INKY);
		sueEdible = edibleGhost(game, GHOST.SUE);
		pinkyEdible = edibleGhost(game, GHOST.PINKY);
	}

	private Boolean edibleGhost(Game game, GHOST g) {
		return game.isGhostEdible(g);
	}

	private void computeMoveToPacmanMain(Game game) {
		blinkyMoveToPacman = computeMoveToPacman(game, GHOST.BLINKY);
		inkyMoveToPacman = computeMoveToPacman(game, GHOST.INKY);
		sueMoveToPacman = computeMoveToPacman(game, GHOST.SUE);
		pinkyMoveToPacman = computeMoveToPacman(game, GHOST.PINKY);
	}

	private MOVE computeMoveToPacman(Game game, GHOST g) {
		return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(),
				game.getGhostLastMoveMade(g), DM.PATH);
	}

	private void computePacmanLives(Game game) {
		this.PacmanLives = game.getPacmanNumberOfLivesRemaining();
	}

	public void computeDistPacmanNEarestPP(Game game) {

		int[] powerPills = game.getActivePowerPillsIndices();

		double closestDistance = Integer.MAX_VALUE;
		int closest = game.getPacmanCurrentNodeIndex();
		for (int i = 0; i < powerPills.length; i++) {
			if (game.getDistance(game.getPacmanCurrentNodeIndex(), powerPills[i], game.getPacmanLastMoveMade(),
					DM.PATH) < closestDistance) {
				closest = powerPills[i];

			}
		}
		closestDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), closest,
				game.getPacmanLastMoveMade(), DM.PATH);
		this.distPacmanNearestPP = closestDistance;

	}

	private void computeDistancesGhostPacmanMain(Game game) {

		if (game.getGhostLairTime(GHOST.BLINKY) == 0) {
			DistPacmanBLINKY = computeDistancesGhostPacman(game, GHOST.BLINKY);

		}
		if (game.getGhostLairTime(GHOST.INKY) == 0) {
			DistPacmanINKY = computeDistancesGhostPacman(game, GHOST.INKY);

		}
		if (game.getGhostLairTime(GHOST.SUE) == 0) {
			DistPacmanSUE = computeDistancesGhostPacman(game, GHOST.SUE);

		}
		if (game.getGhostLairTime(GHOST.PINKY) == 0) {
			DistPacmanPINKY = computeDistancesGhostPacman(game, GHOST.PINKY);

		}

	}

	private double computeDistancesGhostPacman(Game game, GHOST ghost) {
		return game.getDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
	}

	public void parseInput2(GHOST ghost) {
		// computeNearestGhost(game);
		// computeNearestPPill(game);
		ghostToMove = ghost;
		computeDistancesGhostPacmanMain(game);
		computeDistPacmanNEarestPP(game);
		computePacmanLives(game);
		computeMoveToPacmanMain(game);
		computeIsGhostEdible(game);
		lastPacmanMove = game.getPacmanLastMoveMade();
		// pillsLeft = (game.getActivePillsIndices().length == 0);
		// ppLeft = (game.getActivePowerPillsIndices().length == 0);
		int[] PPills = game.getPowerPillIndices();
		for (int pp : game.getActivePowerPillsIndices()) {
			if (pp == PPills[0]) {
				pp1left = true;
			} else if (pp == PPills[1]) {
				pp2left = true;
			} else if (pp == PPills[2]) {
				pp3left = true;
			} else if (pp == PPills[3]) {
				pp4left = true;
			}
		}
		numPPLeft = game.getActivePowerPillsIndices().length;
		time = game.getTotalTime();
		score = game.getScore();
		ghostToMoveEdible = game.isGhostEdible(ghostToMove);
	}

	// private void computeNearestGhost(Game game) {
	// nearestGhost = Integer.MAX_VALUE;
	// edible = false;
	// GHOST nearest = null;
	// for (GHOST g : GHOST.values()) {
	// int pos = game.getGhostCurrentNodeIndex(g);
	// int distance;
	// if (pos != -1)
	// distance = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), pos,
	// DM.PATH);
	// else
	// distance = Integer.MAX_VALUE;
	// if (distance < nearestGhost) {
	// nearestGhost = distance;
	// nearest = g;
	// }
	// }
	// if (nearest != null)
	// edible = game.isGhostEdible(nearest);
	// }

	// private void computeNearestPPill(Game game) {
	// nearestPPill = Integer.MAX_VALUE;
	// for (int pos : game.getPowerPillIndices()) {
	// int distance = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), pos,
	// DM.PATH);
	// if (distance < nearestGhost)
	// nearestPPill = distance;
	// }
	// }
}
