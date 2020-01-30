package bd_erronnee;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

 

public class Jour {
	LocalDate date;
	HashSet <Chirurgien> chirurgiensDuJour; //Liste des chirurgiens presents ce jour
	HashSet <Salle> sallesDuJour; //Liste des salles utilisees ce jour
	LinkedList <Operation> operationsDuJour;
	LinkedList<Conflit> conflitsDuJour;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public Jour(LocalDate date) { //Constructeur
		
		this.date=date;
		chirurgiensDuJour = new HashSet<>();
		sallesDuJour = new HashSet<>();
		operationsDuJour = new LinkedList<>();
		conflitsDuJour = new LinkedList<>();
		
		return;
		
	}
	public int nbconflit(Operation opRef) {
		Operation op2 , tmp , memOpRef = opRef;
		Iterator<Operation> iteratorOperation2 = this.operationsDuJour.iterator();
		int nombreconflits = 0;
		while(iteratorOperation2.hasNext()) {
			opRef=memOpRef;
			op2 = iteratorOperation2.next();
			if(opRef.debut.isAfter(op2.debut)) { //la premiere operation dans le temps est op1 
				tmp= opRef;
				opRef=op2;
				op2=tmp;
			}
				
				
				
			if(opRef.fin/*.plusMinutes(10)*/.isAfter(op2.debut)) { //Plus 10 minutes car temps de pause entre chaque operations
				
				if(opRef.chirurgien.equals(op2.chirurgien)) { // Ubiquite ou Chevauchement
					
					if(opRef.salle.equals(op2.salle)) { 
						// Chevauchement
						nombreconflits++;
					}else {
						//Ubiquite
						nombreconflits++;
					}
				}
				else if(opRef.salle.equals(op2.salle)){ //Interference
					//Interference
					nombreconflits++;
					
				}
			}
		}
		
		return nombreconflits;
	}
	public void detecterConflit() {
		
		Set<Operation> listop = new HashSet<>();
		Iterator<Operation> it = operationsDuJour.iterator();
		
		while(it.hasNext()) { //copie de operationsdujour
			listop.add(it.next());
		}
		
		
		
		
		Iterator<Operation> iteratorOperation1 , iteratorOperation2 ;
		iteratorOperation1 = this.operationsDuJour.iterator();
		Operation op1 , op2 , tmp , memop1;
		while(iteratorOperation1.hasNext()) {
			
			op1 = iteratorOperation1.next();
			listop.remove(op1);
			Conflit conflit;
			memop1=op1;
			iteratorOperation2 = listop.iterator();
			while(iteratorOperation2.hasNext()) {
				op1=memop1;
				op2 = iteratorOperation2.next();
				conflit = null;
				if(op1.debut.isAfter(op2.debut)) { //la premiere operation dans le temps est op1 
					tmp= op1;
					op1=op2;
					op2=tmp;
				}
					
					
					
				if(op1.fin/*.plusMinutes(10)*/.isAfter(op2.debut)) { //Plus 10 minutes car temps de pause entre chaque operations
					
					if(op1.chirurgien.equals(op2.chirurgien)) { // Ubiquite ou Chevauchement
						
						if(op1.salle.equals(op2.salle)) { 
							// Chevauchement
							conflit = new Chevauchement(op1,op2);
						}else {
							//Ubiquite
							conflit = new Ubiquite(op1,op2);
						}
					}
					else if(op1.salle.equals(op2.salle)){ //Interference
						//Interference
						conflit = new Interference(op1,op2);
						
					}
				}
				if(conflit == null) {
					continue;
				}else {
					op1.nouveauConflit(conflit);
					op2.nouveauConflit(conflit);
					this.conflitsDuJour.add(conflit);
				}
			}
			
		}
		
		
		
		return;
	}
	
	public String toString() {
		String s = new String();
		s=date.toString();
		return s;
	}
	
	public void affichage(String type) {
		System.out.println(this.date.format(formatter));
		if(type.equals("salle")) {
			Iterator<Salle> itsalle = sallesDuJour.iterator();
			Salle s;
			Operation o;
			
			while(itsalle.hasNext()) {
				s=itsalle.next();
				System.out.println(s);
				Iterator<Operation> itoperation = this.operationsDuJour.iterator();
				
				while(itoperation.hasNext()){
					o=itoperation.next();
					if(o.salle.equals(s)) {
						System.out.println("\t" +o.id+"/ "+ o.debut + " - " + o.fin + " - "+ o.chirurgien);
					}
				}
				
				
			}
		}
		else if(type.equals("chirurgien")){
			
			Iterator<Chirurgien> itchirugien = chirurgiensDuJour.iterator();
			Chirurgien c;
			Operation o;
			while(itchirugien.hasNext()) {
				c=itchirugien.next();
				System.out.println(c);
				Iterator<Operation> itoperation = this.operationsDuJour.iterator();
				
				while(itoperation.hasNext()){
					o=itoperation.next();
					if(o.chirurgien.equals(c)) {
						System.out.println("\t" +o.id+"/ "+ o.debut + " - " + o.fin + " - "+ o.salle);
					}
				}
				
				
			}
			
			
			
		}else System.out.println("Erreur type : type = salle ou type = chirurgien");
		
		System.out.println();
	}

	public void afficherConflits() {
		Iterator<Conflit> it = this.conflitsDuJour.iterator();
		System.out.println(this.date.format(formatter));
		while(it.hasNext()) {
			it.next().affichage();
		}
				
	}
	
	public void ajouteroperation(Operation op) {
		operationsDuJour.add(op);
		this.chirurgiensDuJour.add(op.chirurgien);
		this.sallesDuJour.add(op.salle);
	}
}
























