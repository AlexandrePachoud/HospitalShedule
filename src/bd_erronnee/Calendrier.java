package bd_erronnee;

import java.util.*;

public class Calendrier {
	ArrayList<Jour> calendrier;
	
	public Calendrier() {
		this.calendrier = new ArrayList<>();
	}
	
	public void affichage(String type) {
		Iterator<Jour> itjour = calendrier.iterator();
		Jour j;
		
		while(itjour.hasNext()) {
			j=itjour.next();
			j.affichage(type);
			
		}
		
	}
	
	public void afficherConflits( ) {
		Iterator<Jour> itjour = calendrier.iterator();
		Jour j;
		
		while(itjour.hasNext()) {
			j=itjour.next();
			j.afficherConflits();
			
		}
		
	}
	
	public void detecterConflit() {
		Iterator<Jour> itjour = calendrier.iterator();
		Jour j;
		
		while(itjour.hasNext()) {
			j=itjour.next();
			j.detecterConflit();
			
		}
	}
}

