package Objet;

import java.util.ArrayList;

/**
 * 
 * @author Corentin, Arthur, Aymeric et Jules, dans le cadre d'un exercice de système expert à l'ESAIP - St Barthélémy d'Anjou - France
 * @category classe Regle un id, une liste d'objet de classe {@link ObjetSysExpert} contant des objets de classe {@link Variable} et {@link Operateur} ainsi q'un algorithme d'interpretation des règles.
 *
 */

public class Regle {
	private ArrayList<ObjetSysExpert> regle;
	private int id;
	public Regle(int id){
		this.setId(id);
		this.setRegle(new ArrayList<ObjetSysExpert>());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<ObjetSysExpert> getRegle() {
		return regle;
	}

	public void setRegle(ArrayList<ObjetSysExpert> regle) {
		this.regle = regle;
	}
	/**
	 * 
	 * @param obj héritant de {@link ObjetSysExpert} permettant l'établissement d'une règle.
	 * @return vrai si lobjet dans le liste est le premier ou que c'est un opérateur ou que c'est une variable précédé d'un opérateur. 
	 * Le test n'est pas infaillible mais permet d'éliminer les erreurs les plus simples.
	 */
	public boolean add(ObjetSysExpert obj){
		
		/*L'objet est ajouté si:
		 * c'est le premier objet de ma regle.
		 * C'est un opérateur
		 * C'est une variable précédé d'un Operateur
		 */
		if(regle.size()==0 || obj instanceof Operateur ||
		(regle.get(regle.size()-1) instanceof Operateur && obj instanceof Variable)){
			regle.add(obj);
			return true;
		}
		else{
			System.err.println("Erreur, deux variables ou deux operations se suivent, veuillez corriger le fichier XML");
			return false;
		}
	}
	
	/**
	 * 
	 * @param faits {@link ArrayList} < {@link Variable} > Liste des faits provenant du fichiers faits.xml
	 * @return {@link Variable} objet résultat de la regle si la regles est repescté.
	 */
	public Variable test(ArrayList<Variable> faits){
		
		//initialisation des booléens permettants de savoir quelles sont les opérateurs qui ont été enclenché ainsi que la réponse de la rêgle. 
		boolean reponse = false, et = false, ou = false, egal = false, non = false;
		//boucle parcourant tous les objets de la règle.
		for(ObjetSysExpert o : regle){
			//si mon objet est de la classe Operateur alors on passe a vrai le boolean de l'opérateur qui est présent.
			if( o instanceof Operateur){
				switch (o.getId()) {
					case "NON":
						non = true;
						break;
					case "ET":
						et = true;
						break;
					case "OU":
						ou = true;
						break;
					case "EGAL":
						egal = true;
						break;
				}
			}
			//Sinon on vérifie la liste des faits.
			else{
				//vérification que la variable lue dans cette boucle est présente dans les faits.
				boolean varInFait = false;
				int i=0;
				while (!varInFait&&i<faits.size()){
					varInFait=faits.get(i).getId().equals(o.getId());
					i++;
				}
				//Si l'operateur ET a été enclenché, alors la réponse prend pour la valeur de la réponse ET de la présence de la variable dans la liste de faits.
				//On passe à faux le booléen ET du fait qu'il ait été utilisé.
				if(et){
					reponse = varInFait&reponse;
					et=false;
				}
				//Si l'operateur OU a été enclenché, alors la réponse prend pour la valeur de la réponse OU de la présence de la variable dans la liste de faits.
				//On passe à faux le booléen OU du fait qu'il ait été utilisé.
				else if(ou){
					reponse = varInFait||reponse;
					ou=false;
				}
				//Si les operateurs NON et EGAL ont été enclenchés, alors la réponse prend pour la valeur de NON réponse.
				//On passe à faux les booléen NON et EGAL du fait qu'ils aient été utilisé.
				else if(non&&egal){
					reponse=!reponse;
					non = false;
					egal = false;
				}
				//Si l'operateur OU a été enclenché, la réponse prend pour la valeur opposé de la présence de la variable dans la liste de faits.
				
				//On passe à faux le booléen ET du fait qu'il ait été utilisé.
				else if (non){
					reponse = !varInFait;
					non=false;
				}
				//Première variable de la règle, la réponse prend pour la valeur de la présence de la variable dans la liste de faits.
				else if (!non&&!egal&&!et&&!ou){
					reponse = varInFait;
				}
			}
		}
		//Si la réponse est vrai alors on renvoi la derniere valeur présente dans la liste de la regle qui est une Variable.
		if(reponse)
			return (Variable)regle.get(regle.size()-1);
		//Sinon renvoi d'un objet null
		else
			return null;
	}
	public String toString(){
		String returnedString = "id = " + id + " regle = ";
		for (ObjetSysExpert o : regle) returnedString += o.toString()+ " ";
		return returnedString;
	}
}
