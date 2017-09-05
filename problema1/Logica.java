package problema1;

import java.util.ArrayList;
import java.util.List;

public class Logica {
	
	private List<String> premissas;
	private String conclusao;
	private boolean isVerdade = false;
	
	public Logica(List<String> premissas, String conclusao){
		this.premissas = premissas;
		this.conclusao = conclusao;
		imprimirPremissas();
	}
	
	public void ConclusaoEstaNasPremissas(){
		
		for(int i=0;i<premissas.size();i++){
			String temp = premissas.get(i);
			
			if(conclusao.equals(temp)){
				System.out.println("A conclusão é verdade devido a premissa " + i + ". " + temp);
				isVerdade = true;
			}
		}
	}
	
	public void imprimirPremissas(){
		for(int i=0;i<premissas.size();i++){
			String temp = premissas.get(i);
			System.out.println(i + ". " + temp);
		}
		System.out.println("==============");
		ConclusaoEstaNasPremissas();
	}
	
	private void adicionarPremissa(String novaPremissa) {
		if(!(premissas.contains(novaPremissa))){
			premissas.add(novaPremissa);
		}
	}

	
	public void aplicarRegras(){
		
		for(int i=0; i<premissas.size() && isVerdade == false; i++){
			String prem1 = premissas.get(i);
			String[] palavras = prem1.split(" ");
			
			//Verifica se a premissa contem a operação "implica"
			for (int j = 0; j<palavras.length; j++){
				
				if(palavras[j].equals(Maquina.IMP)){
					String[] operandos = buscarOperandosImplica(palavras,j);
					
					modusPonens(operandos[0],operandos[1]);
					modusTollens(operandos[0],operandos[1]);
					silogismoHipo(operandos[0],operandos[1]);
				}
				
				if(palavras[j].equals(Maquina.OU)){
					String[] operandos = buscarOperandosOu(palavras,j);
					silogismoDisjun(operandos[0],operandos[1]);
				}
				
				//Verifica se o fato pode ser aplicado em casos de modus ponens
				if(palavras.length == 1){
					fato(palavras[0]);
				}
				
				//Verifica se a premissa do tipo "nao X" pode ser aplicado em casos de sislogismo disjuntivo ou modus tollens
				if(palavras[j].equals(Maquina.NAO) && palavras.length==2){
					operacaoNao(palavras[1]);
				}
				
			}
		}
	}

	//Faz a busca pelos operandos do operador "implica"
	private String[] buscarOperandosImplica(String[] palavras, int posImplica) {
		String temp1= "";
		String temp2= "";
		
		//Seleciona o primeiro operando, localizado antes da palavra "implica"
		for(int i=0; i<posImplica; i++){
			//Concatena as palavras adicionando um " " entre ela, porem adiciona um espaço no final q sera preciso retirar depois
			temp1 = temp1 + palavras[i] +" ";
		}
		
		for(int i = posImplica+1; i<palavras.length; i++){
			temp2 = temp2 + palavras[i] + " ";
		}
		//Retira o " " no final do operador gerado.
		String op1 = temp1.substring(0, temp1.length()-1);
		String op2 = temp2.substring(0, temp2.length()-1);
		String[] retorno = {op1,op2};
		
		return retorno;
	}
	
	private String[] buscarOperandosOu(String[] palavras, int posOu) {
		String temp1="";
		String temp2="";
		//Seleciona o primeiro operando, localizado antes da palavra "ou"
			for(int i=0; i<posOu; i++){
				//Concatena as palavras adicionando um " " entre ela, porem adiciona um espaço no final q sera preciso retirar depois
				temp1 = temp1 + palavras[i] +" ";
			}
			
			for(int i = posOu+1; i<palavras.length; i++){
				temp2 = temp2 + palavras[i] + " ";
			}
			//Retira o " " no final do operador gerado.
			String op1 = temp1.substring(0, temp1.length()-1);
			String op2 = temp2.substring(0, temp2.length()-1);
			String[] retorno = {op1,op2};

			return retorno;
	}

	private void modusPonens(String operando1, String operando2) {	
		
		for(int j=0; j<premissas.size() && isVerdade == false; j++){
			
			//Verifica se alguma outra premissa está de acordo com o modus ponens
			if(premissas.get(j).equals(operando1)){
				//Seleciona o segundo operando, localizado depois da palavra "implica"
				System.out.println("Aplicando o Modus Ponens com ("+ operando1 +" "+Maquina.IMP +" " +operando2
				+ ") e "+ premissas.get(j) +" temos uma nova premissa:");
				adicionarPremissa(operando2);
				//premissas.add(operando2);
				imprimirPremissas();
			}
		}
		
	}
	
	private void modusTollens(String op1, String op2) {
		for(int i = 0; i<premissas.size() && isVerdade==false; i++){
			
			//VErifica se alguma premissa esta de acordo com o modus tollens
			if(premissas.get(i).equals(Maquina.NAO + " " + op2)){
				System.out.println("Aplicando o Modus Tollens com ("+ op1 +" "+Maquina.IMP +" " +op2
						+ ") e "+ premissas.get(i) +" temos uma nova premissa:");
				
				adicionarPremissa(Maquina.NAO + " " + op1);
				//premissas.add(Maquina.NAO + " " + op1);
				imprimirPremissas();
			}
		}
		
	}
	
	private void silogismoHipo(String op1Prem1, String op2Prem1) {
		for(int i = 0; i<premissas.size() && isVerdade == false; i++){
			
			//é necessário saber se a premissa2 possui a operação implica
			String prem2 = premissas.get(i);
			String[] palavrasPrem2 = prem2.split(" ");

			for (int j = 0; j<palavrasPrem2.length; j++){
				
				if(palavrasPrem2[j].equals(Maquina.IMP)){
					String[] operandos = buscarOperandosImplica(palavrasPrem2,j);
					String op1Prem2 = operandos[0];
					String op2Prem2 = operandos[1];
					
	//Verifica o silogismo hipotetico (P>Q), Q operador 2 da premissa 1, (Q>R), Q operador 1 da premissa 2
					if(op2Prem1.equals(op1Prem2)){
						System.out.println("Aplicando o Silogismo Hipotetico com "
								+ "("+ op1Prem1 +" "+Maquina.IMP +" " +op2Prem1
								+ ") e ("+ prem2 +") temos uma nova premissa:");
						adicionarPremissa(op1Prem1 + " " + Maquina.IMP + " " + op2Prem2);
						//premissas.add(op1Prem1 + " " + Maquina.IMP + " " + op2Prem2);
						imprimirPremissas();
					}
					
				}
			}
		}
	}
	
	private void silogismoDisjun(String op1, String op2) {
		for(int i =0; i<premissas.size() && isVerdade == false; i++){
			
			//VErifica se alguma premissa está de acordo com o silogismo disjuntivo
			if(premissas.get(i).equals(Maquina.NAO + " " + op1)){
				System.out.println("Aplicando o Silogismo Disjuntivo "
						+ "com ("+ op1 +" "+Maquina.OU +" " +op2
						+ ") e "+ premissas.get(i) +" temos uma nova premissa:");
				adicionarPremissa(op2);
				//premissas.add(op2);
				imprimirPremissas();
			}
			if(premissas.get(i).equals(Maquina.NAO + " "+ op2)){
				System.out.println("Aplicando o Silogismo Disjuntivo "
						+ "com ("+ op1 +" "+Maquina.OU +" " +op2
						+ ") e "+ premissas.get(i) +" temos uma nova premissa:");
				
				adicionarPremissa(op1);
				//premissas.add(op1);
				imprimirPremissas();
			}
		}
	}
	private void fato(String fato) {
		
		for(int i=0; i<premissas.size() && isVerdade == false; i++){
			String[] palavras = premissas.get(i).split(" ");
			
			for(int j = 0; j<palavras.length;j++){
				
				//Verifica se alguma premissa se encaixa no modus ponens
				if(palavras[j].equals(Maquina.IMP)){
					String[] operandos = buscarOperandosImplica(palavras, j);
					
					if(fato.equals(operandos[0])){
						System.out.println("Aplicando o Modus Ponens com ("+ premissas.get(i)
						+") e "+ fato +" temos uma nova premissa:");
						
						adicionarPremissa(operandos[1]);
						//premissas.add(operando2);
						imprimirPremissas();
					}
				}
			}
		}
	}
	
	//Caso a maquina encontre a operação ou antes de achar uma operação nao, é preciso analisar o nao tbm
	private void operacaoNao(String opUnario) {
		
		for(int i =0; i<premissas.size() && isVerdade == false; i++){
			String[] palavras = premissas.get(i).split(" ");
			
			for(int j = 0; j<palavras.length;j++){
			
				//VErifica se alguma premissa está de acordo com o silogismo disjuntivo
				if(palavras[j].equals(Maquina.OU)){
					String[] operandos = buscarOperandosOu(palavras, j);
					
					if(opUnario.equals(operandos[0])){
						System.out.println("Aplicando o Silogismo Disjuntivo "
								+ "com ("+ premissas.get(i)
								+ ") e "+ Maquina.NAO + " " + opUnario +" temos uma nova premissa:");
				
						adicionarPremissa(operandos[1]);
						//premissas.add(operandos[1]);
						imprimirPremissas();
					}
					if(opUnario.equals(operandos[1])){
						System.out.println("Aplicando o Silogismo Disjuntivo "
								+ "com ("+ premissas.get(i)
								+ ") e "+ Maquina.NAO + " " + opUnario +" temos uma nova premissa:");
				
						adicionarPremissa(operandos[0]);
						//premissas.add(operandos[0]);
						imprimirPremissas();
					}
				}
				
				//Verifica se alguma premissa está de acordo com o modus tollens
				if(palavras[j].equals(Maquina.IMP)){
					String[] operandos = buscarOperandosOu(palavras, j);
					
					if(opUnario.equals(operandos[1])){
						System.out.println("Aplicando o Modus Tollens com ("+ premissas.get(i) + ") e "+
								Maquina.IMP + " " + opUnario +" temos uma nova premissa:");
						
						adicionarPremissa(Maquina.NAO + " " + operandos[0]);
						//premissas.add(Maquina.NAO + " " + op1);
						imprimirPremissas();
					}
				}
			}
		}		
	}
}
