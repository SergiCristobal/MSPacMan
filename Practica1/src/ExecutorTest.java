import es.ucm.fdi.ici.c2324.practica1.grupo12.Ghosts;
import es.ucm.fdi.ici.c2324.practica1.grupo12.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {
 //Ángel y Sergi	
    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();


       // PacmanController pacMan = new es.ucm.fdi.ici.c2223.practica1.grupo03.MsPacMan();
       GhostController ghosts = new Ghosts();
 
        PacmanController pacMan = new MsPacMan();

        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 30) //last parameter defines speed
        );     
    }
	
}
