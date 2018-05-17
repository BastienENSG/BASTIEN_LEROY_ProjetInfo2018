package Visualisation;

import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Animaux.Animal;
import Animaux.Combattante;
import Animaux.Eclaireuse;
import Animaux.Ennemi;
import Animaux.Transporteuse;
import Terrain.Case;
import Terrain.Grille;

public class JPanelGrille extends JPanel{

	
	private static final long serialVersionUID = 1L;

	/**
	 * Grille sur laquelle se deplacer les animaux
	 */
	private Grille grille;
	
	/**
	 * Nombre de case du cote de grille
	 */
	private int tailleCote;

	/**
	 * Dictionnaire des icones 
	 */
	private HashMap<String, ImageIcon> icones;

	/**
	 * Hauteur en pixels
	 */
	private int hauteur;

	/**
	 * Largeur en pixels
	 */
	private int largeur;

	

	/**
	 * Objet de type JPanel permettant de representer une grille et ses occupants  
	 * @param grille objet Grille a representer
	 * @param largeur largeur en pixels
	 * @param hauteur hauteur en pixels
	 */
	public JPanelGrille(Grille grille, int largeur ,int hauteur) {
		super();
		this.icones = new HashMap<String,ImageIcon>();
		this.tailleCote = grille.getTaille();
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.grille  = grille;
		
		//creation et stockage des icones necessaires a l'affichage
		icones.put("fourmiMorte", this.creerIcone("FourmiMorte.PNG"));
		icones.put("eclaireuse", this.creerIcone("Eclaireuse.PNG"));
		icones.put("transporteuse", this.creerIcone("Transporteuse.PNG"));
		icones.put("combattante", this.creerIcone("Combattante.PNG"));
		icones.put("obstacle1", this.creerIcone("Pierre.png"));
		icones.put("vide", this.creerIcone("vide.jpg"));
		icones.put("fourmiliere", this.creerIcone("fourmiliere.PNG"));
		icones.put("pheDanger", this.creerIcone("pheromoneDanger.PNG"));
		icones.put("pheNourriture", this.creerIcone("pheromoneNourriture.PNG"));
		icones.put("nourriture1", this.creerIcone("Nourriture.PNG"));
		icones.put("predateur", this.creerIcone("Araignee.PNG"));
		icones.put("predateurMort", this.creerIcone("AraigneeMorte.PNG"));
		icones.put("transporteuseNourriture",this.creerIcone("Transporteuse avec Nourriture.PNG"));
	
	this.construire();
		
	}
	
	/**
	 * Fonction permettant d'ajouter a un JPanel une icone representant la nature de la case.
	 * Ne marche que pour les cases non occupables (obstacle ou animal sur la case)
	 * @param Case objet case dont on souhaite creer l'icone 
	 */
	public void ajouterIconeCaseNonOccupable(Case Case){
		if(Case.isOccupable() == false){
			//affichage obstacles
			if (Case.getOccupants().isEmpty()) {
				this.add(new JLabel(icones.get("obstacle1")));
			}
			else {
				//affichage ennemis
				if(Case.getOccupants().toArray()[0] instanceof Ennemi) {
					if(((Animal) Case.getOccupants().toArray()[0]).isVivant() == false) {
						this.add(new JLabel(icones.get("predateurMort")));
					}
					else {
					 this.add(new JLabel(icones.get("predateur")));
					}
				}
				else {
					//affichage fourmis mortes
					if(((Animal) Case.getOccupants().toArray()[0]).isVivant() == false) {
						this.add(new JLabel(icones.get("fourmiMorte")));
					}
					//affichage fourmis vivantes
					else {
						if(Case.getOccupants().toArray()[0] instanceof Transporteuse) {
							if(((Transporteuse) Case.getOccupants().toArray()[0]).getquantiteNourritureTransportee() > 0){
								this.add(new JLabel(icones.get("transporteuseNourriture")));
							}
							else{
								this.add(new JLabel(icones.get("transporteuse")));
							}
						}
						else {
							if(Case.getOccupants().toArray()[0] instanceof Eclaireuse) {
								this.add(new JLabel(icones.get("eclaireuse")));
							}
							if(Case.getOccupants().toArray()[0] instanceof Combattante) {
								this.add(new JLabel(icones.get("combattante")));
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Fonction permettant d'ajouter a un JPanel une icone representant la nature de la case.
	 * Ne marche que sur les cases occupables
	 * @param Case objet case dont on souhaite creer l'icone 
	 */
	public void ajouterIconeCaseOccupable(Case Case){
		if(Case.isOccupable()){
			//affichage nourriture
			if(Case.getStockNourriture() > 0) {
				this.add(new JLabel(icones.get("nourriture1")));
			}
			else {
				//affichage pheromome
				if(grille.getListePheromoneDanger().contains(Case.getPheromoneDanger())) {
				this.add(new JLabel(icones.get("pheDanger")));
				}
				else {
					if(grille.getListePheromoneNourriture().contains(Case.getPheromoneNourriture())) {
						this.add(new JLabel(icones.get("pheNourriture")));
					}
					//affichage case vide
					else {
						this.add(new JLabel(icones.get("vide")));
					}
				}
			}
		}
	}
	
	/**
	 * Fonction permettant de creer l'icone d'une image qui pourra s'afficher sur la grille
	 * @param nom de l'image (entre guillemets)
	 * @return l'image en miniature
	 */
	public ImageIcon creerIcone(String nom) {
		return new ImageIcon(new ImageIcon(this.getClass().getResource(nom)).getImage().getScaledInstance(largeur/tailleCote, hauteur/tailleCote, Image.SCALE_DEFAULT));
	}
	
	/**
	 * Fonction permettant d'ajouter a un JPanel une icone representant la nature de la case
	 * @param Case objet case dont on souhaite creer l'icone 
	 */
	public void ajouterIconeCase(Case Case){
		if(Case.isFourmiliere()) {
			this.add(new JLabel(icones.get("fourmiliere")));
		}
		else {	
			if(Case.isOccupable() == false){
				this.ajouterIconeCaseNonOccupable(Case);
			}
			else {
				this.ajouterIconeCaseOccupable(Case);
			}
		}
	}
	/**
	 * Fonction permettant le remplissage duJPanel avec les icones correspondant a la grille
	 */
	public void construire(){
		setLayout(new GridLayout(tailleCote,tailleCote));
		for(int i = 0;i < tailleCote * tailleCote; i++){
			int abscisse = i/tailleCote + 1;
			int ordonnee = i%tailleCote + 1;
			Case Case = grille.getCase(abscisse, ordonnee);
			ajouterIconeCase(Case);
		}
	}
}


