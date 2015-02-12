package ToolBox;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import Objet.Operateur;
import Objet.Regle;
import Objet.Variable;
/**
 * 
 * @author Corentin, Arthur, Aymeric et Jules, dans le cadre d'un exercice de système expert à l'ESAIP - St Barthélémy d'Anjou - France
 * @category Toolbox de parsage de fichier XML spécifique.
 *
 */


public class LectureFichierXML {
	
	/**
	 * 
	 * @param file: fichier à passer en paramètre, doit être un fichier XML.
	 * doit respecter le format suivant
	 * <root>
	 *	 <variables>
	 *		<variable>
	 *			<id>idVariable1</id>
	 *			<nom>nomVariable1</nom>
	 *		</variable>
	 *		<variable>
	 *			<id>idVariable2</id>
	 *			<nom>nomVariable2</nom>
	 *		</variable>
	 *	</variables> 
	* </root> 
	 * @return ArrayList<Variable> contenant tous les objets variables trouvé dans le document XMl
	 */
	
	public static ArrayList<Variable> getVariable(File file){
		SAXBuilder builder = new SAXBuilder();
		//initialisation de la variable de retour. 
		ArrayList<Variable> variables = new ArrayList<Variable>();
		try {
			//Creation du document.JDOM2 pour lecture du fichier XML
			Document document = (Document) builder.build(file);
			//récuperation de la balise racine du document XML
			Element rootNode = document.getRootElement();
			//récuperation du noeud de la balise <variables>
			Element variablesNode = rootNode.getChild("variables");
			//récuperation de tout les noeuds enfants de variables.
			List<Element> list = variablesNode.getChildren("variable");
			//Parcours de tous les éléments de la liste afin d'initialiser les variables.
			for (Element e:list) {
				//ajout des Objets variables dans la liste retourne.
				variables.add(new Variable(e.getChildText("id"),e.getChildText("nom")));
			}
		} 
		catch (IOException io) {
			System.err.println(io.getMessage());
		} 
		catch (JDOMException jdomex) {
			System.err.println(jdomex.getMessage());
		}
		return variables;
	}
	
	/**
	 * 
	 * @param file: fichier à passer en paramètre, doit être un fichier XML.
	 * doit respecter le format suivant
	 * <root>
	 * 	<operations>
	 *		<operation>idOperateur</operation>
	 *		<operation>idOperateur</operation>
	 *	</operations>
	 * </root>
	 * @return boolean true si tout les operateurs trouvés dans le fichier sont présent dans le logiciel, 
	 *false si il en manque un.
	 */

	public static boolean checkOperateurs(File file, ArrayList<Operateur> operateurs){
		SAXBuilder builder = new SAXBuilder();
		//initialisation de la variable de retour à vrai puisque nous cherchons la non présence, le postulat sera donc ils sont tous présent.
		boolean ruleRespected =true;
		try {
			//Creation du document.JDOM2 pour lecture du fichier XML
			Document document = (Document) builder.build(file);
			//récuperation de la balise racine du document XML
			Element rootNode = document.getRootElement();
			//récuperation du noeud de la balise <operations>
			Element variablesNode = rootNode.getChild("operations");
			//récuperation de tout les noeuds enfants de operations.
			List<Element> list = variablesNode.getChildren("operation");
			//Parcours de tous les éléments de la liste afin de vérifier que les opérateurs sont bien supporter par le système.
			
			//initialisation de la variable de parcour de la boucle.
			int i =0;
			//Je parcours ma liste tant que ma regle est réspecté (operateurs du fichier présents dans le système) et que ma variable de boucle est inférieur à la taille de ma liste.
			while(ruleRespected&&i<list.size()) {
				
				//initialisation de la variable de sortie de la boucle de recherche
				boolean found = false;
				//initialisation de la seconde variable de parcour de la boucle.
				int j =0;
				//je parcour ma boucle tant que je n'ai pas trouvé mon objet et que ma variable de boucle est plus petite que la taille de ma liste.
				while(!found&&j<operateurs.size()){
					//afin d'éviter une condition inutile, found prendra directement la valeur de la condition: "id de mon opérateur(j) système" = "id opérateur (i) fichier"
					found=operateurs.get(j).getId().equals(list.get(i).getText());
					//itération de la boucle
					++j;
				}
				//ma régle est respecté si je trouve found, elle ne l'est pas si je ne le trouve pas.
				ruleRespected=found;
				//iteration de la première boucle.
				++i;
			}
		} 
		catch (IOException io) {
			System.err.println(io.getMessage());
		} 
		catch (JDOMException jdomex) {
			System.err.println(jdomex.getMessage());
		}
		//retour du boolean du respect de la regle.
		return ruleRespected;
	}
	/**
	 * 
	 * @param file: fichier à passer en paramètre, doit être un fichier XML.
	 * doit respecter le format suivant
	 * <root>
	 * 	<regles>
	 *		<regle id="idRegle1">variable operateur NON variable EGAL variable</regle>
	 *		<regle id="idRegle2">variable operateur variable EGAL variable</regle>
	 *		<regle id="idRegle3">variable operateur variable EGAL NON variable</regle>
	 *		<regle id="idRegle4">NON variable operateur variable EGAL NON variable</regle>
	 *		.....
	 *	</regles>
	 *</root>
	 *Les regles doivent être inscritent dans l'ordre d'importance pour que l'algorithme de recherche en profondeur fonctionne.
	 * @return ArrayList<Regle> contenant toutes les règles du fichier XML..
	 */
	public static ArrayList<Regle> getRules(File file,ArrayList<Variable> variables,ArrayList<Operateur> operateurs){
		
		SAXBuilder builder = new SAXBuilder();
		//initialisation de la variable de retour.
		ArrayList<Regle> regles = new ArrayList<Regle>();
		try {
			//idem qu'auparavant pour cette partie du code
			Document document = (Document) builder.build(file);
			Element rootNode = document.getRootElement();
			Element reglesNode = rootNode.getChild("regles");
			List<Element> list = reglesNode.getChildren("regle");
			//parcour de toutes les règles
			for (Element e:list) {
				//récuperation de la règle en chaine de caractère. 
				String ruleText = e.getText();
				//création d'une nouvelle règles avec l'id inscrit dans le fichier.
				Regle r = new Regle(Integer.valueOf(e.getAttributeValue("id")));
				// découpage de la chaine de la règle afin d'implémenter les éléments un à un (opérateurs et variables)
				String[] tabElementRegle = ruleText.split(" ");
				//création d'une variable de sortie de boucle car si l'ordre variable-regle-variable (A etB) ou variable-regle-regle-variable  (A et non B) 
				//sinon sortie de la boucle, la règle n'est pas implémenté et une demande de correction est demandée à l'utilisateur.
				boolean orderRespected = true;
				int k = 0;
				//tant que ma règle est respecté et que ma variable de parcour de la boucle est plus petite que la taille de mes éléments à parcourir
				while(orderRespected&&k<tabElementRegle.length){
					//initialisation d'une variable s étant soit un opérateur, soit une variable.
					String s = tabElementRegle[k].trim();
					//Création d'une variable de sortie de boucle passant à true lorsque j'ai un résultat de recherche.
					boolean found = false;
					int i=0;
					//Parcour en premier lieu des operateurs car la liste d'opérateur est plus petite que la liste de variable et qu'ils sont souvent présent dans les règles.
					while(!found&&i<operateurs.size()){
						found=operateurs.get(i).getId().equals(s);
						//si élément trouvé, on ne fait pas l'itération car on utilise l'index pour récupérer la variable après.
						if(!found) ++i;
					}
					//si l'élément est trouvé dans les operateurs alors on l'ajoute dans la règle
					if(found) orderRespected=r.add(operateurs.get(i));
					//Sinon on parcourt la liste de variable. 
					else{
						i=0;
						while(!found&&i<variables.size()){
							found=variables.get(i).getId().equals(s);
							if(!found) ++i;
						}
						//Si l'élément est trouvé dans la liste de variable, alors on l'ajoute à la variable. 
						if(found)orderRespected=r.add(variables.get(i));
						//sinon ma règle n'est pas respecté, un des éléments composant la liste n'existe pas dans les déclarations de variables, la règles n'est pas implémentée.
						else orderRespected= false;
					}
					k++;
				}
				//règle non respectées, avertissement envoyé à l'utilisateur. 
				if(!orderRespected)System.err.println("la regle n°"+r.getId()+ " n'a pas respecté l'enchainement variable->operateur ou possède une variable non déclarée, elle n'a donc pas été implémenté.");
				//Sinon la règle est ajouté à la liste.
				else regles.add(r);	
			}
		} 
		catch (IOException io) {
			System.err.println(io.getMessage());
		} 
		catch (JDOMException jdomex) {
			System.err.println(jdomex.getMessage());
		}
		return regles;
	}
}
