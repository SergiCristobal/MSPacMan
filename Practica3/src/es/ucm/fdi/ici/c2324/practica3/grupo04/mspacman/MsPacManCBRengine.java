package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;

	final static String TEAM = "grupo04"; // Cuidado!! poner el grupo aquí

	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/" + TEAM
			+ "/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata" + File.separator + TEAM + File.separator + "mspacman"
			+ File.separator;

	public MsPacManCBRengine(MsPacManStorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();

		// connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));

		// Do not use default case base path in the xml file. Instead use custom file
		// path for each opponent.
		// Note that you can create any subfolder of files to store the case base inside
		// your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent + ".csv");

		this.storageManager.setCaseBase(caseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("score", MsPacManDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("time", MsPacManDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("nearestPP", MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("nearestGhost", MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("edibleGhost", MsPacManDescription.class), new Interval(4));
		simConfig.addMapping(new Attribute("distGhosts", MsPacManDescription.class),new IntervalDouble(1000));
		simConfig.addMapping(new Attribute("distGhostsEdible", MsPacManDescription.class), new IntervalDouble(1000));
		simConfig.addMapping(new Attribute("remainingPP", MsPacManDescription.class), new Interval(4));
		simConfig.addMapping(new Attribute("closestPPDist", MsPacManDescription.class), new Interval(40));
		simConfig.addMapping(new Attribute("closestPillDist", MsPacManDescription.class), new Interval(15));
		simConfig.addMapping(new Attribute("remainingLives", MsPacManDescription.class), new Interval(3));
		simConfig.addMapping(new Attribute("movesLibres", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("movTowardsGhost", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("movTowardsGhostEdible", MsPacManDescription.class), new Equal());
		// a�adir el xml de distGhostsEdible y los moves[]

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if (caseBase.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		} else {
			// Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query,
					simConfig);
			// Compute reuse
			this.action = reuse(eval);
		}

		// Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);

	}

	private MOVE reuse(Collection<RetrievalResult> eval) // donde se cogen los casos
	{

		List<RetrievalResult> casos = (List<RetrievalResult>) SelectCases.selectTopKRR(eval, 5);
		if (casos.size() < 5) {
			return MOVE.NEUTRAL;
		}
		Comparator<RetrievalResult> customComparator = new Comparator<RetrievalResult>() {
			public int compare(RetrievalResult re1, RetrievalResult re2) {
				CBRCase case1 = re1.get_case();
				CBRCase case2 = re2.get_case();
				MsPacManResult result1 = (MsPacManResult) case1.getResult();
				MsPacManResult result2 = (MsPacManResult) case2.getResult();

				double similarity1 = re1.getEval();
				double similarity2 = re2.getEval();

				double totalScore1 = result1.getScore() * similarity1;
				double totalScore2 = result2.getScore() * similarity2;

				// Sort in descending order
				return Double.compare(totalScore2, totalScore1);
			}
		};

		Collections.sort(casos, customComparator);
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		RetrievalResult first = casos.get(0);

		// cogemos los 5 mas similares y ordenamos de segun su similaridad * puntuacion
		// cogemos el mejor
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
		MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();

		// Now compute a solution for the query

		// Here, it simply takes the action of the 1NN
		MOVE action = solution.getAction();

		RetrievalResult last = casos.get(4);
		RetrievalResult secondLast = casos.get(3);

		CBRCase lastSimilarCase = last.get_case();
		CBRCase secondLastSimilarCase = secondLast.get_case();

		double lastSimilarity = last.getEval();
		double secondLastSimilarity = secondLast.getEval();

		MsPacManResult lastResult = (MsPacManResult) lastSimilarCase.getResult();
		MsPacManResult secondLastResult = (MsPacManResult) secondLastSimilarCase.getResult();

		MsPacManSolution lastSolution = (MsPacManSolution) lastSimilarCase.getSolution();
		MsPacManSolution secondLastSolution = (MsPacManSolution) secondLastSimilarCase.getSolution();

		// But if not enough similarity or bad case, choose another move randomly
		if ((similarity < 0.7) || (result.getScore() < 100)) { // evitar malos resultados

			// HACER Q COMPRUEBE LA SIMILARITY DE LOS CASOS MALOS

			ArrayList<MOVE> arrayMoves = new ArrayList<>(Arrays.asList(MOVE.values()));

			int contador = 0;

			if (lastSimilarity >= 0.7 || lastResult.getScore() >= 100) {
				arrayMoves.remove(lastSolution.getAction());
				contador++;
			}
			if (secondLastSimilarity >= 0.7 || secondLastResult.getScore() >= 100) {
				arrayMoves.remove(secondLastSolution.getAction());
				contador++;
			}
			int index = (int) Math.floor(Math.random() * 3);
			if (MOVE.values()[index] == action) {
				index = (index +1) % (3 - contador);
			}
			action = arrayMoves.get(index);

		}
		return action;
	}

	/**
	 * Creates a new case using the query as description,
	 * storing the action into the solution and
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		int newId = this.caseBase.getNextId();
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
		this.storageManager.close();
		this.caseBase.close();
	}

}
