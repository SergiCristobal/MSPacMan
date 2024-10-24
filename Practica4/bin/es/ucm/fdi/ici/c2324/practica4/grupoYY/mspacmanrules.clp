;FACTS ASSERTED BY GAME INPUT
(deftemplate MSPACMAN 
    (slot pacmanPosition (type NUMBER))  
    (slot allEdible (type SYMBOL))  
    (slot allOut (type SYMBOL))  
    (slot closestEdibleGhost (type NUMBER))    
    (slot closestNotEdibleGhost (type NUMBER))  
    (slot closestEdibleGhostDist (type NUMBER))
    (slot closestNotEdibleGhostDist (type NUMBER))   
    (slot closestPill (type NUMBER))  
    (slot closestPP (type NUMBER))  
    (slot movesLibres (type SYMBOL))  
    (slot numActiveGhosts (type NUMBER))  
    (slot pacmanGetsFirst (type SYMBOL))   
    (slot edible (type SYMBOL))  
    (slot numEdibleGhosts(type INTEGER))
    
    )
    
(deftemplate BLINKY
	(slot edible (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL)))
	
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id)
	(slot info (default ""))
	(slot priority (type NUMBER))
	
	 ;
) 

;RULES 

	
(defrule irPillCercana
	(MSPACMAN (closestEdibleGhostDist ?cegd))(test (>= ?cegd 60) )
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (>= ?cnegd 50) )
	=>
	(assert (ACTION (id irPillCercana) (info "Pill Cercana --> ir")  (priority 10) )))	

	
(defrule irAGhostMasCercano
	(MSPACMAN (closestEdibleGhostDist ?cegd))(test (<= ?cegd 60) )
	=> 
	(assert (ACTION (id irAGhostMasCercano) (info "Ghost comestible --> perseguir")  (priority 40) )))	

(defrule atacaMayorConcentracion
	(MSPACMAN (numEdibleGhosts ?neg))(test(>= ?neg 3) )
	(MSPACMAN (closestEdibleGhostDist ?cegd))(test (<= ?cegd 60) )
	=> 
	(assert (ACTION (id atacaMayorConcentracion) (info "Todos comestibles --> ir donde hay mas")  (priority 10) )))	
	
(defrule huirAPill
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 100) )
	(MSPACMAN (closestPill ?cp))(test (>= ?cp 100) )

	=> 
	(assert (ACTION (id huirAPill) (info "Pill cerca --> perseguir")  (priority 10) )))	
	
(defrule huirAPP
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 70) )
	(MSPACMAN (movesLibres true))
	(MSPACMAN (closestPP ?cpp))(test (<= ?cpp 100) )
	(MSPACMAN (allEdible false) )
	=> 
	(assert (ACTION (id huirAPP) (info "PP cerca --> perseguir")  (priority 10) )))	
	
(defrule huirEncierros
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 100) )
	(MSPACMAN (closestPP ?cpp))(test (>= ?cnegd 100) )
	=> 
	(assert (ACTION (id huirEncierros) (info "Ghost cerca --> huir de encierros")  (priority 10) )))	
	
(defrule huirHuecoLibre
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 50) )
	(MSPACMAN (movesLibres true) )
	=> 
	(assert (ACTION (id huirHuecoLibre) (info "Hueco libre --> huir")  (priority 50) )))	
	
(defrule huirFantasma
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 50) )
	(MSPACMAN (movesLibres true) )
	(MSPACMAN (closestPP ?cpp))(test (>= ?cnegd 100) )
	=> 
	(assert (ACTION (id huirFantasma) (info "Fantasma cerca sin PPill --> huir")  (priority 30) )))
	
(defrule huirAGhostMasLejano
    (MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 50) )
	(MSPACMAN (movesLibres false) )
	=> 
	(assert (ACTION (id huirAGhostMasLejano) (info "No huecos libres --> huir")  (priority 40) )))	

(defrule kamikazeComer
	(MSPACMAN (closestNotEdibleGhostDist ?cnegd))(test (<= ?cnegd 50) )
	(MSPACMAN (closestPP ?cpp))(test (<= ?cpp 60) )
	(MSPACMAN (pacmanGetsFirst true) )
	=> 
	(assert (ACTION (id kamikazeComer) (info "Pacman llega antes --> comer")  (priority 60) )))	
	