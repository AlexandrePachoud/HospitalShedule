package bd_erronnee;

import java.util.*;
import java.time.*;

class Rational implements Comparable<Rational>{ 
	//PRISE SUR INTERNET : https://stackoverflow.com/questions/28298633/what-is-the-rational-class-in-java
	//ET : http://www.bonneville.nom.fr/bibliotheque/cours/POO/TP/3/Rationnel.java

    private int nominator;
    private int denominator;
    
    public Rational() {
        this(0, 1);
    }
    public static final Rational ZERO = new Rational(0);
    
    public Rational(int nominator) {
        this(nominator, 1);
    }
    
    public Rational(int nominator, int denominator) {
        this.nominator = nominator;
        this.denominator = denominator;
        if(denominator == 0) {
        	this.denominator = 1;
    	}
        simplify();
    }
    
    public double toDouble() {
    	return nominator / (double) denominator;
    }
    
    public void setnominator(int i) {
    	this.nominator = i;
    }
    public boolean setdenominator(int i) {
    	if(i==0) {
    		return false;
    	}
    	this.denominator = i;
    	return true;
    }

	public int getNominator() {
		return this.nominator;
	}
	
	public int getDenominator() {
		return this.denominator;
	}
    
	public void plus(Rational r) {
		setnominator(this.getNominator() * r.getDenominator() + r.getNominator()*this.getDenominator());
		setdenominator(r.getDenominator() * this.getDenominator() );
		
		this.simplify();
	}
	
    
	
	static public int PGCD(int m, int n) { //
	     if (m<0) return PGCD(-m,n);   
	     else if (m<n) return PGCD(n,m); //Utilise l'algorithme d'Euclide
	     else {                      // https://fr.wikipedia.org/wiki/Algorithme_d%27Euclide    
	         int r=m%n;
	         if (r==0) return n;     // cette fonction est r�cursive
	         else return PGCD(n,r);
        }  
     }
	
	public void simplify() {
		
		if (nominator<0 && denominator<0){
			nominator=-nominator;
			denominator=-denominator;
		}
		if(nominator == 0) {
			this.nominator=0;        // pour pouvoir simplifier la fraction
		    this.denominator=1;
		    return;
		}
	    int pgcd=PGCD(nominator,denominator); // on calcule le PGCD 
	    this.nominator=nominator/pgcd;        // pour pouvoir simplifier la fraction
	    this.denominator=denominator/pgcd;
	}
	
	@Override
    public String toString() {
        return nominator+"/"+denominator;
    }
	
	public int compareTo(Rational o) {
        if (this.toDouble()<o.toDouble()) return -1;
        else if (this.toDouble()>o.toDouble()) return 1;
        else return 0;
    }
	public boolean equals(Rational r) {
        return (this.nominator  ==r.nominator 
             && this.denominator==r.denominator);
    }

	public void reset() {
		this.nominator=0;
	    this.denominator=1;
	}
	

}

public class Statistiques {
	
	Integer total;
	Hopital hopital;
	Map<Chirurgien,Duration> MoyenneDureeOperationParChirurgien;
	Map<Chirurgien,		Map<Salle,	Map<Long /*NbHeures*/,	Rational>>> TabProba;
	
	//CHIRURGIEN   	SALLE   	DURATION  	HEUREDEBUT ====> Proba
	
	private Statistiques() {
		this.total = 0;
		this.hopital=null;
		this.TabProba = new HashMap<>();
		this.MoyenneDureeOperationParChirurgien = new HashMap<>();
		
	}
	
	public static Statistiques of(Hopital h) {
		Statistiques newStat = new Statistiques();
		newStat.hopital=h;
		Map<Chirurgien,Integer>OperationParChirurgien= new HashMap<>();
		Iterator<Operation> AllOp = h.getAllOperation().iterator();
		Operation op; Chirurgien c;
		Duration d1; Integer nb;
		//RECENSEMENT
		while(AllOp.hasNext()) { 
			
			op = AllOp.next();
			newStat.newData(op); //recensement
			if(newStat.MoyenneDureeOperationParChirurgien.containsKey(op.chirurgien)) {
				d1 = newStat.MoyenneDureeOperationParChirurgien.get(op.chirurgien);
				d1.plus(op.duration());
				nb = OperationParChirurgien.get(op.chirurgien);
				nb = nb + 1;
			}
			else {
				OperationParChirurgien.put(op.chirurgien, 1);
				newStat.MoyenneDureeOperationParChirurgien.put(op.chirurgien,op.duration());
			}
			
		}
		Iterator<Chirurgien> itC = newStat.MoyenneDureeOperationParChirurgien.keySet().iterator();
		while(itC.hasNext()) {
			c = itC.next();
			d1 =newStat.MoyenneDureeOperationParChirurgien.get(c);
			d1.dividedBy(OperationParChirurgien.get(c));
		}
		
		//TABLEAU DES PROBAS
			//Construction des possibilite
		Iterator<Chirurgien> AllChirurgien = h.chirurgien.iterator();
		
		Iterator<Salle> AllSalle;
		
		LinkedList<Long> toutesduration = new LinkedList<>();
		//on otient toutes les durees comprises entre 0h et 7h compris par heure
		for(int i=0; i<=8; i++) {
			toutesduration.add((long) i);
		}
		Iterator<Long>AllDurationInHours;
		
		Salle s; 
		Long d; 
		Map<Salle,	Map<Long,Rational>> dim2 ;
		Map<Long,		Rational> dim1 ;
		
		
		AllChirurgien = h.chirurgien.iterator();
		while(AllChirurgien.hasNext()) {
			c = AllChirurgien.next();
			dim2 = new HashMap<>();
			newStat.TabProba.put(c,dim2);
			AllSalle = h.salle.iterator();
			
			while(AllSalle.hasNext()) {
				s = AllSalle.next();
				dim1 = new HashMap<>();
				dim2.put(s,dim1);
				AllDurationInHours = toutesduration.iterator();
				
				while(AllDurationInHours.hasNext()) {
					d = AllDurationInHours.next();
					dim1.put(d,new Rational(0,1));
				}
			}
		}	
		newStat.CalculAllProba();
		return newStat;
	}

	private void newData(Operation op) {
		
		if(op.enConflit()) {return;} // TODO check si pas dans un conflit
		total++;
	}
	
	private Long hoursInLong(Duration d) { //arrondir a 30min pres
		Long hours = d.toHours();
		hours = Long.min(hours, 7);
		hours = Long.max(hours, 0);
		return hours;
	}
	
	private void reset() {
		
		Iterator<Chirurgien> itC; Chirurgien c;
		Iterator<Salle> itS ; Salle s;
		Iterator<Duration> itD; Duration d;
		itC = this.hopital.chirurgien.iterator();
		
		LinkedList<Duration> toutesduration = new LinkedList<>();
		//on otient toutes les durees comprises entre 0h et 7h compris par heure
		for(int i=0; i<=8; i++) {
			toutesduration.add(Duration.ofHours(i));
		}
		while(itC.hasNext()) {
			c=itC.next(); 
			itS = this.hopital.salle.iterator();
			while(itS.hasNext()) {
				s=itS.next();
				
				itD = toutesduration.iterator();
				while(itD.hasNext()) {
					d = itD.next();
					getRational(c,s,d).reset();
				}
			}
		}
	}
	private void CalculAllProba() {
		Iterator<Operation> AllOp = this.hopital.getAllOperation().
				iterator();
		Operation op;
		Rational p;
		Double totalproba1 = 0.0;
		this.reset();
		//RECENSEMENT
		while(AllOp.hasNext()) { 
			
			op = AllOp.next();
			p = TabProba
				.get(op.chirurgien)
				.get(op.salle)
				.get(hoursInLong(op.duration()))
				//.get(Duration.ofMinutes((long) ((long)op.duration().toMinutes() / (long)30))) //arrondir a 30min pres
			;
			p.setnominator( p.getNominator() + 1 );
			p.setdenominator((int)this.total);
			
			totalproba1 += p.toDouble();
			
		}
	}
	private double getProba(Chirurgien c, Salle s, Duration d) {
		
		Rational p;
		p = TabProba
				.get(c)
				.get(s)
				.get(hoursInLong(d)); 
		return p.toDouble();
	}
	
	private Rational getRational(Chirurgien c, Salle s, Duration d) {
		
		Rational p;
		p = TabProba
				.get(c)
				.get(s)
				.get(hoursInLong(d)); 
		return p;
	}
	
	
	public double getProba(Operation op) { 
		Double p = getProba(op.chirurgien, op.salle , op.duration()) ;
		return p;
	}
	
	
	public void affichage() {
		Iterator<Chirurgien> itC; Chirurgien c;
		Iterator<Salle> itS ; Salle s;
		Iterator<Duration> itD; Duration d;
		itC = this.hopital.chirurgien.iterator();
		Rational r;
		LinkedList<Duration> toutesduration = new LinkedList<>();
		//on otient toutes les durees comprises entre 0h et 7h compris par heure
		for(int i=0; i<=8; i++) {
			toutesduration.add(Duration.ofHours(i));
		}
		while(itC.hasNext()) {
			c=itC.next(); 
			System.out.println(c);
			itS = this.hopital.salle.iterator();
			while(itS.hasNext()) {
				s=itS.next();
				System.out.println("| "+s);
				itD = toutesduration.iterator();
				while(itD.hasNext()) {
					d = itD.next();
					r=getRational(c,s,d);
					if(! r.equals(Rational.ZERO)) {
						System.out.println("| | "+d.toHours()+"h  "+r+"="+r.toDouble());
					}
				}
			}
		}
		
	}

}
