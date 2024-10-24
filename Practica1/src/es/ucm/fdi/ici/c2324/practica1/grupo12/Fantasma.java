package es.ucm.fdi.ici.c2324.practica1.grupo12;

import pacman.game.Constants.GHOST;


public class Fantasma {
   
    private double distancia;
    private GHOST ghostType;
   
    
    public Fantasma(double distancia,GHOST ghostType) {
        this.distancia = distancia;
        this.ghostType = ghostType;
    }

  
    public GHOST getGhostType() {
        return ghostType;
    }
    
    public double getDistanca() {
   	 return distancia;
   }

    public void setDistanca(double distancia) {
    	 this.distancia = distancia;
    }
}
