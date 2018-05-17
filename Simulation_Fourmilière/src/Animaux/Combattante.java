/**
 * Projet Informatique Mai 2018 Simulation Fourmiliere
 * @author Marie Bastien
 * @author Guillaume Leroy 
 */

package Animaux;

import java.util.Iterator;
import java.util.Set;

import Terrain.Case;
import Terrain.Grille;

public class Combattante extends Fourmi implements iAttaquer{

	
	/**
	 * Points d'attaque de la Combattante
	 */
	private final int  pointsAttaque; 
	
	/**
	 * Distance a laquelle la combattante peut attaquer
	 */
	private final int  distanceAttaque; 
	
	/**
	 * Constructor
	 * 
	 * @param dureeDeVie : duree de vie de la Combattante
	 * @param pointsDeVie : points de vie de la Combattante
	 * @param pointsDeSatiete : nombre de tours maximum sans manger avant de mourrir de faim
	 * @param pointsAttaque : point d'attaque de la Combattante
	 * @param distanceattaque : distante d'attaque de la Combattante
	 * @param quantiteNourritureCorrespondante: quantite de nourriture que devient la combattante si elle meurt
	 * que gagnera un animal s'il mange celui-ci a sa mort
	 * @param position objet Case sur lequel se trouve le combattante a la naissance
	 * @param grille Grille sur laquelle se trouve l'animal 
	 * @param distanceDetection distance a laquelle la combattante voit les ennemis et les pheromones
	 */
	public Combattante(int pointsDeVie, int dureeDeVie, int pointsDeSatiete, Case position, Grille grille,
			int quantiteNourritureCorrespondante, int distanceDetection, int pointsAttaque, int distanceattaque) {
		super(pointsDeVie, dureeDeVie, pointsDeSatiete,position, grille, quantiteNourritureCorrespondante, distanceDetection);
		this.pointsAttaque   = pointsAttaque;
		this.distanceAttaque = distanceattaque;
	} 
	

	/**
	 * Getter des points d attaque de la combattante
	 * @return PointsAttaque points d'attaque de la combattante
	 */ 
	public int getPointsAttaque() {
		return pointsAttaque;
	}
	
	/**
	 * Getter de la distante a laquelle la combattante attaque
	 * @return DistanceAttaque distante d attaque de la combattante
	 */
	public int getDistanceAttaque() {
		return distanceAttaque;
	}


	/**
	 * Fonction permettant d'attaquer l'animal passe en argument.
	 */
	@Override
	public void attaquerAnimal(Animal ennemi) {
		ennemi.setpointsDeVie(ennemi.getpointsDeVie() - this.pointsAttaque);
	}

	/**
	 * Detecte et attaque les ennemis qui se trouvent dans son perimetre d'attaque. 
	 * Pour l'instant la combattante n'attaque qu'un ennemi par tour
	 * @return boolean: true si un ennemi a ete attaque, false sinon
	 */
	public boolean attaquerEnnemis() {
		Set<Case> alentours = this.getPosition().alentoursEtendus(distanceAttaque);
		boolean ennemiAttaque = false;
		Iterator<Case> iter  = alentours.iterator();
		while(ennemiAttaque == false && iter.hasNext()) {
			Case caseEtudiee = iter.next();
			if(caseEtudiee.getOccupants().isEmpty() == false) {
				if(caseEtudiee.getOccupants().toArray()[0] instanceof Ennemi) {
					this.attaquerAnimal((Animal) caseEtudiee.getOccupants().toArray()[0]);
				ennemiAttaque = true;
				}
			}
		}
		return ennemiAttaque;
	}
	
	/**
	 * Scanne les alentours a la recherche de pheromone danger. 
	 * Le pheromene detecte le plus proche devient la nouvelle destination
	 * @return boolean true si pheromone detecte, false sinon
	 */
	
	public boolean detecterPheromoneDanger() {
		Set<Case> alentours = this.getPosition().alentoursEtendus(this.getDistanceDetection());
		boolean pheromoneDetectee = false;
		Iterator<Case> iter = alentours.iterator();
		//parcours des alentours
		while(pheromoneDetectee == false && iter.hasNext()) {
			Case caseEtudiee = iter.next();
			//mise a jour de la destination et sortie de boucle au premier pheromone danger trouve
			if(this.getGrille().getListePheromoneDanger().contains(caseEtudiee.getPheromoneDanger())) {
				this.setcoordonneesDestination(caseEtudiee.getCoordonnees());
				pheromoneDetectee=true;
			}
		}
		return pheromoneDetectee;
	}
	
	/**
	 * Scanne les alentours a la recherche d'ennemis. 
	 * L'ennemi detecte le plus proche devient la nouvelle destination
	 * @return boolean true si ennemi detecte detecte, false sinon
	 */
	public boolean rechercherEnnemis() {
		Set<Case> alentours = this.getPosition().alentoursEtendus(this.getDistanceDetection());
		boolean ennemiDetecte = false;
		Iterator<Case> iter = alentours.iterator();
		//scan des alentours
		while(ennemiDetecte == false && iter.hasNext()) {
			Case caseEtudiee = iter.next();
			if(caseEtudiee.getOccupants().isEmpty() == false) {
				//mise a jour de la destination et sortie de boucle au premier ennemi trouve
				if(caseEtudiee.getOccupants().toArray()[0] instanceof Ennemi) {
					this.setcoordonneesDestination(caseEtudiee.getCoordonnees());
					ennemiDetecte=true;
				}
			}
		}
		return ennemiDetecte;
	}
		
	/**
	 * Fonction permettant le scan des alentours 
	 * a la recherche d'ennemis et de pheromene danger (dans cete ordre de priorite).
	 * @return boolean true si un ennemi/une pheromone a ete detecte, false sinon.
	 */
	public boolean scannerZone() {
		boolean ennemiDetecte = this.rechercherEnnemis();
		if(ennemiDetecte == false) {
			boolean pheromoneDangerDetectee = this.detecterPheromoneDanger();
			if(pheromoneDangerDetectee == false) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}
	
	public void actionSurFourmiliere() {
		this.setRetourFourmiliere(false);
		//la fourmi cherche prioritairement a manger
		if(this.getpointsDeSatiete() < this.getPointsDeSatieteMax()/2 && this.getPosition().getStockNourriture()> 0) {
			this.manger();
		}
		else {
			//si ce n'est pas possible de manger priorite a l'attaque
			boolean ennemiAttaque = this.attaquerEnnemis();
				if(ennemiAttaque == false) {
					//si pas possible d'attaquer elle scanne les environs
					boolean actionEffectuee = this.scannerZone();
					if(actionEffectuee) {
						//si la fourmi a vu un ennemi/une pheromone elle avance vers lui/elle
						this.avancerVersDestination();
					}
					else {
						//sinon elle continue son exploration
						this.eloignementFourmiliere();
					}
				}
		}
	}
	
	public void actionHorsFourmiliere() {
		this.determinationSiRetourFourmiliere();
		//la fourmi avance vers la fourmiliere si elle doit y rentrer
		if(this.isretourFourmiliere()) {
			this.avancerVersDestination();
		}
		else {
			//sinon priorite a l'attaque
			boolean ennemiAttaque = this.attaquerEnnemis();
				if(ennemiAttaque == false) {
					//si pas possible d'attaquer elle scanne
					boolean actionEffectuee=this.scannerZone();
					if(actionEffectuee) {
						//si la fourmi a vu un ennemi/une pheromone elle avance vers lui/elle
						this.avancerVersDestination();
					}
					else {
						//sinon elle continue son exploration
						this.eloignementFourmiliere();
					}
				}
		}
	}
	
	/**
	 * Fonction permettant de faire jouer un tour a une combattante
	 */
	public void jouerTour() {
		this.miseAJourDestination();
		this.miseAJourEtat();
		if(isVivant()) {
			if(this.getPosition().isFourmiliere()) {
				this.actionSurFourmiliere();
			}
			else{
				this.actionHorsFourmiliere();
			}
		}
		super.jouerTour();
	}
}
