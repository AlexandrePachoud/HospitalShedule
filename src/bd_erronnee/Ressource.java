package bd_erronnee;

public class Ressource {
	public int id;
	public String nom;
	
	public int hashCode() {
		return nom.hashCode();
	}
	
	public String toString() {
		String s = new String(nom);
		return s;
	}
	
	public Ressource(String nom) {
		this.id=nom.hashCode();
		this.nom=nom;
		return;
	}
}
