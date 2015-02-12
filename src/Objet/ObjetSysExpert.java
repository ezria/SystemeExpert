package Objet;

/**
 * 
 * @author Corentin, Arthur, Aymeric et Jules, dans le cadre d'un exercice de système expert à l'ESAIP - St Barthélémy d'Anjou - France
 * @category Objet abstrait de base utilisé pour le polymorphisme dans les listes des règles.
 *
 */

public abstract class ObjetSysExpert {
	protected String id, nom;
	protected ObjetSysExpert(){
		
	}
	protected ObjetSysExpert(String id){
		this.id = id;
	}
	protected ObjetSysExpert(String id,String nom){
		this.id = id;
		this.nom = nom;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	@Override
	public String toString(){
		return nom;
	}
}
