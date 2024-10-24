package es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	CBRCase mostSimilarCase;
	double similarity;

	private final static int TIME_WINDOW = 5;

	public GhostsStorageManager() {
		this.buffer = new Vector<CBRCase>();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setCaseBase(CBRCaseBase caseBase) {
		this.caseBase = caseBase;
	}

	public void reviseAndRetain(CBRCase newCase, CBRCase similarCase, double similarity) {
		this.buffer.add(newCase);
		this.mostSimilarCase = similarCase;
		this.similarity = similarity;
		// Buffer not full yet.
		if (this.buffer.size() < TIME_WINDOW)
			return;

		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase);

	}

	// VER COMO HA IDO EL CASO
	private void reviseCase(CBRCase bCase) {
		GhostsDescription description = (GhostsDescription) bCase.getDescription();
		int score = 0;
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int scoreResult = currentScore - oldScore;

		int oldLives = description.getPacmanLives();
		int currentLives = game.getPacmanNumberOfLivesRemaining();
		int livesResult = currentLives - oldLives;

		double oldDistances = getoldDistances(description);
		double currDistances = getCurrDistances();
		double distancesDiff = currDistances - oldDistances;

		double oldDistance = getoldDistance(description);
		double currDistance = getCurrDistance(description);
		double distanceDiff = currDistance - oldDistance;

		double distancePP = description.getDistPacmanNearestPP();

		int currPPleft = game.getActivePowerPillsIndices().length;
		int oldPPleft = description.getNumPPLeft();
		int ppDiff = currPPleft - oldPPleft; // PUEDE HABER 0 O NEGATIVO

		boolean isEdible = getEdible(description);

		GhostsResult result = (GhostsResult) bCase.getResult();
		double distPacmanPP = description.getDistPacmanNearestPP();

		if (isEdible || distPacmanPP < 30) {

			score = 5 * (int) distanceDiff; // beneficia que se separen
			if (livesResult == -1) {
				score += 40;
			}

		} else {
			if (currPPleft == -1) {
				score = -(10 * (int) distanceDiff);
			} else {
				score = -(5 * (int) distanceDiff);
			}
			if (livesResult == -1) {
				score += 40;
			}
		}

		if (score > 100)
			score = 100;
		if (score < -100)
			score = -100;

		result.setScore(score);
	}

	private double getCurrDistance(GhostsDescription description) {
		GHOST g = description.getGhostToMove();
		double dist = 0.0;
		if (game.getGhostLairTime(g) == 0) {
			dist = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),
					game.getGhostLastMoveMade(g), DM.PATH);
		}

		return dist;
	}

	private double getoldDistance(GhostsDescription description) {
		GHOST g = description.getGhostToMove();
		double dist;
		if (g == GHOST.BLINKY) {
			dist = description.getDistPacmanBLINKY();
		} else if (g == GHOST.INKY) {
			dist = description.getDistPacmanINKY();
		} else if (g == GHOST.PINKY) {
			dist = description.getDistPacmanPINKY();
		} else {
			dist = description.getDistPacmanSUE();
		}
		return dist;
	}

	private boolean getEdible(GhostsDescription description) {
		GHOST g = description.getGhostToMove();
		boolean dist;
		if (g == GHOST.BLINKY) {
			dist = description.getBlinkyEdible();
		} else if (g == GHOST.INKY) {
			dist = description.getInkyEdible();
		} else if (g == GHOST.PINKY) {
			dist = description.getPinkyEdible();
		} else {
			dist = description.getSueEdible();
		}
		return dist;
	}

	private double getCurrDistances() {
		double d = 0.0;

		for (GHOST g : GHOST.values()) {
			if (game.getGhostLairTime(g) == 0) {
				d += game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),
						game.getGhostLastMoveMade(g), DM.PATH);
			}

		}
		d /= 4;
		return d;
	}

	private double getoldDistances(GhostsDescription description) {
		double d = 0.0;
		int count = 0;
		if (description.getDistPacmanBLINKY() != null) {
			d += description.getDistPacmanBLINKY();
			count++;
		}
		if (description.getDistPacmanINKY() != null) {
			d += description.getDistPacmanINKY();
			count++;
		}
		if (description.getDistPacmanPINKY() != null) {
			d += description.getDistPacmanPINKY();
			count++;
		}
		if (description.getDistPacmanSUE() != null) {
			d += description.getDistPacmanSUE();
			count++;
		}

		return d / count;

	}

	// GUARDAR UN CASO
	private void retainCase(CBRCase bCase) {
		// Store the old case right now into the case base
		// Alternatively we could store all them when game finishes in close() method
		int score = ((GhostsResult) bCase.getResult()).getScore();
		if (mostSimilarCase != null) {
			if (similarity < 0.70) {
				if (((score > 85 || score < -85))) {
					StoreCasesMethod.storeCase(this.caseBase, bCase);
				}

			} else {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
			}
			/*
			 * else {
			 * if (!solution.getAction().equals(solution2.getAction())) {
			 * StoreCasesMethod.storeCase(this.caseBase, bCase);
			 * }
			 * }
			 */
		} else {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
	}

	public void close() {
		for (CBRCase oldCase : this.buffer) {
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
