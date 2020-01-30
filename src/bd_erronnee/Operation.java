package bd_erronnee;
import java.time.*;
import java.util.*;

public class Operation {
	Salle salle;
	Chirurgien chirurgien;
	LocalTime debut;
	LocalTime fin;
	Integer id;
	Jour date;
	Set<Conflit> conflits;
	//La date est contenue dans la classe Jour
	
	public Operation(Integer id,LocalTime debut,LocalTime fin,Chirurgien chirurgien,Salle salle, Jour date) {
		this.salle=salle;
		this.chirurgien=chirurgien;
		this.debut=debut;
		this.fin=fin;
		this.id=id;
		this.date=date;
		this.conflits=null;
	}
	public Operation copy() {
		Operation newOp = new Operation(
				this.id,
				this.debut,
				this.fin,
				this.chirurgien,
				this.salle,
				this.date
				);
		return newOp;
	}
	public boolean enConflit() {
		if(conflits == null) return false;
		if(conflits.isEmpty()){
			return false;
		}
		else {
			return true;
		}
	}
	public void affichage() {
		System.out.println("Operation n°"+id+" -- effectuee par  "+chirurgien+" dans salle "+salle+" de "+debut+" a "+fin);
	}
	
	public Duration duration() {
		return(Duration.between(this.debut, this.fin));
	}
	
	public void nouveauConflit(Conflit conflit) {
		if(this.conflits==null) {
			this.conflits = new HashSet<>();
			this.conflits.add(conflit);
		}
		else {
			this.conflits.add(conflit);
		}
	}
}
