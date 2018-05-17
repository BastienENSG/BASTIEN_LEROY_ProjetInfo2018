/**
 * Projet Informatique Mai 2018 Simulation Fourmiliere
 * @author Marie Bastien
 * @author Guillaume Leroy 
 */

package Animaux;

import java.util.Set;

import Terrain.Case;
import Terrain.Grille;
import Terrain.Pheromone;
import Terrain.TypePheromone;

public class Eclaireuse extends Fourmi{
	
	/**
	 * Nombre de tours avant que le pheromone ne s'efface de la grille
	 */
	public int dureeDeViePheromone;
	
	/**
	 * Constructor
	 * @param dureeDeVie duree de vie de l'eclaireuse
	 * @param pointsDeVie points de vie de l'eclaireuse a la naissance
	 * @param pointsDeSatiete point de satiete de l'eclaireuse
	 * @param position case sur laquelle se trouve l'eclaireuse a la naissance
	 * @param quantiteNourritureCorrespondante nombre de points de satiete gagne par un ennemi qui mangerait cette fourmi une fois morte 
	 * @param grille grille sur laquelle evolue la fourmi
	 * @param distanceDetection distance a laquelle la fourmi voit la nourriture/les pheromones/les ennemis
	 * @param dureeDeViePheromone nombre de tours avant que les pheromones ne disparaissent de la grille
	 */
	public Eclaireuse(int pointsDeVie, int dureeDeVie, int pointsDeSatiete, Case position,
			Grille grille, int quantiteNourritureCorrespondante, int distanceDetection, int dureeDeViePheromone) {
		super(pointsDeVie, dureeDeVie, pointsDeSatiete, position, grille, quantiteNourritureCorrespondante, distanceDetection);
		this.dureeDeViePheromone = dureeDeViePheromone;
	}

	/**
	 * Secrete une pheromone de danger sur la case ou se trouve l'eclaireuse 
	 */
	public void secretionPheromoneDanger(){
		if(this.getPosition().getPheromoneDanger() == null) {
			Pheromone pheromone = new Pheromone(TypePheromone.Danger, this.getPosition(), this.dureeDeViePheromone);
			this.getPosition().setPheromoneDanger(pheromone);
			this.getGrille().ajouterPheromoneDanger(pheromone);
		}
	}
	
	/**
	 * Secrete une pheromone de nourriture sur la case ou se trouve l'eclaireuse 
	 */
	public void secretionPheromoneNourriture(){
		if(this.getPosition().getPheromoneNourriture() == null) {
			Pheromone pheromone = new Pheromone(TypePheromone.Nourriture, this.getPosition(), dureeDeViePheromone);
			this.getPosition().setPheromoneNourriture(pheromone);
			this.getGrille().ajouterPheromoneNourriture(pheromone);
		}
	}
	
	/**
	 * Scanne les alentours et depose une pheromone danger en cas de detection d'un ennemi
	 * et/ou une pheromone nourriture en cas de detection de nourriture. 
	 */
	
	public void scannerAlentours() {
		Set<Case> ZoneAScanner=this.getPosition().alentoursEtendus(this.getDistanceDetection());
		for(Case caseEtudiee:ZoneAScanner) {
			if(caseEtudiee.isFourmiliere() == false) {
				if(caseEtudiee.getStockNourriture()>0) {
					secretionPheromoneNourriture();
				}
				if(caseEtudiee.getOccupants().isEmpty() == false) {
					if(caseEtudiee.getOccupants().toArray()[0] instanceof Ennemi) {
						secretionPheromoneDanger();
					}
				}
			}
		}
	}
	

	public void actionSurFourmiliere() {
		this.setRetourFourmiliere(false);
		if(this.getpointsDeSatiete() < this.getPointsDeSatieteMax()/2 && this.getPosition().getStockNourriture() > 0) {
			this.manger();
		}
		else{
			this.seDeplacerAleatoirement();
		}
	}
	

	public void actionHorsFourmiliere() {
		//les eclaireuses ne peuvent pas deposer de pheromones trop pres de la 
		//fourmiliere pour eviter la congestion aux abords de celle-ci.
		if(this.getPosition().getCoordonnees().calculDistance(this.getGrille().getCoordonneesFourmiliere()) 
				>= this.getDistanceDetection() - 1) {
			scannerAlentours();
		}
		this.determinationSiRetourFourmiliere();
		if(this.isretourFourmiliere()) {
			this.avancerVersDestination();
		}
		else {
			this.eloignementFourmiliere();
		}
	}

	/**
	 * Fonction a utiliser pour faire jouer un tour a la fourmi
	 */
	public void jouerTour() {
		this.miseAJourDestination();
		this.miseAJourEtat();
		if(isVivant()) {
			if(this.getPosition().isFourmiliere()){
				actionSurFourmiliere();
			}
			else{
				actionHorsFourmiliere();
			}
		}
		super.jouerTour();
	}
}
	

