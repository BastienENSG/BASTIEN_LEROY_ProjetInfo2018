/**
 * Projet Informatique Mai 2018 Simulation Fourmiliere
 * @author Marie Bastien
 * @author Guillaume Leroy 
 */

package Terrain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import Animaux.Combattante;
import Animaux.Eclaireuse;
import Animaux.Ennemi;
import Animaux.Transporteuse;
import Terrain.PositionRelative;

public class Grille {
	
	/**
	 * Dictionnaire dont les cles sont des coordonnees et les valeurs sont les objets case correspondants
	 */
	private final HashMap<Coordonnees,Case> grilleCases;
	
	/**
	 * Longeur du cote du terrain
	 */
	private final int taille;
	
	/**
	 * Position de la fourmiliere
	 */
	private final Coordonnees coordonneesFourmiliere;
	
	/**
	 * Liste des Ennemis presents sur la grille
	 */
	private List<Ennemi> listeEnnemis;
	
	/**
	 * Liste des Eclaireuses presentes sur la grille 
	 */
	private List<Eclaireuse> listeEclaireuses;
	
	/**
	 * Liste des Transporteuses presentes sur la grille 
	 */
	private List<Transporteuse> listeTransporteuses;
	
	/**
	 * Liste des Combattantes presentes sur la grille 
	 */
	private List<Combattante> listeCombattantes;
	
	/**
	 * Liste des coordonnees des obstacles presents sur la grille 
	 */
	private List<Coordonnees> listeObstacles;
	
	/**
	 * Liste des pheromones nourriture sur la grille  
	 */
	private List<Pheromone> listePheromoneNourriture;
	
	/**
	 * Liste des pheromones danger sur la grille
	 */
	private List<Pheromone> listePheromoneDanger;
	
	/**
	 * Nombre de cases occupables, fourmiliere exclue
	 */
	private int nombreDeCasesLibres;
	
	/**
	 * Constructeur de la grille 
	 * L'attribut GrilleCases est une Hashmap dont les cles sont des coordonnees et les valeurs sont des cases.
	 * La grille est carree
	 * Ne contient aucun obstacle 
	 * Fourmiliere placee aleatoirement
	 * L'origine des coordonnes est prise en haut a gauche 
	 * Les coordonnees commencent a 1
	 * @param taille: cote de la grille (en nombre de cases) 
	 */
	public Grille(int taille) {
		// on initialise les listes des animaux, obstacles et pheromones
		this.listeCombattantes = new ArrayList<>();
		this.listeEclaireuses  = new ArrayList<>();
		this.listeTransporteuses = new ArrayList<>();
		this.listeEnnemis = new ArrayList<>();
		this.listeObstacles = new ArrayList<>();
		this.listePheromoneDanger = new ArrayList<>();
		this.listePheromoneNourriture = new ArrayList<>();
		
		// on initialise la grille en fonction de la taille donnee
		this.taille = taille;
		this.nombreDeCasesLibres = taille*taille - 1;
		//on recupere une grille de cases occupables
		this.grilleCases=this.grilleSansFourmiliere(taille);
		
		//ajout de la fourmiliere
		Random ran = new Random();
		Object[] listeCoord = grilleCases.keySet().toArray();
		int index = ran.nextInt(listeCoord.length);
		this.coordonneesFourmiliere = (Coordonnees) listeCoord[index];
		this.getFourmiliere().setFourmiliere(true);
		
	}
	
	/**
	 * Constructeur de la grille 
	 * L'attribut GrilleCases est une Hashmap dont les cles sont des coordonnees et les valeurs sont des cases.
	 * La grille est carree
	 * Contient un ou plusieurs obstacles et une fourmiliere placee aleatoirement
	 * L origine des coordonnes est prise en haut a gauche 
	 * Les coordonnes commencent a 1
	 * @param taille cote de la grille (en nombre de cases) 
	 * @param nombreObstacles nombre de cases sur lesquelles les animaux ne peuvent se deplacer
	 */
	public Grille(int taille, int nombreObstacles) {
		//on genere la map aleatoire
		this(taille);
		//on place les obstacles.
		placerObstaclesAleatoirement(nombreObstacles);
	}
	
	/**
	 * Constructeur de la grille 
	 * L'attribut GrilleCases est une Hashmap dont les cles sont des coordonnees et les valeurs sont des cases.
	 * La grille est carree
	 * Ne contient aucun obstacle 
	 * L'origine des coordonnes est prise en haut a gauche 
	 * Les coordonnees commencent a 1
	 * @param taille cote de la grille (en nombre de cases) 
	 * @param CoordonneesFourmiliere Coordonnees de la fourmiliere
	 */
	public Grille(int taille,Coordonnees CoordonneesFourmiliere) {
		this.listeCombattantes = new ArrayList<>();
		this.listeEclaireuses  = new ArrayList<>();
		this.listeTransporteuses = new ArrayList<>();
		this.listeEnnemis   = new ArrayList<>();
		this.listeObstacles = new ArrayList<>();
		this.listePheromoneDanger = new ArrayList<>();
		this.taille = taille;
		this.nombreDeCasesLibres=taille*taille-1;
		
		//on recupere une grille de cases occupables
		this.grilleCases = this.grilleSansFourmiliere(taille);
		//on place la fourmiliere
		this.coordonneesFourmiliere = CoordonneesFourmiliere;
		this.grilleCases.get(CoordonneesFourmiliere).setFourmiliere(true);
	}
	
	/**
	 * Constructeur de la grille 
	 * L'attribut GrilleCases est une Hashmap dont les cles sont des coordonnees et les valeurs sont des cases.
	 * La grille est carree
	 * Contient un ou plusieurs obstacles places aleatoirement
	 * L'origine des coordonnees est prise en haut a gauche 
	 * Les coordonnees commencent a 1
	 * @param taille: cote de la grille (en nombre de cases) 
	 * @param coordonneesFourmiliere: Coordonnes de la fourmiliere
	 * @param nombreObstacles nombre de cases sur lesquelles les animaux ne peuvent pas se deplacer
	 */
	public Grille(int taille, Coordonnees coordonneesFourmiliere, int nombreObstacles) {
		//on genere la mapp
		this(taille,coordonneesFourmiliere);
		//on place les obstacles.
		placerObstaclesAleatoirement(nombreObstacles);
	}
	
	/**
	 * Genere une Hashmap dont les cles sont des coordonnees et les valeurs sont  des cases.
	 * Ne contient aucun obstacle ni fourmiliere, uniquement des cases occupables
	 * La grille est carree
	 * L'origine des coordonnes est prise en bas a gauche 
	 * Les coordonnes commencent a 1
	 * On n'utilisera cette fonction que dans les constructeurs d'ou son statut prive
	 * @param taille cote de la grille (en nombre de cases)
	 */
	private HashMap<Coordonnees,Case> grilleSansFourmiliere(int taille) {
		//choix d'une linkedHashMap pour faciliter la recuperation des cases adjacentes
		HashMap<Coordonnees,Case> grilleSansFourmiliere = new HashMap<Coordonnees,Case>();
		//parcours de la grille
		for(int ordonnee = 1; ordonnee <= taille; ordonnee ++){
			for(int abscisse = 1; abscisse <= taille; abscisse ++){
				//creation de l'objet coordonnees et de l'objet case correspondant
				Coordonnees nouvellesCoordonnees = new Coordonnees(abscisse,ordonnee);
				Case nouvelleCase = new Case(nouvellesCoordonnees);
				nouvelleCase.setOccupable(true);
				// On remplit les voisinages
				Coordonnees coordAGauche = new Coordonnees(abscisse - 1,ordonnee);
				Coordonnees coordEnBas = new Coordonnees(abscisse,ordonnee - 1);
				for(Entry<Coordonnees,Case> entry:grilleSansFourmiliere.entrySet()) {
					// si on trouve une case a gauche...
					if(entry.getKey().equals(coordAGauche)) {
						//...on la recupere on l'ajoute comme case a gauche et on definit dans la foulee la case a droite de celle-ci
						Case caseAGauche = entry.getValue();
						nouvelleCase.ajouterAlentours(PositionRelative.Gauche, caseAGauche);
						caseAGauche.ajouterAlentours(PositionRelative.Droite, nouvelleCase);
					}
					//idem a la verticale
					if(entry.getKey().equals(coordEnBas)) {
						Case caseEnBas = entry.getValue();
						nouvelleCase.ajouterAlentours(PositionRelative.Bas, caseEnBas);
						caseEnBas.ajouterAlentours(PositionRelative.Haut, nouvelleCase);
					}
				}
				//on ajoute la case a la HashMap
				nouvelleCase.setFourmiliere(false);
				grilleSansFourmiliere.put(nouvellesCoordonnees, nouvelleCase);
			}
		}
		return grilleSansFourmiliere;
	}
		

	/**
	 * Getter  de grilleCases
	 * @return GrilleCases Dictionnaire de la grille
	 */
	public HashMap<Coordonnees, Case> getGrilleCases() {
		return grilleCases;
	}

	/**
	 * Getter de la taille de la grille
	 * @return la longueur du cote du terrain en nombre de cases
	 */
	public int getTaille() {
		return taille;
	}

	/**
	 * Getter des coordonnees de la fourmiliere
	 * @return les coordonnes de la fourmiliere
	 */
	public Coordonnees getCoordonneesFourmiliere() {
		return coordonneesFourmiliere;
	}

	/**
	 * Getter de la liste des Ennemis
	 * @return la liste des ennemis presents sur le terrain
	 */
	public List<Ennemi> getListeEnnemis() {
		return listeEnnemis;
	}

	/**
	 * Setter de la liste des ennemis
	 * @param listeEnnemis la nouvelle liste des ennemis
	 */
	public void setListeEnnemis(List<Ennemi> listeEnnemis) {
		this.listeEnnemis = listeEnnemis;
	}

	/**
	 * Getter de la liste des eclaireuses
	 * @return listeEclaireuses la liste des eclaireuses presentes sur le terrain
	 */
	public List<Eclaireuse> getListeEclaireuses() {
		return listeEclaireuses;
	}

	/**
	 * Setter de la liste des eclaireuses
	 * @param listeEclaireuses la liste des eclaireuses presentes sur le terrain
	 */
	public void setListeEclaireuses(List<Eclaireuse> listeEclaireuses) {
		this.listeEclaireuses = listeEclaireuses;
	}

	/**
	 * Getter de la liste des transporteuses
	 * @return la liste des transporteuses presentes sur le terrain
	 */
	public List<Transporteuse> getListeTransporteuses() {
		return listeTransporteuses;
	}

	/**
	 * Setter de la liste des transporteuses
	 * @param listeTransporteuses la liste des transporteuses presentes sur le terrain
	 */
	public void setListeTransporteuses(List<Transporteuse> listeTransporteuses) {
		this.listeTransporteuses = listeTransporteuses;
	}

	/**
	 * Getter de la liste des combattantes
	 * @return listeCombattantes la liste des combattantes presentes sur le terrain
	 */
	public List<Combattante> getListeCombattantes() {
		return listeCombattantes;
	}

	/**
	 * Setter de la liste des combattantes
	 * @param listeCombattantes la liste des combattantes presentes sur le terrain
	 */
	public void setListeCombattantes(List<Combattante> listeCombattantes) {
		this.listeCombattantes = listeCombattantes;
	}

	/**
	 * Getter de la liste des obstacles 
	 * @return listeObstacles la liste des obstacles sur le terrain
	 */
	public List<Coordonnees> getListeObstacles() {
		return listeObstacles;
	}

	/**
	 * Getter du nombre de cases libres sur la grille
	 * @return nombreDeCasesLibres le nombre de cases libres(ni obstacle ni fourmiliere)
	 */
	public int getNombreDeCasesLibres() {
		return nombreDeCasesLibres;
	}

	/**
	 * Getter de la liste des pheromones nourriture actives
	 * @return listePheromoneNourriture la liste des pheromones nourriture actives sur le terrain
	 */
	public List<Pheromone> getListePheromoneNourriture() {
		return listePheromoneNourriture;
	}
	
	/**
	 * Getter de la liste des pheromones danger actives
	 * @return listePheromoneDanger la liste des pheromones danger actives sur le terrain
	 */
	public List<Pheromone> getListePheromoneDanger() {
		return listePheromoneDanger;
	}

	/**
	 * Permet de recuperer la case correspondante a la fourmiliere
	 * @return la case correspondante a la fourmiliere
	 */
	public Case getFourmiliere() {
		return this.grilleCases.get(coordonneesFourmiliere);
	}
	
	/**
	 * Permet l'ajout des coordones d'une case a la  liste des coordonnes des obstacles
	 * @param coordObstacle coordonnees de l'obstacle a placer
	 */
	public void ajouterObstacle(Coordonnees coordObstacle) {
		if(coordObstacle.equals(this.coordonneesFourmiliere) == false && this.nombreDeCasesLibres > 0) {
			this.listeObstacles.add(coordObstacle);
			this.nombreDeCasesLibres -= 1;
		}
	}
	
	/**
	 * Permet l'ajout d'une pheromone danger a la liste des pheromones danger actives
	 * @param pheromone pheromone de danger a ajouter a la liste des pheromones danger
	 */
	public void ajouterPheromoneDanger(Pheromone pheromone) {
		this.listePheromoneDanger.add(pheromone);
	}
	
	
	/**
	 * Permet l'ajout d'une pheromone nourriture a la liste des pheromones nourriture actives
	 * @param pheromone pheromone de nourriture a ajouter a la liste des pheromones danger
	 */
	public void ajouterPheromoneNourriture(Pheromone pheromone) {
		this.listePheromoneNourriture.add(pheromone);
	}
	
	
	/**
	 * Dans la liste des pheromones de danger, la fonction décremente la duree de vie des pheromones danger actives 
	 * et supprime celles dont la duree de vie est nulle
	 */
	public void majPheromonesDanger() {
		Iterator<Pheromone> iter = this.listePheromoneDanger.iterator();
		while(iter.hasNext()) {
			Pheromone pheromone = iter.next();
			if(pheromone.getDureeDeVieRestante()>0) {
				pheromone.decrementerDureeDeVie();
			}
			else {
				pheromone.getposition().setPheromoneDanger(null);
				iter.remove();
			}
		}
	}
	

	/**
	 * Dans la liste des pheromones de nourriture, la fonction décremente la duree de vie des pheromones nourriture 
	 * actives et supprime celles dont la duree de vie est nulle
	 */
	public void majPheromonesNourriture() {
		Iterator<Pheromone> iter = this.listePheromoneNourriture.iterator();
		while(iter.hasNext()) {
			Pheromone pheromone = iter.next();
			if(pheromone.getDureeDeVieRestante() > 0) {
				pheromone.decrementerDureeDeVie();
			}
			else {
				pheromone.getposition().setPheromoneNourriture(null);
				iter.remove();
			}
		}
	}
	
	/**
	 * Permet de placer des obstacles sur la grille aleatoirement 
	 * @param nombreObstacles nombre d'obstacles que que l'utilisateur souhaite placer 
	 */
	public void placerObstaclesAleatoirement(int nombreObstacles) {
		int nombreObstaclesPlaces = 0;
		if (this.nombreDeCasesLibres >= nombreObstacles) {
			while(nombreObstaclesPlaces < nombreObstacles) {
				
				//on recupere des cases aleatoirement
				Object[] listeCoord = grilleCases.keySet().toArray();
				Random ran1 = new Random();
				int index   = ran1.nextInt(listeCoord.length);
				Coordonnees coordonneesAleatoires = (Coordonnees)listeCoord[index];
				Case CaseAleatoire = grilleCases.get(coordonneesAleatoires);
				
				//s'il ne s'agit ni de la fourmiliere ni d'un obstacle precedemment place on declare la case innocupable
				if(CaseAleatoire.isFourmiliere() == false && CaseAleatoire.isOccupable() == true){ 
					CaseAleatoire.setOccupable(false);
					nombreObstaclesPlaces += 1;
					this.ajouterObstacle(coordonneesAleatoires);
					this.nombreDeCasesLibres -= 1;
				}	
			}
		}
	}
	
	
	
	/**
	 * Fonction qui retourne l'objet case situe aux coordonnees demandees
	 * @param abcsisse abscisse de la case ou se trouve l'obstacle
	 * @param ordonnee ordonnee de la case ou se trouve l'obstacle
	 * @return objet case dont les coordonnes sont (abcsisse,ordonnee)
	 */
	public Case getCase(int abcsisse, int ordonnee) {
		Coordonnees coordCase = new Coordonnees (abcsisse, ordonnee);
		Coordonnees coordObjectif = null;
		//parcours des coordonnes des cases de la grille
		for(Coordonnees coord:this.getGrilleCases().keySet()) {
			//selection des coordonnes recherchees
			if(coord.equals(coordCase)){
				coordObjectif = coord;
			}
		}
		//on renvoie la case demandee
		return this.grilleCases.get(coordObjectif);
	}
	
	/**
	 * Permet la selection d'une case au hasard
	 * @return un objet Case de la grille pris au hasard
	 */
	private Case caseAleatoire() {
		Random ran = new Random();
		return (Case)this.grilleCases.values().toArray()[ran.nextInt(this.taille*this.taille) ];
	}
	
	/**
	 * Permet d'ajouter de la nourriture sur une case depourvue de nourriture prise aleatoirement.
	 * La case selectionnee n'est jamais un obstacle ou la fourmiliere.
	 * @param quantiteNourriture quantite de nourriture a deposer sur la case
	 */
	public void ajouterNourritureAleatoirement(int quantiteNourriture) {
		boolean nourriturePlacee = false;
		while(nourriturePlacee  == false && this.getNombreDeCasesLibres() > 0){
			//recuperation de case aleatoire
			Case caseAleatoire   = caseAleatoire();
			if(this.listeObstacles.contains(caseAleatoire.getCoordonnees()) == false 
			&& caseAleatoire.getStockNourriture() == 0 && caseAleatoire.isFourmiliere() == false){
				//ajout de la nourriture
				caseAleatoire.apparitionNourriture(quantiteNourriture);
				nourriturePlacee = true;
			}
		}
	}
	
	/**
	 * Permet d'ajouter un ennemi sur une case aleatoire
	 * @param pointsDeVie points de vie de l'ennemi
	 * @param dureeDeVie duree de vie de l'ennemi
	 * @param pointsDeSatiete points de satiete (maximum) de l'ennemi
	 * @param QuantiteNourritureCorrespondante quantite de nourriture que devient l'ennemi s'il meurt
	 * @param distanceDetection distance a laquelle l'ennemi voit les fourmis
	 * @param pointsAttaque points d'attaque de l'ennemi
	 */
	public void creerEnnemi(int pointsDeVie,int dureeDeVie,int pointsDeSatiete,
			int QuantiteNourritureCorrespondante, int  distanceDetection,int pointsAttaque){
		boolean ennemiPlace = false;
		if(this.nombreDeCasesLibres > 0) {
			while(ennemiPlace == false) {
				//recuperation d'une case aleatoire : 
				//si elle est occupable sans etre la fourmiliere on instancie l'ennemi dessus
				Case caseAleatoire = this.caseAleatoire();
				if(caseAleatoire.isOccupable() && caseAleatoire.isFourmiliere() == false) {
					List<Ennemi> listeEnnemis = this.getListeEnnemis();
					listeEnnemis.add(new Ennemi(pointsDeVie, dureeDeVie, pointsDeSatiete,
							caseAleatoire, QuantiteNourritureCorrespondante, pointsAttaque, distanceDetection));	
					this.setListeEnnemis(listeEnnemis);
					caseAleatoire.setOccupable(false);
					ennemiPlace = true;
					this.nombreDeCasesLibres -= 1;
				}
			}
		}
	}
	
	/**
	 * Permet d'ajouter une eclaireuse sur la fourmiliere
	 * @param pointsDeVie points de vie de l'eclaireuse a la naissance
	 * @param dureeDeVie duree de vie de l'eclaireuse
	 * @param pointsDeSatiete points de satiete max de l'eclaireuse
	 * @param quantiteNourritureCorrespondante nombre de points de satiete recupere par l'ennemi lorsqu'il mange la fourmi 
	 * @param distanceDetection distance a laquelle l'eclaireuse voit les ennemis et la nourriture
	 * @param dureeDeViePheromone duree de la vie de la pheromone
	 */
	public void creerEclaireuse(int pointsDeVie,int dureeDeVie,int pointsDeSatiete,
			int quantiteNourritureCorrespondante,int distanceDetection,int dureeDeViePheromone) {
		Case Fourmiliere=this.grilleCases.get(coordonneesFourmiliere);
		this.getListeEclaireuses().add(new Eclaireuse(pointsDeVie, dureeDeVie, pointsDeSatiete,Fourmiliere, 
				this, quantiteNourritureCorrespondante, distanceDetection,dureeDeViePheromone));	
	}
	
	/**
	 * Permet d'ajouter une transporteuse sur la fourmiliere
	 * @param pointsDeVie points de vie de la fourmi la naissance
	 * @param dureeDeVie nombre de tours maximal que peut jouer de la fourmi
	 * @param pointsDeSatiete points de satiete max de la fourmi
	 * @param quantiteNourritureCorrespondante nombre de points de satiete recupere par l'ennemi s'il mange la fourmi 
	 * @param distanceDetection distance a laquelle l'eclaireuse voit les ennemis et la nourriture et les pheromones nourriture
	 * @param dureeDeViePheromone duree de vie de la pheromone
	 * @param QuantiteNourritureTransportable: quantite max de nourriture que la transporteuse peut transporter
	 */
	public void creerTransporteuse(int pointsDeVie,int dureeDeVie,int pointsDeSatiete,
			int quantiteNourritureCorrespondante, int distanceDetection, 
			int dureeDeViePheromone, int QuantiteNourritureTransportable) {
		Case Fourmiliere = this.grilleCases.get(coordonneesFourmiliere);
		this.getListeTransporteuses().add(new Transporteuse(pointsDeVie, dureeDeVie, pointsDeSatiete, Fourmiliere, 
				this, quantiteNourritureCorrespondante, distanceDetection, 
				dureeDeViePheromone, QuantiteNourritureTransportable));
	}
	
	/**
	 * Permet d'ajouter une combattante sur la fourmiliere
	 * @param pointsDeVie points de vie de la fourmi la naissance
	 * @param dureeDeVie nombre de tours maximal que puet jouer de la fourmi
	 * @param pointsDeSatiete points de satiete max de la fourmi
	 * @param quantiteNourritureCorrespondante nombre de points de satiete recupere par l'ennemi s'il mange la fourmi
	 * @param distanceDetection distance de detection de la combattante
	 * @param pointsAttaque nombre de points de vie retires a l'ennemi au maximum lorsque la combattante l'attaque
	 * @param distanceattaque distance a laquelle la fourmi peut attaquer un ennemi
	 */
	public void creerCombattante(int pointsDeVie, int dureeDeVie, int pointsDeSatiete,
			int quantiteNourritureCorrespondante, int distanceDetection, int pointsAttaque, int distanceattaque) {
		Case Fourmiliere = this.grilleCases.get(coordonneesFourmiliere);
		this.getListeCombattantes().add(new Combattante(pointsDeVie, dureeDeVie, pointsDeSatiete, Fourmiliere,
				this, quantiteNourritureCorrespondante, distanceDetection, pointsAttaque, distanceattaque));
	}
	
	
}