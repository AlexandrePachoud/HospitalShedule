package bd_erronnee;

import java.util.Iterator;

//meme chirurgien, salles differentes
public class Ubiquite extends Conflit{
	public double solve() {
		
		if()
		
		
		//changer Chirurgien
		Jour jourActuel = op1.date;
		if(op2.date != jourActuel) {
			System.out.println("les operations sont pas le meme jour");
		}
		Operation op1AutreChirurgien, op2AutreChirurgien ;
		Chirurgien chir;
		Operation meilleuresolution1 = op1.copy(), meilleuresolution2= op2.copy();
		Integer nbconflitmeilleursolution1 = Integer.MAX_VALUE ,
				nbconflitmeilleursolution2 = Integer.MAX_VALUE, 
				nbConflitPourOp1=Integer.MAX_VALUE, 
				nbConflitPourOp2;
		Iterator<Chirurgien> itChir = jourActuel.chirurgiensDuJour.iterator();
		
		
		
		while(itChir.hasNext()) {
			chir=itChir.next();
			if(op1.chirurgien.equals(chir)) continue; // c'est le chirurgien a probleme
			//On cree une fausse operation avec juste un autre chirurgien 
			op1AutreChirurgien = op1.copy();
			op1AutreChirurgien.chirurgien = chir;
			
			//on regarde le nombre de conflit que ca genere pour trouver la meilleure solution
			nbConflitPourOp1=jourActuel.nbconflit(op1AutreChirurgien);
			
			if(nbConflitPourOp1 == 0) { meilleuresolution1 = op1AutreChirurgien;break;}
			else {
				if(nbConflitPourOp1 < nbconflitmeilleursolution1) {
					meilleuresolution1 = op1AutreChirurgien;
					nbconflitmeilleursolution1 = nbConflitPourOp1;
				}
			}
			
		}
		
		itChir = jourActuel.chirurgiensDuJour.iterator();
		if(nbConflitPourOp1 != 0) {
			while(itChir.hasNext()) {
				chir=itChir.next();
				if(op2.chirurgien.equals(chir)) continue; // c'est le chirurgien a probleme
				//On cree une fausse operation avec juste un autre chirurgien 
				op2AutreChirurgien = op2.copy();
				op2AutreChirurgien.chirurgien = chir;
				
				//on regarde le nombre de conflit que ca genere pour trouver la meilleure solution
				nbConflitPourOp2=jourActuel.nbconflit(op2AutreChirurgien);
				
				if(nbConflitPourOp2 == 0) { meilleuresolution2 = op2AutreChirurgien;break;}
				else {
					if(nbConflitPourOp2 < nbconflitmeilleursolution2) {
						meilleuresolution2 = op2AutreChirurgien;
						nbconflitmeilleursolution2 = nbConflitPourOp2;
					}
				}
			}	
		}
		
		
		if(nbconflitmeilleursolution1 < nbconflitmeilleursolution2) {
			jourActuel.operationsDuJour.remove(op1);
			jourActuel.operationsDuJour.add(meilleuresolution1);
		}
		if(nbconflitmeilleursolution2 < nbconflitmeilleursolution1) {
			jourActuel.operationsDuJour.remove(op2);
			jourActuel.operationsDuJour.add(meilleuresolution2);
		}
		
		
		//changer temps
		if(nbconflitmeilleursolution2 == Integer.MAX_VALUE && nbconflitmeilleursolution1 == Integer.MAX_VALUE) {
			//on a pas trouver de chirurgien disponible on doit donc modifier le temps
			
		}
		
				
		
		
		return 0;
	}
	public Ubiquite(Operation op1, Operation op2) {
		super(op1,op2);
		
	}
}
