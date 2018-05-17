package Animaux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Terrain.Case;
import Terrain.Coordonnees;
import Terrain.Grille;



public abstract class Fourmi extends Animal{
	
	
	/**
	 * Coordonnees de la fourmiliere
	 */
	private Coordonnees coordonneesFourmiliere; 
	
	
	
	/**
	 * Retour fourmiliere : boolean qui vaut 
	 * true si la fourmi doit rentrer a la fourmiliere pour se nourrir
	 * false sinon
	 */
	private boolean retourFourmiliere;
	
	/**
	 * Valeur seuil de nourriture de l'animal : nombre de detour que peut faire la fourmi en rentrant a la fourmiliere 
	 * (si un obstacle apparait sur son chemin par exemple). 
	 * Cela se traduit par : valeurSeuiManger + distanceFourmiliere < Points de satiete
	 */
	private int valeurSeuilManger;
	


	/**
	 * Grille sur laquelle la fourmi evolue 
	 */
	private final Grille grille;
	
	
	/**
	 * Getter de l'attribut retourFourmiliere
	 * @return boolean true si un retour a la fourmiliere est necessaire, false sinon
	 */
	public boolean isretourFourmiliere() {
		return retourFourmiliere;
	}

	/**
	 * Setter de retourFourmiliere
	 * @param retourFourmiliere booleen true si la fourmi doit retourner a la fourmiliere,
	 * false sinon.
	 */
	public void setRetourFourmiliere(boolean retourFourmiliere) {
		this.retourFourmiliere = retourFourmiliere;
	}

	/**
	 * Getter de la grille sur laquelle evolue la fourmi 
	 * @return grille
	 */
	public Grille getGrille() {
		return grille;
	}

	
	/**
	 * @param pointsDeVie nombre de points de vie a la naissance
	 * @param dureeDeVie nombre maximal de tours avant la mort
	 * @param pointsDeSatiete nombre de tours maximum sans manger avant de mourrir de faim (instanciee au maximum)
	 * @param position case sur laquelle se trouve l'ennemi
	 * @param quantiteNourritureCorrespondante nombre de points de satiete gagne par un ennemi qui mangerait la fourmi une fois morte 
	 * @param grille grille sur laquelle evolue la fourmi
	 * @param distanceDetection distance a laquelle la fourmi voit la nourriture/les pheromones/les ennemis
	 */
	public Fourmi(int pointsDeVie, int dureeDeVie, int pointsDeSatiete, Case position, Grille grille,
			int quantiteNourritureCorrespondante, int distanceDetection) {
		super(pointsDeVie, dureeDeVie, pointsDeSatiete, position, quantiteNourritureCorrespondante, distanceDetection);
		this.retourFourmiliere = false;
		this.coordonneesFourmiliere = grille.getCoordonneesFourmiliere();
		this.grille = grille;
		this.valeurSeuilManger = 2;
	}
	
	/**
	 * Fait manger la fourmi 
	 * Fonction qui augmente les points de satiete de la fourmi 
	 * si les points de satiete de la fourmi sont en dessous de la valeur seuil
	 * et jusque la quantite maximum de nourriture mangeable soit atteinte (s'il y a assez de nourriture a disposition)
	 * diminue d'autant la quantite de nourriture sur la case
	 */
	public void manger(){
			//recuperation de la quantite de nourriture sur la case
			int StockNourriture = this.getPosition().getStockNourriture();
			//augmentation des points de satiete 
			int quantiteNourritureMangee = Math.min(StockNourriture, getPointsDeSatieteMax() - getpointsDeSatiete());
			this.setpointsDeSatiete(getpointsDeSatiete() + quantiteNourritureMangee) ;
			//mise a jour de la quantite de nourriture sur la case
			this.getPosition().disparitionNourriture(quantiteNourritureMangee);
	}
	
	@Override
	/**
	 * Etude des alentours de la position actuelle de la foumi en incluant la case correspondant a la fourmiliere
	 * @param  coordonnnesFourmiliere coordonnees de la fourmiliere qui est cette fois accessible 
	 * @return deplacementPossible  liste de Case aux alentours de la position actuelle de l'animal 
	 * et sur lesquelles l'animal peut aller (pas d'obstacle, pas d'animaux sur ces cases)
	 */
	public List<Case> etudierAlentours(){
		//creation de la liste des deplacments possibes
		List<Case> deplacementPossible = new ArrayList<Case>(); 
		//recuperation des cases adjacentes a celle sur laquelle se trouve l'animal
		Collection<Case> Alentours = getPosition().getAlentours().values();
		//on ajoute celles qui sont occupables a la liste
		for (Case caseAlentours : Alentours){
			if (caseAlentours.isOccupable()){
				deplacementPossible.add(caseAlentours);
			}
		}
		return deplacementPossible;
	}
	

	
	/**
	 * Methode permettant de faire s'eloigner une fourmi de la fourmiliere. 
	 * Si pas possible de s'eloigner deplacement aleatoire classique
	 */
	public void eloignementFourmiliere() {
		//on recupere les deplacements possibles
		List<Case> deplacementsPossibles = this.etudierAlentours();
		boolean deplacementEffectue = false;
		int rang = 0;
		while(deplacementEffectue == false && rang<deplacementsPossibles.size()) {
			// selection d'un deplacement possible
			Case CaseAEtudier = deplacementsPossibles.get(rang);
			Coordonnees coordonneesAEtudier=CaseAEtudier.getCoordonnees();
			//on regarder si il nous eloigne de la fourmiliere
			if(coordonneesAEtudier.calculDistance(this.coordonneesFourmiliere) 
					> this.getPosition().getCoordonnees().calculDistance(this.coordonneesFourmiliere)) {
				//si oui on effectue le deplacement
				this.seDeplacer(CaseAEtudier);
				deplacementEffectue = true;
			}
			// sinon on passe a la case suivante
			else{
				rang += 1;
			}
		}
		if(deplacementEffectue == false) {
			this.seDeplacerAleatoirement();
		}
	}
	
	
  
	/**
	 * Determine si un retour a la fourmiliere est necessaire et 
	 * modifie la valeur des attributs retourFourmiliere et destinatiion le cas echeant
	 */
	public void determinationSiRetourFourmiliere(){
		int distanceFourmiliere = this.getPosition().getCoordonnees().calculDistance(this.coordonneesFourmiliere);
		if(distanceFourmiliere + this.valeurSeuilManger >= this.getpointsDeSatiete()-1) {
			this.retourFourmiliere = true;
			this.setcoordonneesDestination(this.coordonneesFourmiliere);
		}
	}
	
	/**
	 * Fait agir la fourmi lorsqu'elle se trouve sur la fourmiliere
	 */
	public abstract void actionSurFourmiliere();
	
	/**
	 * Fait agir la fourmi lorsqu'elle se trouve en dehors de la fourmiliere
	 */
	public abstract void actionHorsFourmiliere();
	

}
