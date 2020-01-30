package bd_erronnee;
import java.io.*;
import java.time.*;
import java.util.*;

public class Hopital {
	
	Collection<Salle> salle;
	Collection<Chirurgien> chirurgien;
	Calendrier calendrier;
	
	public Hopital() {
		this.salle = new HashSet<>();
		this.chirurgien = new HashSet<>();
		this.calendrier = new Calendrier();
	}
	
	public static Hopital lecture(String filename) {
		Hopital hop = new Hopital();
		Map<String,Chirurgien> mapchirurgien = new HashMap<>();
		Map<String,Salle> mapsalle = new HashMap<>();
		Map<LocalDate , Jour> mapjour = new HashMap<>();
		
		try {
			File F = new File(filename);
			Scanner scan = new Scanner(F);
			String ligne;
			String [] elements;
			
			if(scan.hasNextLine()) scan.nextLine(); //on passe la premiere ligne de texte
			
			while(scan.hasNextLine()) {
				ligne = scan.nextLine();
				elements = ligne.split(";");
				
				if(elements.length!=6) {
					System.out.println("Ligne ne contient pas le bon nombre d'elements (6) ");
					continue;
				}
				Integer id = Integer.parseInt(elements[0]);
				LocalTime debut = LocalTime.parse(elements[2]);
				LocalTime fin = LocalTime.parse(elements[3]);
				
				Chirurgien newchirurgien; //obtenir l'objet Chirurgien correspondant
				if(mapchirurgien.containsKey(elements[5])) {
					newchirurgien = mapchirurgien.get(elements[5]);
				}else {
					newchirurgien = new Chirurgien(elements[5]);
					mapchirurgien.put(elements[5], newchirurgien);
					hop.chirurgien.add(newchirurgien);
				}
				
				Salle newsalle; //obtenir l'objet Salle 
				if(mapsalle.containsKey(elements[4])) {
					newsalle = mapsalle.get(elements[4]);
				}else {
					newsalle = new Salle(elements[4]);
					mapsalle.put(elements[4], newsalle);
					hop.salle.add(newsalle);
				}
				
				LocalDate datejour;
				Jour jour;
				String [] 	String_date = elements[1].split("/");
				int [] 		date = new int[3];
				date[2] = Integer.parseInt(String_date[2]);
				date[1] = Integer.parseInt(String_date[1]);
				date[0] = Integer.parseInt(String_date[0]);
				datejour = LocalDate.of(date[2], date[1], date[0]);
				
				
				if(mapjour.containsKey(datejour)) {
					jour = mapjour.get(datejour);
				}else{
					
					jour = new Jour(datejour);
					mapjour.put(datejour, jour);
					hop.calendrier.calendrier.add(jour);
				}
				
				
				
				
				Operation op = new Operation( id, debut, fin, newchirurgien, newsalle, jour);
				
				jour.ajouteroperation(op);
				
			}
			
			scan.close();
		}
		catch(IOException e) {
			System.out.println("Erreur Fichier (lecture)");
			return hop;
		}
		return hop;
		
	}
	
	public void Ecriture(String filename) {
		ArrayList<Operation> operation = this.getAllOperation();
		Operation op;
		
		try {
			File f = new File(filename);
			PrintWriter writer = new PrintWriter(f);
			writer.println("ID CHIRURGIE;DATE CHIRURGIE;HEURE_DEBUT CHIRURGIE;HEURE_FIN CHIRURGIE;SALLE;CHIRURGIEN");
			Iterator<Operation> it = operation.iterator();
			
			while(it.hasNext()) {
				op = it.next();
				writer.print(op.id+";");
				writer.print(op.date.date.getDayOfMonth()+"/"+op.date.date.getMonthValue()+"/"+op.date.date.getYear()+";");
				writer.print(op.debut+";");
				writer.print(op.fin+";");
				writer.print(op.salle.nom+";");
				writer.println(op.chirurgien.nom);
			}
			
			writer.close();
			
		}
		catch (IOException e){
			System.out.println("Erreur Fichier (ecriture)");
		}
	}
	
	public void affichage(String type) {
		System.out.println("Hopital\n");
		System.out.println("Liste des chirurgiens de l'hopital");
		System.out.println(this.chirurgien);
		System.out.println("\nListe des salles de l'hopital");
		System.out.println(this.salle);
		System.out.println("\nCalendrier de l'hopital\n");
		
		if(type.equals("chirurgien")) {

			System.out.println("NOM_CHIRURGIEN");
			System.out.println("\t ID_OPERATION : HEURE_DEBUT - HEURE_FIN - SALLE");
		}
		else if(type.equals("salle")) {
			System.out.println("NOM_SALLE");
			System.out.println("\t ID_OPERATION : HEURE_DEBUT - HEURE_FIN - CHIRURGIEN");
		}
		System.out.println("\n");
		this.calendrier.affichage(type);
	}
	
	public void detecterConflit() {
		this.calendrier.detecterConflit();
	}
	
	public void afficherConflits() {
		this.calendrier.afficherConflits();
	}
	
	public ArrayList<Operation> getAllOperation(){
		 Iterator<Jour> itjour = this.calendrier.calendrier.iterator();
		 Jour j;
		 ArrayList<Operation> AllOperation = new ArrayList<>();
		 
		 while(itjour.hasNext()) { //parcours de jour en jours
		  j = itjour.next();
		  AllOperation.addAll(j.operationsDuJour);
		 }
		 return AllOperation;
	}
	
	public static void main(String[] args) {
		Hopital hop = Hopital.lecture("MiniBase(1).csv");
		hop.detecterConflit();
		hop.afficherConflits();
		hop.affichage("salle");
		Statistiques s = Statistiques.of(hop);
		s.affichage();
		System.out.println(s.MoyenneDureeOperationParChirurgien);
		hop.afficherConflits();
		//hop.Ecriture("testEcriture.csv");

	}

}
