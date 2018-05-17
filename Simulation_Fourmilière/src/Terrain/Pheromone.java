package Terrain;

// enumeration

public class Pheromone {
	
	/**
	 * Type de la pheromone : Danger ou Nourriture 
	 */
	private final TypePheromone type;
	
	/**
	 * Duree de vie Restante de la pheromone : nombre de tour qu'il lui reste avant de disparaitre
	 */
	private int dureeDeVieRestante;
	
	/**
	 * Case sur laquelle se trouve la pheromone
	 */
	private final Case position;
	
	/**
	 * Constructor
	 * @param type type de pheromone
	 * @param casePheromone case ou se trouve la pheromone
	 * @param dureeDeVie duree de vie de la pheromone
	 */
	public Pheromone(TypePheromone type, Case casePheromone, int dureeDeVie) {
		this.type = type;
		this.position = casePheromone;
		this.dureeDeVieRestante = dureeDeVie;
	}
	
	/**
	 * Getter du type de pheromone (Nourriture ou Danger)
	 * @return le type de la pheromone
	 */
	public TypePheromone getType() {
		return type;
	}
	
	/**
	 * Getter de la position de la pheromone
	 * @return l'objet Case sur lequel est pose la pheromone
	 */
	public Case getposition() {
		return position;
	}

	/**
	 * Getter de la duree de Vie Restante de la pheromone
	 * @return le niombre de tours avant la disparition de la pheromone
	 */
	public int getDureeDeVieRestante() {
		return dureeDeVieRestante;
	}

	/**
	 * Fait diminuer de 1 la duree de vie du pheromone
	 */
	public void decrementerDureeDeVie() {
		dureeDeVieRestante -= 1;
	}
	

	
	
	

	
	
}
