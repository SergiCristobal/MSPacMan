package es.ucm.fdi.ici.c2324.practica3.grupo04;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2324.practica3.grupo04.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

    GhostsCBRengine cbrEngine;
    GhostsStorageManager storageManager;

    public Ghosts() {
        this.storageManager = new GhostsStorageManager();
        cbrEngine = new GhostsCBRengine(storageManager);
    }

    @Override
    public void preCompute(String opponent) {
        cbrEngine.setOpponent(opponent);
        try {
            cbrEngine.configure();
            cbrEngine.preCycle();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postCompute() {
        try {
            cbrEngine.postCycle();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        EnumMap<GHOST, MOVE> result = new EnumMap<GHOST, MOVE>(GHOST.class);

        try {
            for (GHOST ghost : GHOST.values()) {

                if (game.doesGhostRequireAction(ghost)) {
                    GhostsInput in = new GhostsInput(game);
                    in.parseInput2(ghost);

                    storageManager.setGame(game);
                    cbrEngine.setManager(storageManager);
                    cbrEngine.cycle(in.getQuery());
                    MOVE move = cbrEngine.getSolution();

                    result.put(ghost, move);
                }

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
