package es.ucm.fdi.ici.c2324.practica1.grupo12;

import java.util.Random;

import gate.util.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.internal.Ghost;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Actions {
	static Color[] colours = { Color.RED, Color.PINK, Color.CYAN, Color.ORANGE };
	private static boolean isPacmanPilled = false;

	public static boolean todosFuera(Game game) {
		boolean correcto = true;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) > 0) {
				correcto = false;
			}
		}
		return correcto;
	}

	public static boolean todosComibles(Game game) {
		boolean correcto = true;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostEdibleTime(ghostType) <= 0) {
				correcto = false;
			}
		}
		return correcto;
	}

	public static GHOST getNearestGhost(Game game) {
		return getNearestGhost(game, Double.MAX_VALUE);
	}

	public static GHOST getNearestActiveGhost(Game game) {
		for (GHOST g : GHOST.values()) {
			if (!game.isGhostEdible(g) && g == getNearestGhost(game)) {
				return g;
			}
		}
		return null;
	}

	public static GHOST getNearestGhost(Game game, double limit) {

		GHOST nearestGhost = null;
		double minDistance = Double.MAX_VALUE;

		int pacManNodeIndex = game.getPacmanCurrentNodeIndex();

		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) <= 0) {
				int ghostNodeIndex = game.getGhostCurrentNodeIndex(ghostType);
				double distance = game.getDistance(pacManNodeIndex, ghostNodeIndex, DM.PATH);

				if (distance < minDistance && (distance < limit)) {
					minDistance = distance;
					nearestGhost = ghostType;

				}
			}
		}

		return nearestGhost;
	}

	public static double getDistanceToPacMan(Game game, int ghost) {
		int pacManNodeIndex = game.getPacmanCurrentNodeIndex();
		return game.getDistance(pacManNodeIndex, ghost, DM.PATH);
	}

	public static boolean pacManCloseToPPill(Game game, int limite) {
		boolean cerca = false;
		int[] activePowerPills = game.getActivePowerPillsIndices();
		int pacManNodeIndex = game.getPacmanCurrentNodeIndex();

		for (int pillNodeIndex : activePowerPills) {

			double distance = game.getDistance(pacManNodeIndex, pillNodeIndex, DM.PATH);

			if (distance < limite) {
				cerca = true;
			}

		}

		return cerca;
	}

	public static int getNearestPPill(Game game, int pos) {

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

	public static int getNearestPill(Game game, int pos) {

		int[] activePills = game.getActivePillsIndices();
		int[] activePowerPills = game.getActivePowerPillsIndices();

		double nearestPill = Double.MAX_VALUE;
		double minDistance = Double.MAX_VALUE;

		for (int pillNodeIndex : activePills) {

			double distance = game.getDistance(pos, pillNodeIndex, DM.PATH);

			if (distance < minDistance) {
				minDistance = distance;
				nearestPill = pillNodeIndex;

			}
		}

		for (int pillNodeIndex : activePowerPills) {

			double distance = game.getDistance(pos, pillNodeIndex, DM.PATH);

			if (distance < minDistance) {
				minDistance = distance;
				nearestPill = pillNodeIndex;

			}

		}

		return (int) nearestPill;
	}

	private static Random rnd = new Random();
	private static MOVE[] allMoves = MOVE.values();

	public static MOVE perseguir(MOVE previousMove, int caza, int presa, Game game) {
		MOVE move = game.getApproximateNextMoveTowardsTarget(caza, presa, previousMove, DM.PATH);
		if (game.getPossibleMoves(presa).length == 1) {
			game.getApproximateNextMoveAwayFromTarget(presa, caza, previousMove, DM.PATH);
		}

		return move;
	}

	public static MOVE huir(MOVE previousMove, int caza, int presa, Game game) {
		MOVE move = game.getApproximateNextMoveAwayFromTarget(presa, caza,
				previousMove == null ? MOVE.NEUTRAL : previousMove, DM.PATH);
		return move;
	}

	public static MOVE cubrirPPill(MOVE previousMove, int ghost, int pm, Game game) {
		// Cubre la ppill mas cercana a pacman
		int i = 0;
		MOVE move = null;
		if (allMoves.length > 0) {
			i = rnd.nextInt(allMoves.length);
			move = allMoves[i];
		}
		if (Actions.pacManCloseToPPill(game, i)) {
			move = perseguir(previousMove, ghost,
					game.getClosestNodeIndexFromNodeIndex(pm, game.getActivePowerPillsIndices(), DM.PATH), game);

		}
		return move;
	}

	public static MOVE huirPacman(Game game) {
		// hacer un struc de pos ds y lastmove
		int pacman = game. getPacmanCurrentNodeIndex();
		MOVE[] posiblesMoves = game.getPossibleMoves(pacman,  game.getPacmanLastMoveMade());
		
		int numFantasmas = GHOST.values().length;
		Fantasma[] fantasmas = new Fantasma[numFantasmas];
		int index = 0; //  ndice para mantener un seguimiento de los fantasmas v lidos
		for (GHOST ghostType : GHOST.values()) {
			//guardar ghostype yo creo no funciona bien el huir
		    //if (game.getGhostLairTime(ghostType) <= 0) { 
		        int ghostNodeIndex = game.getGhostCurrentNodeIndex(ghostType);
		        double distance = game.getDistance(pacman, ghostNodeIndex, DM.PATH);
		        // Asignar valores a los vectores
		      
		        fantasmas[index] = new Fantasma(distance,ghostType);
		        index++;
		    //}
		}
		
		
	
		Arrays.sort(fantasmas, (f1, f2) -> Double.compare(f1.getDistanca(), f2.getDistanca()));
		MOVE move = null;
		MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos
		for (int i = 0; i < numFantasmas; i++) {
			// ver tamben lastmove de los fantasmas
			if( game.getGhostLairTime(fantasmas[i].getGhostType()) <= 0 && fantasmas[i].getDistanca() < 60 
					&& game.getGhostLastMoveMade(fantasmas[i].getGhostType()) != game.getPacmanLastMoveMade() &&
					game.getGhostEdibleTime(fantasmas[i].getGhostType()) <= 0) {
				MOVE movePE = null;
				GameView.addPoints(game,colours[fantasmas[i].getGhostType().ordinal()],game.getShortestPath(game.getGhostCurrentNodeIndex(fantasmas[i].getGhostType()),pacman));
				int node = game.getShortestPath(game.getGhostCurrentNodeIndex(fantasmas[i].getGhostType()),pacman)[0];
				movePE= game.getNextMoveTowardsTarget(pacman, node, DM.PATH);
				boolean repe = false;

				for(MOVE m : movesEvitar) {
					if(m  == movePE) {
						repe = true;
					}
				}
				if(!repe) {
					movesEvitar[i] = movePE;
				}
			// si ya esta metido que no se meta;
			}
		}
			// si ya esta metido que no se meta;
			MOVE[] libres = new MOVE[4];
			
			//ARREGLAR
			int posM = -1 , posE = -1;
			int contador = 0;
			int i = 0,j = 0;
			boolean igual;
			
			for(MOVE moveP : posiblesMoves) {
			
				igual = false;
				System.out.println(" moveP " +moveP);
				j = 0;
				for (MOVE moveE: movesEvitar) {
					System.out.println(" moveE " + moveE);
					if(moveP == moveE) {
						igual = true;
						if(posM == -1) {
							posM = i;
							posE = j;
						}
						else {
							if(posE < j) {
								posM = i;
								posE = j;
							}
						}
					}
					j++;
				}
				if(!igual) {
					libres[contador] = moveP;
					contador++;
				}
				i++;
			}
			
			if(contador == 0) { //Arreglar
				System.out.println("NO MOVES LIBRES");
				if (posM >= 0) {
				    move = posiblesMoves[posM];
				    System.out.println(posM);
				} else {
				    
				    move = game.getPacmanLastMoveMade();
				}
				
			}
			else {
				if(contador > 1) { // ver que move le renta mas
					System.out.println("VARIOS LIBRES");

					boolean encontrado = false; 
					move = libres[rnd.nextInt(contador)];
					for (int x = 0; x < contador ;x++) {
						System.out.println(libres[x]);
						System.out.println ("pp:"+game.getApproximateNextMoveTowardsTarget(pacman,getNearestPPill(game,pacman), game.getPacmanLastMoveMade(), DM.PATH));
						if(libres[x] == game.getApproximateNextMoveTowardsTarget(pacman,getNearestPPill(game,pacman) ,
								game.getPacmanLastMoveMade(), DM.PATH)) {
							move = libres[x];
							encontrado = true;
							System.out.println("Super PLL");
						}
					}
					if(!encontrado) {
						for (int x = 0; x < contador ;x++) {
							System.out.println(libres[x]);
							System.out.println("p:" + game.getApproximateNextMoveTowardsTarget(pacman,getNearestPill(game,pacman), game.getPacmanLastMoveMade(), DM.PATH));
							if(libres[x] == game.getApproximateNextMoveTowardsTarget(pacman,getNearestPill(game,pacman) ,
									game.getPacmanLastMoveMade(), DM.PATH)) {
								move = libres[x];
								encontrado = true;
								System.out.println(" PLL");
							}
						}
						
					}
					
				}
				else {
					move = libres[0];
				}
			}
			
			
			System.out.println(move);
			return move;
		}
		
		public static MOVE goToP(Game game) {
			int pacman = game. getPacmanCurrentNodeIndex();
			int pill = Actions.getNearestPill(game,pacman);
			MOVE previousMove = game.getPacmanLastMoveMade();
			MOVE move = null;
			move = game.getApproximateNextMoveTowardsTarget(pacman,pill ,
					previousMove, DM.PATH);
			if(game.getActivePowerPillsIndices().length > 0) {
				if(todosFuera(game) && !todosComibles(game) ) {
					move = game.getApproximateNextMoveTowardsTarget(pacman, Actions.getNearestPPill(game,pacman),
	    					previousMove, DM.PATH);
				}
			}

			return move;
		}
	

		
		public static MOVE huirPacman2(Game game) {
			int limit = 20; // perseguir lugares vacios haciendo horizontal
			// hacer un struc de pos ds y lastmove
			int pacman = game. getPacmanCurrentNodeIndex();
			MOVE[] posiblesMoves = game.getPossibleMoves(pacman,  game.getPacmanLastMoveMade());
			
			int numFantasmas = GHOST.values().length;
			Fantasma[] fantasmas = new Fantasma[numFantasmas];
			int index = 0; //  ndice para mantener un seguimiento de los fantasmas v lidos
			for (GHOST ghostType : GHOST.values()) {
				//guardar ghostype yo creo no funciona bien el huir
			    //if (game.getGhostLairTime(ghostType) <= 0) { 
			        int ghostNodeIndex = game.getGhostCurrentNodeIndex(ghostType);
			        double distance = game.getDistance(pacman, ghostNodeIndex, DM.PATH);
			        // Asignar valores a los vectores
			      
			        fantasmas[index] = new Fantasma(distance,ghostType);
			        index++;
			    //}
			}
			
			
		
			Arrays.sort(fantasmas, (f1, f2) -> Double.compare(f1.getDistanca(), f2.getDistanca()));

			MOVE move = null;

			MOVE[] movesEvitar = new MOVE[4]; // Inicializa el arreglo con 4 elementos

			for (int i = 0; i < numFantasmas; i++) {

				// ver tamben lastmove de los fantasmas

				if( game.getGhostLairTime(fantasmas[i].getGhostType()) <= 0 && fantasmas[i].getDistanca() < 60 
						&& game.getGhostLastMoveMade(fantasmas[i].getGhostType()) != game.getPacmanLastMoveMade() &&
						game.getGhostEdibleTime(fantasmas[i].getGhostType()) <= 0) {
					MOVE movePE = null;
					
					
					
					GameView.addPoints(game,colours[fantasmas[i].getGhostType().ordinal()],game.getShortestPath(game.getGhostCurrentNodeIndex(fantasmas[i].getGhostType()),pacman));
					int node = game.getGhostCurrentNodeIndex(fantasmas[i].getGhostType());
					int vecino = node;
					boolean inter = false;
					int t = 0;
					while (!inter && vecino != -1) {
						//comprobamos inter
						t = 0;
						if(game.getPossibleMoves(vecino).length > 2) {
							inter = true;
						}
						node = vecino;
						//System.out.println(1);
						vecino = game.getNeighbour(node, game.getGhostLastMoveMade(fantasmas[i].getGhostType())); // cogemos el siguiente
						while(vecino == -1 && !inter && game.getPossibleMoves(node) != null && t < game.getPossibleMoves(node).length) {
							System.out.println(t);
							vecino = game.getNeighbour(node, game.getPossibleMoves(node)[t]);// cogemos el siguiente
							t = t + 1;
						}
					}
					if(vecino != -1) {
						node = vecino;
						System.out.println("NO FUE -1");
					}
					if(game.getDistance(pacman, node,game.getPacmanLastMoveMade(), DM.PATH) >= game.getDistance(game.getGhostCurrentNodeIndex(fantasmas[i].getGhostType()), node,game.getGhostLastMoveMade(fantasmas[i].getGhostType()), DM.PATH)) {
						movePE= game.getNextMoveTowardsTarget(pacman, node, DM.PATH);
						boolean repe = false;
						
						for(MOVE m : movesEvitar) {
							if(m  == movePE) {
								repe = true;
							}

						}
						if(!repe) {// si ya esta metido que no se meta;
							movesEvitar[i] = movePE;
						}
					}
					
				
				}

			}

				MOVE[] libres = new MOVE[4];
				
				//ARREGLAR
				int posM = -1 , posE = -1;
				int contador = 0;
				int i = 0,j = 0;
				boolean igual;
				
				for(MOVE moveP : posiblesMoves) {
				
					igual = false;
					System.out.println(" moveP " +moveP);
					j = 0;
					for (MOVE moveE: movesEvitar) {
						System.out.println(" moveE " + moveE);
						if(moveP == moveE) {
							igual = true;
							if(posM == -1) {
								posM = i;
								posE = j;
							}
							else {
								if(posE < j) {
									posM = i;
									posE = j;
								}
							}
						}
						j++;
					}
					if(!igual) {
						libres[contador] = moveP;
						contador++;
					}
					i++;
				}
				
				if(contador == 0) { //Arreglar
					System.out.println("NO MOVES LIBRES");
					if (posM >= 0) {
					    move = posiblesMoves[posM];
					    System.out.println(posM);
					} else {
					    
					    move = game.getPacmanLastMoveMade();
					}
					
				}
				else {
					if(contador > 1) { // ver que move le renta mas
						System.out.println("VARIOS LIBRES");
						
						boolean encontrado = false; 
						move = libres[rnd.nextInt(contador)]; 
						for (int x = 0; x < contador ;x++) {
							System.out.println(libres[x]);
							System.out.println ("pp:"+game.getApproximateNextMoveTowardsTarget(pacman,getNearestPPill(game,pacman), game.getPacmanLastMoveMade(), DM.PATH));
							if(libres[x] == game.getApproximateNextMoveTowardsTarget(pacman,getNearestPPill(game,pacman) ,
									game.getPacmanLastMoveMade(), DM.PATH)) {
								move = libres[x];
								encontrado = true;
								System.out.println("Super PLL");
							}
						}
						if(!encontrado) {
							for (int x = 0; x < contador ;x++) {
								System.out.println(libres[x]);
								System.out.println("p:" + game.getApproximateNextMoveTowardsTarget(pacman,getNearestPill(game,pacman), game.getPacmanLastMoveMade(), DM.PATH));
								if(libres[x] == game.getApproximateNextMoveTowardsTarget(pacman,getNearestPill(game,pacman) ,
										game.getPacmanLastMoveMade(), DM.PATH)) {
									move = libres[x];
									encontrado = true;
									System.out.println(" PLL");
								}
							}
							
						}
						
					}
					else {
						move = libres[0];
					}
				}
				
				
				System.out.println(move);
				return move;
			}

	

	public static MOVE acorralar(MOVE previousMove, int pacman, int ghost, Game game) {
		double distance = getDistanceToPacMan(game, ghost);

		MOVE[] movesG = game.getPossibleMoves(ghost, previousMove);
		MOVE[] movesP = game.getPossibleMoves(pacman, previousMove);

		MOVE move = perseguir(previousMove, pacman, ghost, game);

		for (MOVE m : movesG) {

			int nextG = game.getNeighbour(ghost, m);
			int nextP = game.getNeighbour(pacman, game.getPacmanLastMoveMade());
			// Si con el siguiente movimiento las distancias siguen siendo las mismas, toma
			// el segundo camino más rápido
			if (game.getDistance(nextG, nextP, DM.PATH) == distance) {
				if (move != m)
					// A veces devuelve excepción, ya que
					return game.getApproximateNextMoveTowardsTarget(ghost, pacman, previousMove, DM.MANHATTAN);

			}
//			if (game.getDistance(nextG, game.getPacmanCurrentNodeIndex(), previousMove, DM.PATH) <= distance) {
//				distance = game.getDistance(nextG, game.getPacmanCurrentNodeIndex(), m, DM.PATH);
//				move = m;
//			}

		}
		return move;
	}
//		int limit = 10;
//		MOVE[] movesP = game.getPossibleMoves(pacman, previousMove);
//		for (MOVE m : movesP) {
//			int nextPos = game.getNeighbour(pacman, m);
//			if (game.getPossibleMoves(nextPos, m).length == 1) {
//				return m;
//			}
//		}
//		// Si la distancia del fantasma a pacman es menor que el limite, movimientos
//		// aleatorios
//		if (limit < getDistanceToPacMan(game, ghost)) {
//			return game.getApproximateNextMoveTowardsTarget(ghost, pacman,
//					previousMove == null ? MOVE.NEUTRAL : previousMove, DM.PATH);
//		} else
//			// si se acerca al lÃ­mite, el fantasma se mueve hacia el pacman
//			return game.getApproximateNextMoveTowardsTarget(ghost, pacman,
//					previousMove == null ? MOVE.NEUTRAL : previousMove, DM.EUCLID);

	public static MOVE superPillPacman(MOVE previousMove, int pacman, int ghost, Game game) {
		int limiteSuper = 50;
		MOVE move = null;
		if (getNearestPPill(game, pacman) < limiteSuper && todosFuera(game)) {
			move = perseguir(previousMove, pacman, ghost, game);
		}
		// devolver null si no es momento para perseguirla para q haga otra cosa
		return move;
	}

	public static MOVE random() {
		return allMoves[rnd.nextInt(allMoves.length)];// random
	}

	public static MOVE huirDoble(Object caza, Object presa, Game game) {
		return null;
	}

	public static MOVE vulnerablePresa(MOVE previousMove, int caza, int presa, Game game) {
		MOVE move = game.getApproximateNextMoveAwayFromTarget(presa, caza,
				previousMove == null ? MOVE.NEUTRAL : previousMove, DM.PATH);

		return move;
	}

	public static MOVE vulnerableCaza(Object caza, Object presa, Game game) {
		return null;
	}

	public static MOVE cebo(MOVE previousMove, int ghost, int mspacman, Game game) {
		MOVE move = MOVE.NEUTRAL;
		for (MOVE m : game.getPossibleMoves(ghost, previousMove)) {
			if (m.compareTo(previousMove) >= 0) {
				move = m;
			}
		}
		return move;
	}

}