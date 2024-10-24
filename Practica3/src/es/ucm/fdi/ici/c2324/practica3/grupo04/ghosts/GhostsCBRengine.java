package es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2324.practica3.grupo04.CBRengine.Average;
import es.ucm.fdi.ici.c2324.practica3.grupo04.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2324.practica3.grupo04.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private GhostsStorageManager storageManager;

	private Random rand = new Random();
	private MOVE[] allMoves = MOVE.values();

	CachedLinearCaseBase casebase = null;
	CustomPlainTextConnector connector1;
	CustomPlainTextConnector connector2;
	CachedLinearCaseBase caseBase1;
	CachedLinearCaseBase caseBase2;
	NNConfig simConfig;

	final static String TEAM = "grupo04"; // Cuidado!! poner el grupo aquí

	final static String CONNECTOR_FILE_PATH1 = "es/ucm/fdi/ici/c2324/practica3/" + TEAM
			+ "/ghosts/plaintextconfig1.xml";
	final static String CASE_BASE_PATH1 = "cbrdata" + File.separator + TEAM + File.separator + "Ghosts"
			+ File.separator;

	final static String CONNECTOR_FILE_PATH2 = "es/ucm/fdi/ici/c2324/practica3/" + TEAM
			+ "/ghosts/plaintextconfig2.xml";
	final static String CASE_BASE_PATH2 = "cbrdata" + File.separator + TEAM + File.separator + "Ghosts"
			+ File.separator;

	public GhostsCBRengine(GhostsStorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	@Override
	public void configure() throws ExecutionException {
		connector1 = new CustomPlainTextConnector();
		connector2 = new CustomPlainTextConnector();
		caseBase1 = new CachedLinearCaseBase();
		caseBase2 = new CachedLinearCaseBase();

		connector1.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH1));
		connector2.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH2));

		// Do not use default case base path in the xml file. Instead use custom file
		// path for each opponent.
		// Note that you can create any subfolder of files to store the case base inside
		// your "cbrdata/grupoXX" folder.
		connector1.setCaseBaseFile(CASE_BASE_PATH1, opponent + "Edible.csv");
		connector2.setCaseBaseFile(CASE_BASE_PATH2, opponent + "NoEdible.csv");

		simConfig = new NNConfig();

		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("score", GhostsDescription.class), new Interval(15000));
		simConfig.setWeight(new Attribute("score", GhostsDescription.class), 30.0);

		simConfig.addMapping(new Attribute("time", GhostsDescription.class), new Interval(4000));
		simConfig.setWeight(new Attribute("time", GhostsDescription.class), 5.0);

		simConfig.addMapping(new Attribute("PacmanLives", GhostsDescription.class), new Equal());

		simConfig.addMapping(new Attribute("DistPacmanINKY", GhostsDescription.class), new Interval(40));
		simConfig.addMapping(new Attribute("DistPacmanBLINKY", GhostsDescription.class), new Interval(40));
		simConfig.addMapping(new Attribute("DistPacmanSUE", GhostsDescription.class), new Interval(40));
		simConfig.addMapping(new Attribute("DistPacmanPINKY", GhostsDescription.class), new Interval(40));
		simConfig.addMapping(new Attribute("distPacmanNearestPP", GhostsDescription.class), new Interval(15));

		simConfig.setWeight(new Attribute("DistPacmanINKY", GhostsDescription.class), 200.0);
		simConfig.setWeight(new Attribute("DistPacmanBLINKY", GhostsDescription.class), 200.0);
		simConfig.setWeight(new Attribute("DistPacmanSUE", GhostsDescription.class), 200.0);
		simConfig.setWeight(new Attribute("DistPacmanPINKY", GhostsDescription.class), 200.0);
		simConfig.setWeight(new Attribute("distPacmanNearestPP", GhostsDescription.class), 200.0);

		simConfig.addMapping(new Attribute("inkyMoveToPacman", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("blinkyMoveToPacman", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("sueMoveToPacman", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("pinkyMoveToPacman", GhostsDescription.class), new Equal());

		simConfig.setWeight(new Attribute("inkyMoveToPacman", GhostsDescription.class), 50.05);
		simConfig.setWeight(new Attribute("blinkyMoveToPacman", GhostsDescription.class), 50.05);
		simConfig.setWeight(new Attribute("sueMoveToPacman", GhostsDescription.class), 50.05);
		simConfig.setWeight(new Attribute("pinkyMoveToPacman", GhostsDescription.class), 50.05);

		simConfig.addMapping(new Attribute("lastPacmanMove", GhostsDescription.class), new Equal());
		simConfig.setWeight(new Attribute("lastPacmanMove", GhostsDescription.class), 50.05);

		simConfig.addMapping(new Attribute("inkyEdible", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("blinkyEdible", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("sueEdible", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("pinkyEdible", GhostsDescription.class), new Equal());

		simConfig.setWeight(new Attribute("inkyEdible", GhostsDescription.class), 150.05);
		simConfig.setWeight(new Attribute("blinkyEdible", GhostsDescription.class), 150.05);
		simConfig.setWeight(new Attribute("sueEdible", GhostsDescription.class), 150.05);
		simConfig.setWeight(new Attribute("pinkyEdible", GhostsDescription.class), 150.05);

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase1.init(connector1);
		caseBase2.init(connector2);
		return null;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		CBRCase mostSimilarCase = null;
		double similarity = 0;

		GhostsDescription desc = (GhostsDescription) query.getDescription();
		if (desc.getGhostToMoveEdible()) {
			casebase = caseBase1;
		} else {
			casebase = caseBase2;
		}

		if (casebase.getCases().isEmpty()) {
			this.action = allMoves[rand.nextInt(allMoves.length)];
		} else {
			// Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(casebase.getCases(), query,
					simConfig);
			// Compute reuse
			this.action = reuse(eval);

			RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
			mostSimilarCase = first.get_case();
			similarity = first.getEval();
		}

		// Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, mostSimilarCase, similarity);

	}

	private MOVE reuse(Collection<RetrievalResult> eval) {
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting

		HashMap<MOVE, Double> moves = new HashMap<MOVE, Double>();

		for (MOVE m : MOVE.values()) {
			moves.put(m, -100.0);
		}

		Collection<RetrievalResult> casesList = SelectCases.selectTopKRR(eval, 5);

		for (RetrievalResult Case : casesList) {
			CBRCase mostSimilarCase = Case.get_case();
			double similarity = Case.getEval();
			GhostsResult result = (GhostsResult) mostSimilarCase.getResult();
			GhostsSolution solution = (GhostsSolution) mostSimilarCase.getSolution();
			Double value;
			if (similarity < 0 && result.getScore() < 0) {
				value = -(similarity * result.getScore());
			} else {
				value = (similarity * result.getScore());
			}

			MOVE action = solution.getAction();
			moves.replace(action, moves.get(action) + value); // Sumamos lo que se parece * el score

		}

		// Now compute a solution for the query

		// calculamos qué dirección obtuvo más puntuación

		double maxValue = Double.MIN_VALUE;
		MOVE sol = allMoves[rand.nextInt(allMoves.length)];
		for (MOVE m : moves.keySet()) {
			if (moves.get(m) > maxValue) {
				maxValue = moves.get(m);
				sol = m;
			}
		}
		return sol;
	}

	/**
	 * Creates a new case using the query as description,
	 * storing the action into the solution and
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		int newId = this.casebase.getNextId();
		newId += storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}

	public MOVE getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		casebase = caseBase1;
		this.storageManager.setCaseBase(caseBase1);
		this.storageManager.close();
		this.casebase.close();
		casebase = caseBase2;
		this.storageManager.setCaseBase(casebase);
		this.storageManager.close();
		this.casebase.close();
	}

	public void setManager(GhostsStorageManager st) {
		this.storageManager = st;
		this.storageManager.setCaseBase(casebase);
	}

}
