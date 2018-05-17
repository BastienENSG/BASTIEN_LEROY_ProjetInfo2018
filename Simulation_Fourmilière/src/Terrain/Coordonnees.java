package Terrain;

public class Coordonnees {
	
	/**
	 * Abscisse du couple de Coordonnees
	 */
	private final int abscisse;
	
	/**
	 * Ordonnee du couple de Coordonnees
	 */
	private final int ordonnee;



	/**
	 * Constructor 
	 * @param abscisse Abscisse du couple de Coordonnees
	 * @param ordonnee Ordonnee du couple de Coordonnees
	 */
	public Coordonnees(int abscisse, int ordonnee) {
		this.abscisse = abscisse;
		this.ordonnee = ordonnee;
	}


	/**
	 * Getter de l'abscisse
	 * @return abscisse 
	 */
	public int getAbscisse() {
		return abscisse;
	}

	/**
	 * Getter de l'ordonnee
	 * @return ordonnee
	 */
	public int getOrdonnee() {
		return ordonnee;
	}


	/**
	 * Permet de calculer la distance separant deux coordonnees 
	 * @param coordonnees de la destination
	 * @return int nombre de cases a parcourir pour se rendre a la destination
	 */
	
	public Integer calculDistance(Coordonnees coordonnees) {
		return Math.abs(this.abscisse - coordonnees.abscisse) + Math.abs(this.ordonnee - coordonnees.ordonnee);
	}
	
	/**
	 * Definit l egalite entree coordonnees
	 */
	@Override
	public boolean equals(Object o) {
		if ((o instanceof Coordonnees) 
				&& this.abscisse == ((Coordonnees)o).getAbscisse() 
				&& this.ordonnee ==((Coordonnees)o).getOrdonnee()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Affiche dans la console les coordonees de la case
	 */
	public void afficherCoordonnees() {
		System.out.print( "(" + this.abscisse + ","+ ordonnee +") ");
	}
	

	
	

}
