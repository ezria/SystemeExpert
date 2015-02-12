package Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import Objet.Operateur;
import Objet.Regle;
import Objet.Variable;
import ToolBox.LectureFichierXML;
import ToolBox.MoteurInference;


public class Main {

	public static void main(String[] args) {
		System.out.println("Bienvenue dans l'application Sypert developé par Corentin, Arthur, Aymeric, Jules dans le cadre d'un projet de complexité algorithmique à l'école ESAIP à st Barthélémy d'Anjou d'ingénieur.");
		System.out.println("Ce programme permet de répondre au problème de recherche en profondeur dans un système expert.");
		
		JFileChooser dialogue=null;
		try {
			dialogue =new JFileChooser(new File(".").getCanonicalFile());
        } catch(IOException e) {
        	System.err.println("impossible de lire le fichier, ouverture de la boite de dialogue sur le répertoire HOME");
        	dialogue =new JFileChooser();
        }
		dialogue.setDialogTitle("Choisissez votre fichier XML des REGLES.");
		dialogue.showOpenDialog(null);
		File fileRegle= dialogue.getSelectedFile();
		try {
			dialogue =new JFileChooser(fileRegle.getCanonicalFile());
        } catch(IOException e) {
        	System.err.println("impossible de lire le fichier, ouverture de la boite de dialogue sur le répertoire HOME");
        	dialogue =new JFileChooser();
        }
		dialogue.setDialogTitle("Choisissez votre fichier XML des FAITS.");
		dialogue.showOpenDialog(null);
		File fileFait =  dialogue.getSelectedFile();

		ArrayList<Operateur> operateurs = MoteurInference.generateOperator();
		ArrayList<Variable> variables = LectureFichierXML.getVariable(fileRegle);
		
		if(LectureFichierXML.checkOperateurs(fileRegle, operateurs)){
			ArrayList<Regle> regles = LectureFichierXML.getRules(fileRegle, variables, operateurs);
			ArrayList<Variable> faits=LectureFichierXML.getVariable(fileFait);
			MoteurInference.enProfondeur(regles, faits);
		}
		else
			System.out.println("Votre fichier XML ne respecte pas les operateurs autorisé par ce logiciel. Seuls les opérateurs \"ET\", \"NON\", \"OU\" et \"EGAL\" sont autorisés.");
	}

}
