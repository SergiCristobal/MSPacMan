package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	int id;

	int nearestGhost;
	Boolean edible;
	int nearestPP;
	int score;
	int time;
	int edibleGhost;
	Double[]  distGhosts;
	Double[] distGhostsEdible;
	int remainingPP;
	Double closestPPDist;
	Double closestPillDist;
	int remainingLives;

	MOVE[] movTowardsGhost;
	MOVE[] movTowardsGhostEdible;
	MOVE[] movesLibres;

	public MOVE[] getMovTowardsGhost() {
		return movTowardsGhost;

	}

	public MOVE[] getMovTowardsGhostEdible() {
		return movTowardsGhostEdible;

	}

	public MOVE[] getMovLibres() {
		return movesLibres;

	}

	public void setMovTowardsGhost(MOVE[] movTowardsGhost) {
		this.movTowardsGhost = movTowardsGhost;

	}

	public void setMovTowardsGhostEdible(MOVE[] movTowardsGhostEdible) {
		this.movTowardsGhostEdible = movTowardsGhostEdible;

	}

	public void setMovLibres(MOVE[] movesLibres) {
		this.movesLibres = movesLibres;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNearestGhost() {
		return nearestGhost;
	}

	public void setNearestGhost(int nearestGhost) {
		this.nearestGhost = nearestGhost;
	}

	public Boolean getEdible() {
		return edible;
	}

	public void setEdible(Boolean edible) {
		this.edible = edible;
	}

	public int getNearestPP() {
		return nearestPP;
	}

	public void setNearestPP(int nearestPP) {
		this.nearestPP = nearestPP;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getEdibleGhost() {
		return edibleGhost;
	}

	public void setEdibleGhost(int edibleGhosts) {
		this.edibleGhost = edibleGhosts;
	}

	public Double[]  getDistGhosts() {
		return distGhosts;
	}

	public void setDistGhosts(Double[]  distGhosts2) {
		this.distGhosts = distGhosts2;
	}

	public Double[]  getDistGhostsEdible() {
		return distGhostsEdible;
	}

	public void setDistGhostsEdible(Double[]  distGhostsEdible2) {
		this.distGhostsEdible = distGhostsEdible2;
	}

	public int getRemainingPP() {
		return remainingPP;
	}

	public void setRemainingPP(int remainingPP) {
		this.remainingPP = remainingPP;
	}

	public Double getClosestPPDist() {
		return closestPPDist;
	}

	public void setClosestPPDist(Double closestPPDist) {
		this.closestPPDist = closestPPDist;
	}

	public Double getClosestPillDist() {
		return closestPillDist;
	}

	public void setClosestPillDist(Double closestPillDist) {
		this.closestPillDist = closestPillDist;
	}

	public int getRemainingLives() {
		return remainingLives;
	}

	public void setRemainingLives(int remainingLives) {
		this.remainingLives = remainingLives;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	// int id;
	//
	// int nearestGhost;
	// Boolean edible;
	// Integer nearestPP;
	// Integer score;
	// Integer time;
	// Integer edibleGhost;
	// Integer[] distGhosts;
	// Integer[] distGhostsEdible;
	// Integer remainingPP;
	// Double closestPPDist;
	// Double closestPillDist;
	// Integer remainingLives;distGhosts
	// MOVE[] movTowardsGhost; //A�ADIR
	// MOVE[] movTowardsGhostEdible; //A�ADIR
	// MOVE[] movesLibres; //A�ADIR
	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", score=" + score + ", time=" + time + ", nearestPP=" + nearestPP
				+ ", nearestGhost=" + nearestGhost + ", edibleGhost=" + edibleGhost + ", edible=" + edible + ","
				+ " distGhosts=" + distGhosts + "," + " distGhostsEdible=" + distGhostsEdible + ", remainingPP="
				+ remainingPP + ", closestPPDist=" + closestPPDist
				+ ", closestPillDist=" + closestPillDist + ", remainingLives=" + remainingLives + ", movesLibres=" + movesLibres +", movTowardsGhostEdible=" + movTowardsGhostEdible +", movTowardsGhost=" + movTowardsGhost + "]";
	}

}
