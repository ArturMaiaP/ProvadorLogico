package problema1;

import java.util.ArrayList;
import java.util.List;

public class Maquina {
	
	public static final String IMP = "implica";
	public static final String AND = "and";
	public static final String OU = "ou";
	public static final String NAO = "nao";
	
	public static void main(String args[]){
		
		String prem1 = "A implica B";
		String prem2 = "B implica C";
		String prem3 = "C implica D";
		String prem4 = "nao D";
		String prem5 = "A ou E";
		String prem6 = "nao A";
		
		List<String> premissas = new ArrayList<String>();
		premissas.add(prem5);
		premissas.add(prem2);
		premissas.add(prem3);
		premissas.add(prem4);
		premissas.add(prem1);
		premissas.add(prem6);
		
		String conclusao = "E";
		
		Logica log = new Logica(premissas,conclusao);
		
		//log.imprimirPremissas();
		log.ConclusaoEstaNasPremissas();
		log.aplicarRegras();
		
	}
}
