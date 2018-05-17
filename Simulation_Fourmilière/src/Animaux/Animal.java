package Animaux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import Terrain.Case;
import Terrain.Coordonnees;



public abstract class Animal {

	/**
	 * Coordonnees de la case vers laquelle se dirige l'animal
	 */
	private Coordonnees coordonneesDestination;
	
	/**
	 * Distance a laquelle l'enimal detecte les pheromones/la nourriture/les ennemis/les fourmis(selon son type)
	 */
	private final int distanceDetection;
	/**
	 * Nombre de points de vie de l animal
	 */
	private int pointsDeVie;
	
	/**
	 * Duree de vie de l animal : nombre de tour durant lequel l animal est vivant
	 */
	private int dureeDeVie;
	
	/**
	 * Nombre de points de satiete de l animal
	 */
	private int pointsDeSatiete;
	
	/**
	 * nombre de point de satiete maximum de l animal
	 */
	private final int pointsDeSatieteMax;
	
	/**
	 * Case sur laquelle l animal se situe
	 */
	private Case position;

	/**
	 * booleen false si animal mort, true si vivant 
	 */
	private boolean vivant;
	
	/**
	 * nombre de points de satiete que gagnera un animal s'il mange celui-ci a sa mort
	 */
	private final int quantiteNourritureCorrespondante;
	

	/**
	 * Constructor:L'animal est instancie avec ses points de satiete au maximum. attention TOUJOURS verifier que la case est occupable avant d'instancier
	 * @param pointsDeVie nombre de points de vie a la naissance
	 * @param dureeDeVie nombre maximal de tours avant la mort
	 * @param pointsDeSatiete nombre de tours maximum sans manger avant de mourrir de faim
	 * @param position case sur laquelle se trouve l'animal
	 * @param quantiteNourritureCorrespondante nombre de points de satiete que gagnera un animal s'il mange celui-ci a sa mort
	 * @param distanceDetection Distance a laquelle l'enimal detecte les pheromones/la nourriture/les ennemis/les fourmis(selon son type)
	 * }
	 */
	public Animal(int pointsDeVie,int dureeDeVie,int pointsDeSatiete,Case position,int quantiteNourritureCorrespondante,int distanceDetection) {
		this.quantiteNourritureCorrespondante = quantiteNourritureCorrespondante;
		this.pointsDeVie = pointsDeVie;
		this.distanceDetection = distanceDetection;
		this.dureeDeVie  = dureeDeVie;
		this.pointsDeSatiete = pointsDeSatiete;
		this.pointsDeSatieteMax = pointsDeSatiete;
		this.position = position;
		this.vivant = true;
		position.ajouterOccupant(this);
	}

	/**
	 * Getter des points de Vie de l animal
	 * @return Les points de vie de l animal
	 */
	public int getpointsDeVie() {
		return pointsDeVie;
	}
	
	/**
	 * setter des points de vie de l animal
	 * @param pointsDeVie points de vie de l animal
	 */
	public void setpointsDeVie(int pointsDeVie) {
		this.pointsDeVie = pointsDeVie;
	}

	/**
	 * Getter de la duree de vie restante de l animal
	 * @return duree de vie
	 */
	public int getdureeDeVie() {
		return dureeDeVie;
	}
	
	/**
	 * Setter de la duree de vie de l animal
	 * @param dureeDeVie duree de vie (en tour) de l animal
	 */
	public void setdureeDeVie(int dureeDeVie) {
		this.dureeDeVie = dureeDeVie;
	}

	/**
	 * Getter des points de satiete de l animal
	 * @return pointsDeSatiete point de satiete de l animal
	 */
	public int getpointsDeSatiete() {
		return pointsDeSatiete;
	}
	
	/**
	 * Setter des points de Satiete de l animal
	 * @param pointsDeSatiete point de satiete de l animal
	 */
	public void setpointsDeSatiete(int pointsDeSatiete) {
		this.pointsDeSatiete = pointsDeSatiete;
	}

	
	/**
	 * getter de la position de l'animal
	 * @return la case sur laquelle il se trouve
	 */
	public Case getPosition() {
		return position;
	}

	/**
	 * permetd de mettre a jour la position de l'animal
	 * @param position nouvelle position de l'animal
	 */
	public void setPosition(Case position) {
		this.position = position;
	}
	

	/**
	 * @return la nombre de points de satiete gagne par celui qui mangera cet animal 
	 */
	public int getQuantiteNourritureCorrespondante() {
		return quantiteNourritureCorrespondante;
	}
	
	/**
	 * @return le nombre maximal de points de satiete de l'animal
	 */
	public int getPointsDeSatieteMax() {
		return pointsDeSatieteMax;
	}

	/**
	 * permet de savoir si l'animal est en vie 
	 * @return boolean : true si vivant, false si mort
	 */
	public boolean isVivant() {
		return vivant;
	}
	
	/**
	 * Getter de la Case vers laquelle se dirige l'animal
	 * @return coordonneesDestination
	 */
	public Coordonnees getcoordonneesDestination() {
		return coordonneesDestination;
	}

	/**
	 * Setter de la Case vers laquelle va la'animal
	 * @param coordonneesCase Coordonnees de la case de destination
	 */
	public void setcoordonneesDestination(Coordonnees coordonneesCase) {
		this.coordonneesDestination = coordonneesCase;
	}

	/**
	 * Getter de la distance de detection
	 * @return distanceDetection
	 */
	public int getDistanceDetection() {
		return distanceDetection;
	}
	
	/**
	 * Supprime la destination si elle a ete atteinte
	 */
	public void miseAJourDestination() {
		if(this.getPosition().getCoordonnees() == this.coordonneesDestination) {
			this.coordonneesDestination = null;
		}
	}
	
	/**
	 * Deplace la fourmi vers une case adjacente a sa position en essayant 
	 * dans la mesure du possible de se rapprocher de sa destination, sachant que si ce n'est pas possible, 
	 * la fourmi effectue un deplacement aleatoire (pour eviter l'agglutination des fourmis autour de la fourmiliere)
	 *@return boolean : true si le deplacement a rapproche de la fourmiliere, false sinon
	 */
	public boolean avancerVersDestination(){
		//on recupere les deplacements possibles
		List<Case> deplacementsPossibles = this.etudierAlentours();
		boolean deplacementEffectue = false;
		int rang = 0;
		while(deplacementEffectue == false && rang < deplacementsPossibles.size()) {
			// selection d'un deplacement possible
			Case CaseAEtudier = deplacementsPossibles.get(rang);
			Coordonnees coordonneesAEtudier = CaseAEtudier.getCoordonnees();
			//on regarder s'il nous rapproche de la destination
			if(coordonneesAEtudier.calculDistance(this.getcoordonneesDestination())
					< this.getPosition().getCoordonnees().calculDistance(this.getcoordonneesDestination())) {
				//si oui on avance
				this.seDeplacer(CaseAEtudier);
				deplacementEffectue=true;
			}
			// sinon on passe a la case suivante
			else{
				rang += 1;
			}
		}
		// Si aucune case ne permet de se rapprocher de la destination, la fourmi se deplace aleatoirement
		if(deplacementEffectue == false) {
			this.seDeplacerAleatoirement();
		}
		return(deplacementEffectue);
	}


	/**
	 * Met a jour l etat de l animal (mort ou vivant)
	 */
	public void miseAJourEtat(){
		if(vivant) {
			int pointsDeVie = getpointsDeVie();
			int dureeDeVie = getdureeDeVie();
			int pointsDeSatiete = getpointsDeSatiete();
			if (dureeDeVie ==0 || pointsDeVie <=0 || pointsDeSatiete ==0){
				this.vivant=false;
				}
			}
			else {
				this.vivant=true;
			}
		}
	
	/**
	 * Fonction permettant de supprimer la reference a l'animal de la liste des occupants 
	 * de la case sur laquelle il se trouve. 
	 */
	public void disparaitre() {
		this.getPosition().supprimerOccupant(this);
		this.position.setOccupable(true);
	}
	
	/**
	 * Fonction d'etude des alentours de la position actuelle de l'animal.
	 * @return deplacementPossible : liste des cases sur lesquelles l'animal peut aller 
	 * (pas d'obstacle, pas d'animaux sur ces cases, pas la fourmiliere) 
	 */
	public List<Case> etudierAlentours(){
		//creation de la liste des deplacments possibes
		List<Case> deplacementPossible = new ArrayList<Case>(); 
		//recuperation des cases adjacentes a celle sur laquelle se trouve l'animal
		Collection<Case> Alentours = position.getAlentours().values();
		//on ajoute celles qui sont occupables a la liste
		for (Case caseAlentours : Alentours){
			//on exclut le deplacement vers la fourmiliere, on reecrira la methode dans la classe fourmi
			if (caseAlentours.isOccupable() && caseAlentours.isFourmiliere() == false){
				deplacementPossible.add(caseAlentours);
			}
		}
		return deplacementPossible;
	}

	/**
	 * Deplace l'animal sur la case passee en argument
	 * @param caseSuivante prochaine case occupee par l'animal
	 */
	public void seDeplacer(Case caseSuivante){
		if(caseSuivante.isOccupable()) {
			//on quitte la case
			this.disparaitre();
			// on occupe la case suivante
			if(caseSuivante.isFourmiliere() == false) {
				caseSuivante.setOccupable(false);
			}
			caseSuivante.ajouterOccupant(this);
			this.position = caseSuivante;
		}
	}
	
	/**
	 * Deplace l'animal aleatoirement vers une des cases adjacentes occupables
	 */
	public void seDeplacerAleatoirement(){ 
		//on recupere les alentours et on pioche en excluant la fourmiliere
			List<Case> deplacementPossible =  etudierAlentours();
			int taille = deplacementPossible.size();
			if (taille != 0){
				Random ran=new Random();
				int rang = ran.nextInt(taille);
				Case caseSuivante = deplacementPossible.get(rang);
				seDeplacer(caseSuivante);
			}
	}
	
	/**
	 * Fonction generique faisant diminuer la duree de vie et les points de satiete de 1
	 */
	public void jouerTour() {
		this.dureeDeVie -= 1;
		this.pointsDeSatiete -= 1;
	}

}

	



