package bd_erronnee;


public abstract class Conflit {
	
	public abstract double solve(); //double <=> a la qualite de resolution du conflit entre 0 et 1;
	public Operation op1;
	public Operation op2;
	
	public String toString() {
		String s = new String(this.getClass().getName());
		s += ":"+op1+"  "+op2;
		return s;
	}
	public Conflit(Operation op1, Operation op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	public void affichage() {
		System.out.println(this.getClass().getName().replaceAll("bd_erronee.", ""));
		System.out.println(op1 + "\t"+ op2);
	}
}
