package es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman.MsPacManDescription;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
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

	Boolean pp1left;
	Boolean pp2left;
	Boolean pp3left;
	Boolean pp4left;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getPacmanLives() {
		return PacmanLives;
	}

	public void setPacmanLives(Integer pacmanLives) {
		PacmanLives = pacmanLives;
	}

	public Double getDistPacmanINKY() {
		return DistPacmanINKY;
	}

	public void setDistPacmanINKY(Double distPacmanINKY) {
		DistPacmanINKY = distPacmanINKY;
	}

	public Double getDistPacmanBLINKY() {
		return DistPacmanBLINKY;
	}

	public void setDistPacmanBLINKY(Double distPacmanBLINKY) {
		DistPacmanBLINKY = distPacmanBLINKY;
	}

	public Double getDistPacmanSUE() {
		return DistPacmanSUE;
	}

	public void setDistPacmanSUE(Double distPacmanSUE) {
		DistPacmanSUE = distPacmanSUE;
	}

	public Double getDistPacmanPINKY() {
		return DistPacmanPINKY;
	}

	public void setDistPacmanPINKY(Double distPacmanPINKY) {
		DistPacmanPINKY = distPacmanPINKY;
	}

	public Double getDistPacmanNearestPP() {
		return distPacmanNearestPP;
	}

	public void setDistPacmanNearestPP(Double distPacmanNearestPP) {
		this.distPacmanNearestPP = distPacmanNearestPP;
	}

	public MOVE getInkyMoveToPacman() {
		return inkyMoveToPacman;
	}

	public void setInkyMoveToPacman(MOVE inkyMoveToPacman) {
		this.inkyMoveToPacman = inkyMoveToPacman;
	}

	public MOVE getBlinkyMoveToPacman() {
		return blinkyMoveToPacman;
	}

	public void setBlinkyMoveToPacman(MOVE blinkyMoveToPacman) {
		this.blinkyMoveToPacman = blinkyMoveToPacman;
	}

	public MOVE getSueMoveToPacman() {
		return sueMoveToPacman;
	}

	public void setSueMoveToPacman(MOVE sueMoveToPacman) {
		this.sueMoveToPacman = sueMoveToPacman;
	}

	public MOVE getPinkyMoveToPacman() {
		return pinkyMoveToPacman;
	}

	public void setPinkyMoveToPacman(MOVE pinkyMoveToPacman) {
		this.pinkyMoveToPacman = pinkyMoveToPacman;
	}

	public MOVE getLastPacmanMove() {
		return lastPacmanMove;
	}

	public void setLastPacmanMove(MOVE lastPacmanMove) {
		this.lastPacmanMove = lastPacmanMove;
	}

	public Boolean getInkyEdible() {
		return inkyEdible;
	}

	public void setInkyEdible(Boolean inkyEdible) {
		this.inkyEdible = inkyEdible;
	}

	public Boolean getBlinkyEdible() {
		return blinkyEdible;
	}

	public void setBlinkyEdible(Boolean blinkyEdible) {
		this.blinkyEdible = blinkyEdible;
	}

	public Boolean getSueEdible() {
		return sueEdible;
	}

	public void setSueEdible(Boolean sueEdible) {
		this.sueEdible = sueEdible;
	}

	public Boolean getPinkyEdible() {
		return pinkyEdible;
	}

	public void setPinkyEdible(Boolean pinkyEdible) {
		this.pinkyEdible = pinkyEdible;
	}

	/*
	 * public Boolean getPillsLeft() {
	 * return pillsLeft;
	 * }
	 * 
	 * public void setPillsLeft(Boolean pillsLeft) {
	 * this.pillsLeft = pillsLeft;
	 * }
	 * 
	 * public Boolean getPpLeft() {
	 * return ppLeft;
	 * }
	 * 
	 * public void setPpLeft(Boolean ppLeft) {
	 * this.ppLeft = ppLeft;
	 * }
	 */

	public Boolean getPp1left() {
		return pp1left;
	}

	public void setPp1left(Boolean pp1Left) {
		this.pp1left = pp1Left;
	}

	public Boolean getPp2left() {
		return pp2left;
	}

	public void setPp2left(Boolean pp2Left) {
		this.pp2left = pp2Left;
	}

	public Boolean getPp3left() {
		return pp3left;
	}

	public void setPp3left(Boolean pp3Left) {
		this.pp3left = pp3Left;
	}

	public Boolean getPp4left() {
		return pp4left;
	}

	public void setPp4left(Boolean pp1Left) {
		this.pp4left = pp1Left;
	}

	public Integer getNumPPLeft() {
		return numPPLeft;
	}

	public void setNumPPLeft(Integer numPPLeft) {
		this.numPPLeft = numPPLeft;
	}

	public GHOST getGhostToMove() {
		return ghostToMove;
	}

	public void setGhostToMove(GHOST ghostToMove) {
		this.ghostToMove = ghostToMove;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	public Boolean getGhostToMoveEdible() {
		return ghostToMoveEdible;
	}

	public void setGhostToMoveEdible(Boolean ghostToMoveEdible) {
		this.ghostToMoveEdible = ghostToMoveEdible;
	}

	@Override
	public String toString() {
		return "GhostsDescription [id=" + id + ", ghostToMove=" + ghostToMove + ", ghostToMoveEdible="
				+ ghostToMoveEdible + ", score=" + score + ", time=" + time + ", PacmanLives=" + PacmanLives
				+ ", numPPLeft=" + numPPLeft + ", DistPacmanINKY=" + DistPacmanINKY + ", DistPacmanBLINKY="
				+ DistPacmanBLINKY + ", DistPacmanSUE=" + DistPacmanSUE + ", DistPacmanPINKY=" + DistPacmanPINKY
				+ ", distPacmanNearestPP=" + distPacmanNearestPP + ", inkyMoveToPacman=" + inkyMoveToPacman
				+ ", blinkyMoveToPacman=" + blinkyMoveToPacman + ", sueMoveToPacman=" + sueMoveToPacman
				+ ", pinkyMoveToPacman=" + pinkyMoveToPacman + ", lastPacmanMove=" + lastPacmanMove + ", inkyEdible="
				+ inkyEdible + ", blinkyEdible=" + blinkyEdible + ", sueEdible=" + sueEdible + ", pinkyEdible="
				+ pinkyEdible + ", pp1left=" + pp1left + ", pp2left=" + pp2left + ", pp3left=" + pp3left + ", pp4left="
				+ pp4left + "]";
	}

}
