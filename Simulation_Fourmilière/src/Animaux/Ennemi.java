/**
 * Projet Informatique Mai 2018 Simulation Fourmiliere
 * @author Marie Bastien
 * @author Guillaume Leroy 
 */

package Animaux;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Terrain.Case;

public class Ennemi extends Animal implements iAttaquer{
	
	/**
	 * Nombre de points de vie perdu par une fourmi lorsque l'ennemi l'attaque
	 */
	private int pointsAttaque; 
	
	
	/**
	 * Constructeur d'un ennemi.
	 * @param pointsDeVie nombre de points de vie a la naissance
	 * @param dureeDeVie nombre maximal de tours avant la mort
	 * @param pointsDeSatiete nombre de tours maximum sans manger avant de mourrir de faim
	 * @param position case sur laquelle se trouve l'ennemi
	 * @param quantiteNourritureCorrespondante quantite de nourriture pour fourmi deversee sur la case a la mort de l'ennemi 
	 * @param distanceDetection distance a laquelle l'ennemi voit les fourmis
	 * @param pointsAttaque nombre de poinst de vie perdu par une fourmi lorsque l'ennemi l'attaque
	 */
	public Ennemi(int pointsDeVie, int dureeDeVie, int pointsDeSatiete, Case position,
			int quantiteNourritureCorrespondante, int distanceDetection,int pointsAttaque) {
		super(pointsDeVie, dureeDeVie, pointsDeSatiete, position, quantiteNourritureCorrespondante,distanceDetection);
		this.pointsAttaque = pointsAttaque;
	}

	/**
	 * Getter des points d attaque de l'ennemi
	 * @return PointsAttaque
	 */
	public int getPoints_Attaque() {
		return pointsAttaque;
	}

	/**
	 * Setter des points d'attaque de l'ennemi 
	 * @param pointsAttaque nombre de points d'attaque
	 */
	public void setpointsAttaque(int pointsAttaque) {
		this.pointsAttaque = pointsAttaque;
	}

	/**
	 * Methode permettant d'attaquer une fourmi
	 * @param fourmi fourmi a attaquer
	 */
	@Override
	public void attaquerAnimal(Animal fourmi) {
		fourmi.setpointsDeVie(fourmi.getpointsDeVie()-this.pointsAttaque);
	}
	
	/**
	 * Methode permettant de manger une fourmi morte
	 * @param fourmi fourmi morte a manger
	 */
	public void manger(Fourmi fourmi) {
		//on nourrit l'ennemi jusqu'a ce qu'il n'est plus faim
		int satiete = this.getpointsDeSatiete();
		this.setpointsDeSatiete(Math.min(fourmi.getQuantiteNourritureCorrespondante()+ satiete, this.getPointsDeSatieteMax()));
	}
	
	/**
	 * Cette methode renvoie une HashMap des fourmis situees aux alentours de l'ennemi
	 * @return fourmisAlentours dictionnaire dont les cles sont vivantes et mortes et les valeurs sont des listes d'objets Fourmi
	 * et les valeurs les listes des fourmis vivantes et mortes aux alentours
	 */
	public HashMap<String,List<Fourmi>> scannerGrille(){
		//on commence par recuperer la liste des fourmis des alentours (vivantes et mortes) hors fourmiliere
		HashMap<String,List<Fourmi>> fourmisAlentours = new HashMap<String,List<Fourmi>>();
		List<Fourmi> fourmisVivantes = new ArrayList<>();
		List<Fourmi> fourmisMortes = new ArrayList<>();
		//parcours des alentours
		for (Case caseAdjacente: this.getPosition().getAlentours().values()) {
			//interdiction d'attaquer la fourmiliere
			if(caseAdjacente.getOccupants().isEmpty() == false && caseAdjacente.isFourmiliere() == false){
				Animal Occupant=(Animal) caseAdjacente.getOccupants().toArray()[0];
				//on references les occupants
				if(Occupant instanceof Fourmi) {
					if(Occupant.isVivant()) {
						fourmisVivantes.add((Fourmi) Occupant);
					}
					else{
						fourmisMortes.add((Fourmi) Occupant);
					}
				}
			}
		}
		//permet d'obtenir les fourmis vivantes avec la cle "vivantes"
		fourmisAlentours.put("vivantes", fourmisVivantes);
		//permet d'obtenir les fourmis mortes avec la cle "mortes"
		fourmisAlentours.put("mortes", fourmisMortes);
		return fourmisAlentours;
	}
	
	/**
	 * Scanne les alentours a la recherche de fourmis eclaireuses ou transporteuses
	 * L'ennemi detecte le plus proche devient la nouvelle destination
	 * @return boolean true si une fourmie non combattant a ete detectee, false sinon
	 */
	public boolean ScannerZone() {
		Set<Case> alentours = this.getPosition().alentoursEtendus(this.getDistanceDetection());
		boolean fourmiDetecte = false;
		Iterator<Case> iter = alentours.iterator();
		//scan des alentours
		while(fourmiDetecte == false && iter.hasNext()) {
			Case caseEtudiee = iter.next();
			if(caseEtudiee.getOccupants().isEmpty() == false) {
				//mise a jour de la destination et sortie de boucle au premier ennemi trouve
				if(caseEtudiee.getOccupants().toArray()[0] instanceof Eclaireuse) {
					this.setcoordonneesDestination(caseEtudiee.getCoordonnees());
					fourmiDetecte=true;
				}
			}
		}
		return fourmiDetecte;
	}
		
	/**
	 * Fonction permettant de faire agir l'ennemi, 
	 * ie par ordre de priorite de le faire manger des fourmis mortes, d'attaquer des fourmis vivantes,
	 * ou de prendre en chasse une fourmi non combattante
	 * @return boolean : true si l'ennemi a attaque , mange des fourmis, false sinon
	 */
	public boolean agir() {
		boolean action = false;
		//scan des fourmis se trouvant aux alentours
		HashMap<String,List<Fourmi>> fourmisAlentours = this.scannerGrille();
		//l'ennemi mange prioritairement les fourmis mortes s'il y en a autour de lui 
		if(fourmisAlentours.get("mortes").isEmpty() == false) {
			action = true;
			for(Fourmi fourmi:fourmisAlentours.get("mortes")) {
				this.manger(fourmi);
			}
		}
		//sinon il attaque les vivantes (s'il y en a)
		else if(fourmisAlentours.get("vivantes").isEmpty() == false) {
				action = true;
				for(Fourmi fourmi:fourmisAlentours.get("vivantes")) {
					this.attaquerAnimal(fourmi);
				}
		}
		else{
			//sinon il prend en chasse des fourmis non combattantes s'il le peut
			boolean fourmiDetecte=ScannerZone();
			if(fourmiDetecte) {
				action=true;
				this.avancerVersDestination();
			}
		}
		//on renvoie le boleen indiquant si une action a eu lieu
		return(action);
	}
		
	
	/**
	 * Methode permettant de faire jouer un tour de simulation a l'objet ennemi 
	 */
	public void jouerTour() {
		this.miseAJourEtat();
		this.miseAJourDestination();
		if(isVivant()) {
			boolean action = agir();
			// soit l ennemi mange/attaque, soit il prend en chasse une fourmi, soit il se deplace aleatoirement
			if(action == false) {
				this.seDeplacerAleatoirement();
			}
			super.jouerTour();
		}
	}

}
