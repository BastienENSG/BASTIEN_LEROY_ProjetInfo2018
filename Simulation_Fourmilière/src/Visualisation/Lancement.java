package Visualisation;

import java.util.Scanner;

public class Lancement {

public static void main(String[] args) throws InterruptedException {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Jouer avec les parametres par defaut ? si oui taper 1, sinon taper 2 ");
		int mode = scan.nextInt();
		if (mode == 1 ) {
			new Jeu();
		}
		
		else {
		System.out.println("Combien de tours ?");
		int nombreTours = scan.nextInt();
		
		System.out.println("Combien d'obstacles ? Rappel la grille comporte 100 cases");
		int nombreObstacles = scan.nextInt();
		
		System.out.println("Combien de fourmis ? entrer un multiple de 3");
		int nombreFourmis = scan.nextInt();
		
		int maxEnnemis=99-nombreObstacles;
		System.out.println("Combien d'ennemis ? entrer un nombre entre 0 et "+maxEnnemis);
		int nombreEnnemis = scan.nextInt();
		
		System.out.println("Combien de cases avec de la nourriture ? entrer un nombre entre 0 et "+maxEnnemis);
		int nombreCasesNourriture = scan.nextInt();
			
		new  Jeu( nombreTours, 10,  nombreObstacles,  nombreFourmis,  nombreFourmis*10, 
				 nombreEnnemis,  nombreCasesNourriture); 
		}
		
		scan.close();
	}

}
