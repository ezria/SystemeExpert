package ToolBox;
import java.util.ArrayList;

import Objet.Operateur;
import Objet.Regle;
import Objet.Variable;

/**
 * 
 * @author Corentin, Arthur, Aymeric et Jules, dans le cadre d'un exercice de système expert à l'ESAIP - St Barthélémy d'Anjou - France
 * @category classe toolbox permettant la génération des opérateurs systèmes ainsi que les algorithmes de résolution du système experts.
 *
 */

public class MoteurInference{
	/**
	 * 
	 * @return ArrayList<Operateur> liste des opérateurs supportés par le système. ET, OU, NON et EGAL. Ils sont donc les seuls opérateurs acceptés dans le fichier XML.
	 */
	public static ArrayList<Operateur>  generateOperator(){
		ArrayList<Operateur> operations = new ArrayList<Operateur>();
		operations.add(new Operateur("ET", "et"));
		operations.add(new Operateur("OU", "ou"));
		operations.add(new Operateur("EGAL", "égal"));
		operations.add(new Operateur("NON", "non"));
		return operations;
	}
	
	/**
	 * 
	 * @param regles {@link ArrayList}< {@link Regle} > liste des règles renvoyé par la toolBox de lecture des fichiers XML à partir du fichier de regles
	 * @param faits {@link ArrayList}< {@link Variable} >liste des faits renvoyé par la toolBox de lecture des fichiers XML à partir du fichier des faits.
	 */
	public static void enProfondeur(ArrayList<Regle> regles, ArrayList<Variable> faits) {
		//initialisation de la variable de parcour de boucle ainsi que d'une variable du nombre de résultat permettant l'affichage du résultat.
		int nombreRegleTrouve = 0, i=0;
		//Tant que je n'attein pas la fin de la boucle je continue à chercher, Si j'atteins la fin c'est que plus aucune règle n'est vérifiable, c'est donc la fin de l'algorithme.
		while(i<regles.size()){
			//On utilise la fonction de résolution des règles afin de recevoir un objet de classe Variable.
			Variable resultat = regles.get(i).test(faits);
			//Si le resultat est non nul, c'est que ma règle fonctionne est donc resultat vaut le resultat de la rêgle.
			if(resultat!=null){
				//Si le resultat n'est pas dans les faits alors je l'ajoute à ma liste de faits.
				if(!faits.contains(resultat))faits.add(resultat);
				//Variable pour afficher toutes les initales des variables présentes dans ma liste de faits.
				String listeF = "";
				for(Variable v: faits) listeF+=v.getId()+", ";
				//affichage du résultats de la règle. BF -> Base des faits, R la règle utilisée dans l'itération.
				System.out.println("BF"+(++nombreRegleTrouve)+"={"+listeF.substring(0, listeF.length()-2)+"} / R"+regles.get(i).getId());
				//On supprime la règle de la base des règles.
				regles.remove(i);
				//on réinitialise i;
				i=0;
			}
			//Sinon on incremente i;
			else
				i++;
		}
		if(nombreRegleTrouve==0) System.out.println("Aucune règle n'a été validé");
	}
}
