package Terrain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import Animaux.Animal;




public class Case {
	
	/**
	 * Coordonnees de la Case
	 */
	private final Coordonnees Coordonnees;
	
	/**
	 * Renvoie la liste des animaux occupant la case : un seul element dans la liste sauf si la case est la fourmiliere
	 */
	private Set<Animal> occupants;

	/**
	 * Occupabilite de la Case : True si elle est occupable, False sinon
	 */
	private boolean occupable;
	

	/**
	 * Stock de nourriture disponible sur la case
	 */
	private int stockNourriture;
	
	/**
	 * Pheromone danger se trouvant sur la case (il peut ne pas en avoir)
	 */
	private Pheromone pheromoneDanger;
	
	/**
	 * Pheromone danger se trouvant sur la case (il peut ne pas en avoir)
	 */
	private Pheromone pheromoneNourriture;
	
	/**
	 * Dictionnaire des alentours. 
	 * La cle est la position relative (Haut,Bas,Gauche,Droite) et la valeur est l'objet case correspondant.
	 */
	private HashMap <PositionRelative, Case> alentours;
	
	/**
	 * true si la case est la fourmiliere,false sinon
	 */
	private boolean fourmiliere;
	
	/**
	 * Constructor 
	 * @param coordonnees coordonnees de la Case
	 */
	public Case(Coordonnees coordonnees) {
		this.Coordonnees = coordonnees;
		this.stockNourriture = 0;
		this.alentours = new HashMap<PositionRelative,Case>();
		this.occupants = new HashSet<Animal>();
	}

	/**
	 * Getter du stock de nourriture disponible sur la case
	 * @return stockNourriture quantite de nourriture disponible sur la case
	 */
	public int getStockNourriture() {
		return stockNourriture;
	}

	/**
	 * Setter du stock de nourriture disponible sur la case
	 * @param stockNourriture stock de nourriture disponible sur la case
	 */
	public void setStockNourriture(int stockNourriture) {
		this.stockNourriture = stockNourriture;
	}
	
	/**
	 * Getter des coordonnees de la case
	 * @return les coordonnees de la case
	 */
	public Coordonnees getCoordonnees() {
		return Coordonnees;
	}

	/**
	 * Getter des alentours
	 * @return le dictionnaire des alentours de la case. 
	 * La cle est la position relative (Haut,Bas,Gauche,Droite) et la valeur est l'objet case correspondant.
	 */
	public HashMap<PositionRelative, Case> getAlentours() {
		return alentours;
	}

	/**
	 * Setter des alentours
	 * @param alentours cases aux alentours de la case
	 */
	public void setAlentours(HashMap<PositionRelative, Case> alentours) {
		this.alentours = alentours;
	}
	
	/**
	 * Getter de fourmiliere 
	 * @return true si la case est la fourmiliere, false sinon.
	 */
	public boolean isFourmiliere() {
		return fourmiliere;
	}

	/**
	 * Permet de mettre a jour l'attribut fourmiliere
	 * @param fourmiliere true si on souhaite declarer la case comme fourmiliere, false sinon.
	 */
	public void setFourmiliere(boolean fourmiliere) {
		this.fourmiliere = fourmiliere;
	}
	/**
	 * @return boolean true si la case est occupable, false sinon 
	 */
	public boolean isOccupable() {
		return occupable;
	}
	
	/**
	 * Permet de changer l'occupabilite de la case
	 * @param occupable true pour declarer la case occupable, false sinon
	 */
	public void setOccupable(boolean occupable) {
		this.occupable = occupable;
	}
	
	/**
	 * Getter des occupants de la case
	 * @return l'ensemble des animaux occupant la case
	 */
	public Set<Animal> getOccupants() {
		return occupants;
	}
	
	/**
	 * Setter des occupants de la case
	 * @param Occupants les occupants de la case
	 */
	public void setOccupants(Set<Animal> Occupants) {
		this.occupants = Occupants;
	}
	
	/**
	 * Getter de la pheromone de Danger
	 * @return la pheromone de danger presente sur la case
	 */
	public Pheromone getPheromoneDanger() {
		return pheromoneDanger;
	}

	/**
	 * Permet de modifier la pheromone danger presente sur la case 
	 * @param pheromoneDanger la pheromone de danger que l'on souhaite declarer sur la case.
	 */
	public void setPheromoneDanger(Pheromone pheromoneDanger) {
		this.pheromoneDanger = pheromoneDanger;
	}

	/**
	 * Getter de la pheromone de Nourriture 
	 * @return la pheromone de nourriture presente sur la case
	 */
	public Pheromone getPheromoneNourriture() {
		return pheromoneNourriture;
	}

	/**
	 * Permet de modifier la pheromone nourriture presente sur la case 
	 * @param pheromoneNourriture la pheromone de nourriture que l'on souhaite declarer sur la case.
	 */
	public void setPheromoneNourriture(Pheromone pheromoneNourriture) {
		this.pheromoneNourriture = pheromoneNourriture;
	}
	
	/**
	 * Fonction permettant l'ajout d'une case en tant que case adjacente
	 * @param PositionRelative position relative de la case a ajouter (element de l'ennumeration Position relative)
	 * @param Case nouvelle case adjacente
	 */
	public void ajouterAlentours(PositionRelative PositionRelative,Case Case) {
		HashMap <PositionRelative, Case> Alentours = getAlentours();
		Alentours.put(PositionRelative, Case);
		this.setAlentours(Alentours);
	}

	/**
	 * Fait apparaitre de la nourriture sur la case : augmente le stock de nourriture sur la Case
	 * @param quantiteNourriture : quantite de nourrite a rajouter sur la Case
	 */
	public void apparitionNourriture(int quantiteNourriture) {
		this.setStockNourriture(this.getStockNourriture() + quantiteNourriture);
	} 

	/**
	 * Fait disparaitre de la nourriture sur la case : diminue le stock de nourriture sur la Case
	 * @param quantiteNourriture : quantite de nourrite a enlever de la Case
	 */
	public void disparitionNourriture(int quantiteNourriture) {
	this.setStockNourriture(this.getStockNourriture()- quantiteNourriture);
	}

	/**
	 * Permet d'ajouter l animal a la liste des occupants
	 * ATTENTION : ne met pas a jour l'attribut occupable afin de pouvoir etre utilise pour quitter la fourmiliere
	 * @param animal l'animal qui entre sur la case 
	 */
	public void ajouterOccupant(Animal animal) {
		Set<Animal> occupants = this.getOccupants();
		occupants.add(animal);
		this.setOccupants(occupants);
	}
	/**
	 * Permet de supprimer l'animal a la liste des occupants
	 * ATTENTION : ne met pas a jour l'attribut occupable afin de pouvoir etre utilise pour quitter la fourmiliere
	 * @param animal l'animal qui quitte la case 
	 */
	public void supprimerOccupant(Animal animal) {
		Set<Animal> occupants = this.getOccupants();
		occupants.remove(animal);
		this.setOccupants(occupants);
	}
	
	/**
	 * Cette methode permet d'obtenir l'ensemble des cases situees a une moins d'une certaine distance de la case actuelle. 
	 * Les cases sont triees par distance a celle-ci.
	 * @param distance distance maximale a la case 
	 * @return casesInterieures un ensemble ordonnee des cases voisines, tries par distance a la case.
	 */
	public LinkedHashSet<Case> alentoursEtendus(int distance){
		// On va construire la boule topologique fermee associee a la distance en faisant incrementer sa taille (on commence a 0).
		// On va separer les cases les plus en peripherie des autres 
		LinkedHashSet<Case> casesInterieures   = new LinkedHashSet<>();
		LinkedHashSet<Case> couronneExterieure = new LinkedHashSet<>();
		couronneExterieure.add(this);
		//pour chaque incrementation de la valeur de distance...
		for(int rayon = 1; rayon <= distance; rayon ++) {
			//on prepare la couronne nouvelle exterieure qui va se former 
			LinkedHashSet<Case> nouvelleCouronneExterieure = new LinkedHashSet<>();
			// on parcourt les cases de la couronne exterieure existante (celles qui sont en peripherie)
			for(Case caseEtudiee : couronneExterieure){
				//on recupere les cases adjacentes a celles ci ne figurant pas dans les cases interieures 
				//(elles ne peuvent pas faire partie de la couronne exterieure)
				for(Case candidat : caseEtudiee.getAlentours().values()) {
					if(casesInterieures.contains(candidat) == false) {
					// on les ajoute a la nouvelle couronne exterieure
					nouvelleCouronneExterieure.add(candidat);
					}
				}
			//on agrege l'ancienne couronne exterieure aux cases interieures
			casesInterieures.addAll(couronneExterieure);
			//on remplace l'ancienne couronne exterieure par la nouvelle 
			couronneExterieure=nouvelleCouronneExterieure;
			}
		}
	//on ajoute la derniere couronne exterieure
	casesInterieures.addAll(couronneExterieure);
	// on supprime la case courante
	casesInterieures.remove(this);
	return casesInterieures;
	}



}