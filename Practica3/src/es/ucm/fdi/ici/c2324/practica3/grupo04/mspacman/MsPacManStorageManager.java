package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts.GhostsDescription;
import es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts.GhostsResult;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase);
		
	}
	
	private void reviseCase(CBRCase bCase) { // ver el resultado del caso calculo score
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		
		int resultScore = 0;
		
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore; // cuanto ha mejorado en escore
		

		int oldLives = description.getRemainingLives();
		int currentLives = game.getPacmanNumberOfLivesRemaining();
		int livesResult = currentLives - oldLives; // vidas

		

		//double distancePP = description.getNearestPP(); // distancia a la ppCercana
		
		int currPPleft = game.getActivePowerPillsIndices().length;
		int oldPPleft = description.getRemainingPP();
		int ppDiff = currPPleft - oldPPleft; // Si se ha comido PP

			if (livesResult != 0) { // PacMan pierde vida
				if (ppDiff == 0) {
					// pacman pierde 1 vida y  no se ha comido ppills
					resultScore -= 100;
				} else { 
					if (resultValue > 1000) { //aprovecho bien
						resultScore -= 30;
					}
					else {//aun no aprovechada
						resultScore -= 160;
					}
				}

			} else if (livesResult == 0) { //PacMan no pierde vida
				if (ppDiff == 0) { // no morir, pero tampoco hacer nada interesante
					resultScore += 40;
				} else { // come una pPill
					if (resultValue > 1000) { //aprovecho bien
						resultScore += 160;
					}
					else {//aun no aprovechada
						resultScore += 100;
					}
				}
			}
		
		resultScore += resultValue / 5;
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultScore);	
	}
	
	
	
	private void retainCase(CBRCase bCase) // cuando guardar el caso
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
