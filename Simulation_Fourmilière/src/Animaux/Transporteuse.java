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

public class Transporteuse extends Eclaireuse {

	/**
	 * Quantite de nourriture maximale que peut transportee la transporteuse
	 */
	private final int quantiteNourritureTransportable;
	
	/**
	 * Quantite de nourrtiture que transporte la transporteuse
	 */
	private int quantiteNourritureTransportee;
	
	/**
	 * Constructor 
	 * @param pointsDeVie  points de vie de la transporteuse a la naissance
	 * @param dureeDeVie  duree de vie de la transporteuse
	 * @param pointsDeSatiete point de satiete de la transporteuse
	 * @param position case sur laquelle se trouve la transporteuse a la naissance
	 * @param grille  grille sur laquelle evolue la fourmi
	 * @param QuantiteNourritureCorrespondante quantite de nourriture disponible pour l'ennemi si  la transpeuse meurt
	 * @param distanceDetection distance a laquelle la transporteuse voit la nourriture/les pheromones/les ennemis
	 * @param dureeDeViePheromone nombre de tours avant que les pheromones ne disparaissent de la grille
	 * @param quantiteNourritureTransportable quantite de nourriture que la transporteuse peut porter
	 */
	public Transporteuse(int pointsDeVie, int dureeDeVie, int pointsDeSatiete, Case position, Grille grille, 
			int QuantiteNourritureCorrespondante, int distanceDetection, 
			int dureeDeViePheromone, int quantiteNourritureTransportable) {
		super(pointsDeVie, dureeDeVie, pointsDeSatiete, position, grille, 
				QuantiteNourritureCorrespondante, distanceDetection,dureeDeViePheromone);
		this.quantiteNourritureTransportable = quantiteNourritureTransportable;
		this.quantiteNourritureTransportee = 0;
	}

	/**
	 * Getter de la quantite de nourriture que transporte la transporteuse
	 * @return quantiteNourritureTransportee
	 */
	public int getquantiteNourritureTransportee() {
		return quantiteNourritureTransportee;
	}

	/**
	 * Setter de la quantite de nourriture que transporte la transporteuse
	 * @param quantiteNourritureTransportee quantite de nourriture que transporte la Transporteuse
	 */
	public void setquantiteNourritureTransportee(int quantiteNourritureTransportee) {
		this.quantiteNourritureTransportee = quantiteNourritureTransportee;
	}

	/**
	 * Getter de la quantite de nourriture transportable par la transporteuse
	 * @return QuantiteNourritureTransportable
	 */
	public int getQuantiteNourritureTransportable() {
		return quantiteNourritureTransportable;
	}

	/**
	 * Fonction permettant a la transporteuse de prendre de la nourriture se trouvant sa case courante.
	 * Elle prend de la nourriture jusqu'a ce que la quantite de nourriture qu'elle peut transporter soit atteinte 
	 * ou qu'il n'y aie de nourriture sur la case 
	 */
	public void prendreNourriture(){
		int nourriture = this.getPosition().getStockNourriture();
		int nourriturePrelevee = Math.min(nourriture, this.quantiteNourritureTransportable - this.quantiteNourritureTransportee);
		this.quantiteNourritureTransportee += nourriturePrelevee;
		this.getPosition().disparitionNourriture(nourriturePrelevee);
	}
	
	/**
	 * Fonction permettant a la transporteuse de deposer de la nourriturre transportee sur sa case courante
	 */
	public void deposerNourriture (){
		this.getPosition().apparitionNourriture(this.quantiteNourritureTransportee);
		this.quantiteNourritureTransportee = 0;
	}
	
	
	@Override
	public void determinationSiRetourFourmiliere() {
		super.determinationSiRetourFourmiliere();
		//ajout d'une condition supplementaire de retour a la fourmiliere 
		if(quantiteNourritureTransportee >= quantiteNourritureTransportable/2) {
			this.setRetourFourmiliere(true);
			this.setcoordonneesDestination(this.getGrille().getCoordonneesFourmiliere());
		}
	}
	
	/**
	 * Fonction permettant un scan des alentours a la recherche de nourriture. 
	 * La case contenant de la nourriture la plus proche devient la nouvelle destination.
	 * @return true si de la nourriture a ete detectee, false sinon.
	 */
	public boolean rechercherNourriture(){
		Set<Case> ZoneAScanner= this.getPosition().alentoursEtendus(this.getDistanceDetection());
		boolean NourritureTrouvee=false;
		Iterator<Case> iter = ZoneAScanner.iterator();
		while(iter.hasNext() && NourritureTrouvee == false) {
			Case CaseEtudiee = iter.next();
			if(CaseEtudiee.getStockNourriture() >0 && CaseEtudiee.isFourmiliere() == false) {
				this.setcoordonneesDestination(CaseEtudiee.getCoordonnees());
				NourritureTrouvee=true;
			}
		}
		return NourritureTrouvee;
	}
	
	/**
	 * Fonction permettant le scan des alentours de la transporteuse. Met a jour la destination 
	 * en cas de decouverte de nourriture (prioritairement) ou de pheromones nourriture 
	 * (si pas de nourriture) aux environs.
	 * La fonction enclenche egalement la secretion de pheromones de nourriture 
	 * et de danger sous les memes conditions que l'eclaireuse.
	 */
	public void scannerAlentours() {
		//detection ennemis et pose pheromones :uniquement si loin de fourmiiere
		if(this.getPosition().getCoordonnees().calculDistance(this.getGrille().getCoordonneesFourmiliere()) 
				>= this.getDistanceDetection()-1) {
			super.scannerAlentours();
		}
		//recherche de pheromones si pas de nourriture
		if(rechercherNourriture() == false){
			Set<Case> ZoneAScanner = this.getPosition().alentoursEtendus(this.getDistanceDetection());
			boolean pheromoneTrouvee = false;
			Iterator<Case> iter = ZoneAScanner.iterator();
			//parcours des cases
			while(iter.hasNext() && pheromoneTrouvee == false) {
				Case caseEtudiee = iter.next();
				//ajout de la case comme destination si pheromone nourriture dessus
				if(this.getGrille().getListePheromoneNourriture().contains(caseEtudiee.getPheromoneNourriture())) {
					this.setcoordonneesDestination(caseEtudiee.getCoordonnees());
					pheromoneTrouvee=true;
				}
			}
		}
	}
	
	public void actionSurFourmiliere() {
		//si la transporteuse transporte de la nourriture, elle la depose
		this.setRetourFourmiliere(false);
		if(this.quantiteNourritureTransportee>0) {
			this.deposerNourriture();
		}
		//sinon elle mange si elle en a besoin
		else {
			if(this.getpointsDeSatiete() < this.getPointsDeSatieteMax()/2 
					&& this.getPosition().getStockNourriture() > 0) {
				this.manger();
			}
			//s'il ne peut pas manger elle scanne les alentours
			else {
				scannerAlentours();
				//si elle a vu de la nourriture ou une pheromone nourriture elle avance vers elle 
				try {
					this.avancerVersDestination();
				}
				//sinon elle quitte se deplace au hasard
				catch(NullPointerException e) {
					this.seDeplacerAleatoirement();
				}
			}
		}
	}
	
	public void actionHorsFourmiliere() {
		//scan des alentours
		scannerAlentours();
		this.determinationSiRetourFourmiliere();
		//si la fourmi doit retourner a la fourmiliere elle le fait 
		if(this.isretourFourmiliere()) {
			this.avancerVersDestination();
		}
		else{
		//sinon elle prend de la nourriture si elle le peut 
			if(this.getPosition().getStockNourriture() > 0){
				this.prendreNourriture();
			}
			else{
				//si pas de nourriture elle se deplace
				try{
					this.avancerVersDestination();
				}
				catch(NullPointerException e) {
					this.eloignementFourmiliere();
				}
			}
			
		}
	}
	
	
	@Override
	public void jouerTour() {
		this.miseAJourDestination();
		this.miseAJourEtat();
		if(isVivant()) {
			if(this.getPosition().isFourmiliere()){
				this.actionSurFourmiliere();
			}
			else{
				this.actionHorsFourmiliere();
			}
		}
		else{
			this.getPosition().apparitionNourriture(this.quantiteNourritureTransportee);
		}
		this.setdureeDeVie(getdureeDeVie() - 1);
		this.setpointsDeSatiete(this.getpointsDeSatiete() - 1);
	}
	
}
