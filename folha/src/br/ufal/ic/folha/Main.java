package br.ufal.ic.folha;

import java.util.Scanner;

public class Main {

	private static Scanner teclado;
	private static final int linhasCal = 6;
	private static final int linhas = 100;
	private static final int colunas = 16;
	private static final int estados = 10000;
	private static int estadoAtual = 0;
	private static int estadoMaximo = 0;
	private static int id = 2019000;
	private static boolean refazer = false;
	private static String[][][] empregados = new String[linhas][colunas][estados];
	private static int[][] cal = new int[linhasCal][estados];
	
	/* 
	 * posicoes de empregados: 0 - ID; 1 - nome; 2 - endereco; 3 - tipo
	 * 4 - forma de pagamento; 5 - sindicato; 6 - ID sindicato; 7 - taxa sindical;
	 * 8 - salario horario; 9 - salario mensal; 10 - comissao; 11 - taxas adicionais;
	 * 12 - hora de entrada; 13 - hora de saida; 14 - salario acumulado;
	 * 15 - agenda de pagamento.
	 */
	
	/*
	 * posicoes de calendario: 0 - dia da semana; 1 - dia; 2 - mes; 3 - ano;
	 * 4 - semana; 5 - rodou a folha hoje? (0 nao : 1 sim).
	 */
	
	public static void main(String[] args) {
		
		int opcao = 0;
		boolean executando = true;
		
		teclado = new Scanner(System.in);
		
		// iniciando calendario
		cal[0][estadoAtual] = 4;
		cal[1][estadoAtual] = 16;
		cal[2][estadoAtual] = 10;
		cal[3][estadoAtual] = 2019;
		cal[4][estadoAtual] = 3;
		cal[5][estadoAtual] = 0;
		
		while(executando) {
			
			System.out.println("\n\n===========================================|| FOLHA DE PAGAMENTO ||==========================================");
			System.out.println("\n(1) Adicao de um empregado\t\t(5) Lancar uma taxa de servico\t\t(9)  Agenda de pagamento");
			System.out.println("(2) Remocao de um empregado\t\t(6) Alterar detalhes de um empregado\t(10) Lista de empregados");
			System.out.println("(3) Lancar um cartao de ponto\t\t(7) Rodar a folha para hoje\t\t(11) Proximo dia");
			System.out.println("(4) Lancar um resultado de venda\t(8) Undo/redo\t\t\t\t(0)  SAIR");
			System.out.println("иииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииии");
			mostrarData();
			System.out.println("=============================================================================================================");			
			
			opcao = teclado.nextInt();
			
			switch(opcao) {
			case 1:
				adicionarEmpregado(); break;
			case 2:
				removerEmpregado(); break;
			case 3:
				procurarHorista(); break;
			case 4:
				procurarComissionado(); break;
			case 5:
				lancarTaxaDeServico(); break;
			case 6:
				alterarDetalhes(); break;
			case 7:
				rodarFolhaDePagamento(); break;
			case 8:
				desfazerRefazer(); break;
			case 9:
				exibirAgenda(); break;
			case 10:
				listarEmpregados(); break;
			case 11:
				avancarDia(); break;
			case 0: 
				executando = false; break;
			default:
				System.out.println("Por favor insira uma opcao valida.");
			}
		}
	}
	
	
	private static void mostrarData() {
		
		System.out.print("                                            ");
		
		switch(cal[0][estadoAtual]) {
		case 1:
			System.out.print("Domingo, "); break;
		case 2:
			System.out.print("Segunda, "); break;
		case 3:
			System.out.print("Terca, "); break;
		case 4:
			System.out.print("Quarta, "); break;
		case 5:
			System.out.print("Quinta, "); break;
		case 6:
			System.out.print("Sexta, "); break;
		case 7:
			System.out.print("Sabado, "); break;
		}
		
		System.out.printf("%d/%d/%d\n", cal[1][estadoAtual], cal[2][estadoAtual], cal[3][estadoAtual]);
	}
	
	private static void avancarDia() {
		
		copiarEstado();
		
		cal[5][estadoAtual] = 0;
		
		cal[0][estadoAtual]++;
		cal[1][estadoAtual]++;
		
		if(cal[1][estadoAtual] == 31 && (cal[2][estadoAtual] == 4 || cal[2][estadoAtual] == 6 || cal[2][estadoAtual] == 9 || cal[2][estadoAtual] == 11)) {
			
			cal[2][estadoAtual]++;
			cal[1][estadoAtual] = cal[4][estadoAtual] = 1;
			
		} else if(cal[1][estadoAtual] == 32 && (cal[2][estadoAtual] == 1 || cal[2][estadoAtual] == 3 || cal[2][estadoAtual] == 5 || cal[2][estadoAtual] == 7 ||
				cal[2][estadoAtual] == 8 || cal[2][estadoAtual] == 10 || cal[2][estadoAtual] == 12)) {
			
			cal[2][estadoAtual]++;
			cal[1][estadoAtual] = cal[4][estadoAtual] = 1;

		} else if(cal[1][estadoAtual] == 29 && cal[2][estadoAtual] == 2 && !(eAnoBissexto())) {
			
			cal[2][estadoAtual]++;
			cal[1][estadoAtual] = cal[4][estadoAtual] = 1;
		
		} else if(cal[1][estadoAtual] == 30 && cal[2][estadoAtual] == 2 && eAnoBissexto()) {
			
			cal[2][estadoAtual]++;
			cal[1][estadoAtual] = cal[4][estadoAtual] = 1;
			
		}
		
		if(cal[2][estadoAtual] == 13) {
			cal[3][estadoAtual]++;
			cal[1][estadoAtual] = cal[2][estadoAtual] = cal[4][estadoAtual] = 1;
		}
		
		if(cal[0][estadoAtual] == 8) {
			
			cal[0][estadoAtual] = 1;
			cal[4][estadoAtual]++;
			
		}
		
	}
	
	private static boolean eAnoBissexto() {
		
		if(cal[3][estadoAtual] % 4 == 0) {
			
			if((cal[3][estadoAtual] % 100 == 0) && (cal[3][estadoAtual] % 400 != 0)) {
				
				return false;
				
			}
			return true;
		}
		return false;
	}

	private static void copiarEstado() {
		
		for(int i = 0; i < linhas; i++) {
			
			for(int j = 0; j < colunas; j++) {
				
				empregados[i][j][estadoAtual+1] = empregados[i][j][estadoAtual]; 
			}
		}
		for(int i = 0; i < linhasCal; i++) {
			cal[i][estadoAtual+1] = cal[i][estadoAtual];
		}
		estadoAtual++;
		refazer = false;
		estadoMaximo = estadoAtual;
	}
	
	private static void adicionarEmpregado() {
		
		int i;
		
		teclado = new Scanner(System.in);
		
		copiarEstado();
		
		for(i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] == null) {
				break;
			}
		}
		
		empregados[i][0][estadoAtual] = String.valueOf(id++);
		
		inserirNome(i);
		inserirEndereco(i);
		inserirTipo(i);
		inserirMetodoDePagamento(i);
		pertencerSindicato(i);
		
		System.out.println("\nEMPREGADO ADICIONADO");
		mostrarInfo(i);
		
	}
	
	private static void inserirNome(int i) {
		System.out.println("\nEntre com o nome do empregado:");
		empregados[i][1][estadoAtual] = teclado.nextLine();
	}
	
	private static void inserirEndereco(int i) {
		System.out.println("\nEntre com o endereco:");
		empregados[i][2][estadoAtual] = teclado.nextLine();
	}
	
	private static void inserirTipo(int i) {
		
		while(true) {
			System.out.println("\nInforme o tipo: (1) Horista (2) Assalariado (3) Comissionado");
			
			empregados[i][3][estadoAtual] = teclado.nextLine();
			
			if(empregados[i][3][estadoAtual].equals("1")) {
				System.out.println("\nSalario horario:");
				empregados[i][8][estadoAtual] = teclado.nextLine();
				
				while(Double.parseDouble(empregados[i][8][estadoAtual]) < 0) {
					System.out.println("\nINSIRA UM VALOR POSITIVO!");
					empregados[i][8][estadoAtual] = teclado.nextLine();

				}
				empregados[i][15][estadoAtual] = "1";
				break;
				
			} else if(empregados[i][3][estadoAtual].equals("2")) {
				System.out.println("\nSalario mensal:");
				empregados[i][9][estadoAtual] = teclado.nextLine();
				
				while(Double.parseDouble(empregados[i][9][estadoAtual]) < 0) {
					System.out.println("\nINSIRA UM VALOR POSITIVO!");
					empregados[i][9][estadoAtual] = teclado.nextLine();

				}
				empregados[i][15][estadoAtual] = "2";
				break;
				
			} else if(empregados[i][3][estadoAtual].equals("3")) {
				System.out.println("\nSalario mensal:");
				empregados[i][9][estadoAtual] = teclado.nextLine();
				
				while(Double.parseDouble(empregados[i][9][estadoAtual]) < 0) {
					System.out.println("\nINSIRA UM VALOR POSITIVO!");
					empregados[i][9][estadoAtual] = teclado.nextLine();

				}
				System.out.println("\nComissao [0 : 100]%:");
				empregados[i][10][estadoAtual] = teclado.nextLine();
				
				while(Double.parseDouble(empregados[i][10][estadoAtual]) < 0 ||
						Double.parseDouble(empregados[i][10][estadoAtual]) > 100 ) {
					System.out.println("\nINSIRA UM VALOR POSITIVO MENOR QUE 100!");
					empregados[i][10][estadoAtual] = teclado.nextLine();

				}
				empregados[i][15][estadoAtual] = "3";
				break;
			}
		}
	}
	
	private static void inserirMetodoDePagamento(int i) {
		
		while(true) {
			System.out.println("\nInforme o metodo de pagamento: (1) Em maos (2) Correios (3) Banco");
			empregados[i][4][estadoAtual] = teclado.nextLine();
			
			if(Integer.parseInt(empregados[i][4][estadoAtual]) > 0 &&
					Integer.parseInt(empregados[i][4][estadoAtual]) < 4) {
				break;
			}
		}
		
	}
	
	private static void removerEmpregado() {
		
		String id;
		
		teclado = new Scanner(System.in);
		
		System.out.println("\nInforme o ID do empregado a ser removido do sistema:");
		id = teclado.nextLine();
		
		for(int i = 0; i < linhas; i++) {
			
			if(empregados[i][0][estadoAtual] == null) {
				continue;
			}
			if(empregados[i][0][estadoAtual].equals(id)) {
				
				copiarEstado();
				System.out.printf("\nEmpregado removido: %s\n", empregados[i][1][estadoAtual]);
				for(int j = 0; j < colunas; j++) {
					empregados[i][j][estadoAtual] = null;
				}
				
				return;
			}
		}
		
		System.out.println("\nEmpregado nao encontrado!");
		
	}

	private static void procurarHorista() {
		
		String id;
		
		teclado = new Scanner(System.in);
		
		System.out.println("\nEntre com o ID do empregado:");
		id = teclado.nextLine();
		
		for(int i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] == null) {
				continue;
			}
			if(empregados[i][0][estadoAtual].equals(id) && !empregados[i][3][estadoAtual].equals("1")) {
				System.out.println("\nO empregado nao e horista!");
				return;
			} 
			if(empregados[i][0][estadoAtual].equals(id) && empregados[i][3][estadoAtual].equals("1")) {
				System.out.printf("\nEmpregado encontrado: %s\n", empregados[i][1][estadoAtual]);
				lancarCartaoDePonto(i);
				return;
			}
		}
		System.out.println("\nEmpregado nao encontrado.");
		
	}
	
	private static void lancarCartaoDePonto(int i) {
		
		int opcao;
		int hora;
		
		teclado = new Scanner(System.in);
		
		while(true) {
			System.out.println("\n(1) Hora de entrada\t(2) Hora de saida:");
			opcao = teclado.nextInt();
			
			if(opcao == 1) {
				if(empregados[i][12][estadoAtual] != null) {
					System.out.println("\nO empregado ja lancou o cartao na entrada!");
					break;
				}
				System.out.println("\nInforme a hora de entrada:");
				hora = teclado.nextInt();
				copiarEstado();
				empregados[i][12][estadoAtual] = String.valueOf(hora);
				System.out.println("\nLancamento realizado!");
				break;
			
			} else if(opcao == 2) {
				if(empregados[i][12][estadoAtual] == null) {
					System.out.println("\nO empregado ainda nao lancou o cartao na entrada.");
					break;
				}
				System.out.println("\nInforme a hora de saida:");
				hora = teclado.nextInt();
				if(hora < Integer.parseInt(empregados[i][12][estadoAtual])) {
					System.out.println("\nO empregado nao pode sair antes de entrar!");
					break;
				} else {
					copiarEstado();
					empregados[i][13][estadoAtual] = String.valueOf(hora);
					System.out.println("\nLancamento realizado!");
					calcularSalarioHorista(i);
					break;
				}		
			}
		}
	}
	
	private static void calcularSalarioHorista(int i) {
		
		int tempo;
		double salarioAcumulado;
		
		tempo = Integer.parseInt(empregados[i][13][estadoAtual]) - Integer.parseInt(empregados[i][12][estadoAtual]);
		
		if(empregados[i][14][estadoAtual] == null) {
			if(tempo <= 8) {
				empregados[i][14][estadoAtual] = String.valueOf(tempo * Double.parseDouble(empregados[i][8][estadoAtual]));
			} else {
				empregados[i][14][estadoAtual] = String.valueOf(8 * Double.parseDouble(empregados[i][8][estadoAtual])
						+ (tempo - 8) * 1.5 * Double.parseDouble(empregados[i][8][estadoAtual]));
			}
		} else {
			salarioAcumulado = Double.parseDouble(empregados[i][14][estadoAtual]);
			if(tempo <= 8) {
				empregados[i][14][estadoAtual] = String.valueOf(tempo * Double.parseDouble(empregados[i][8][estadoAtual]) + salarioAcumulado);
			} else {
				empregados[i][14][estadoAtual] = String.valueOf(8 * Double.parseDouble(empregados[i][8][estadoAtual])
						+ (tempo - 8) * 1.5 * Double.parseDouble(empregados[i][8][estadoAtual]) + salarioAcumulado);
			}
		}
		
		empregados[i][12][estadoAtual] = null;
		empregados[i][13][estadoAtual] = null;

	}
	
 	private static void procurarComissionado() {
 		
 		String id;
 		
 		teclado = new Scanner(System.in);
 		
 		System.out.println("\nEntre com o ID do empregado:");
		id = teclado.nextLine();
		
		for(int i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] == null) {
				continue;
			}
			if(empregados[i][0][estadoAtual].equals(id) && !empregados[i][3][estadoAtual].equals("3")) {
				System.out.println("\nO empregado nao e comissionado!");
				return;
			} 
			if(empregados[i][0][estadoAtual].equals(id) && empregados[i][3][estadoAtual].equals("3")) {
				System.out.printf("\nEmpregado encontrado: %s\n", empregados[i][1][estadoAtual]);
				lancarResultadoDeVenda(i);
				return;
			}
		}
		System.out.println("\nEmpregado nao encontrado.");
		
 	}
 	
 	private static void lancarResultadoDeVenda(int i) {
 		
 		String valorDaVenda;
 		double salarioAcumulado;
 		
 		teclado = new Scanner(System.in);
 		
 		copiarEstado();
 		
 		while(true) {
 			System.out.println("\nInforme o valor da venda:");
 	 		valorDaVenda = teclado.nextLine();
 	 		if(Double.parseDouble(valorDaVenda) > 0) {
 	 			break;
 	 		}
 	 		System.out.println("\nValor invalido!");	
 		}
 		
 		if(empregados[i][14][estadoAtual] == null) {
 			empregados[i][14][estadoAtual] = String.valueOf(Double.parseDouble(valorDaVenda)
 					* Double.parseDouble(empregados[i][10][estadoAtual]) / 100);
 		} else {
 			salarioAcumulado = Double.parseDouble(empregados[i][14][estadoAtual]);
 			empregados[i][14][estadoAtual] = String.valueOf(Double.parseDouble(valorDaVenda)
 					* Double.parseDouble(empregados[i][10][estadoAtual]) / 100 + salarioAcumulado);
 		}
 	}

	private static void lancarTaxaDeServico() {
		
		String id;
		String taxaDeServico;
		double taxaAcumulada;
		
		teclado = new Scanner(System.in);
		
		System.out.println("\nEntre com o ID de sindicato do empregado:");
		id = teclado.nextLine();
		
		for(int i = 0; i < linhas; i++) {
			if(empregados[i][6][estadoAtual] == null) {
				continue;
			}
			
			if(empregados[i][6][estadoAtual].equals(id)) {
				
				copiarEstado();
				
				System.out.printf("\nEmpregado encontrado: %s\n", empregados[i][1][estadoAtual]);
				System.out.println("\nInforme a taxa de servico:");
				taxaDeServico = teclado.nextLine();
				
				while(Double.parseDouble(taxaDeServico) < 0) {
					System.out.println("\nValor invalido!");
					taxaDeServico = teclado.nextLine();
				}
				
				if(empregados[i][11][estadoAtual] == null) {
					empregados[i][11][estadoAtual] = taxaDeServico;
					
				} else {
					taxaAcumulada = Double.parseDouble(empregados[i][11][estadoAtual]);
					empregados[i][11][estadoAtual] = Double.toString(taxaAcumulada + Double.parseDouble(taxaDeServico));
				}
				
				System.out.println("\nLancamento realizado!");
				return;
			}
		}
		
		System.out.println("\nEmpregado nao encontrado!");
				
	}

	private static void alterarDetalhes() {
		
		int i;
		String id;
		String opcao;
		boolean encontrou = false;
		
		teclado = new Scanner(System.in);
		
		System.out.println("\nEntre com o ID do empregado:");
		id = teclado.nextLine();
		
		for(i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] == null) {
				continue;
			}
			if(empregados[i][0][estadoAtual].equals(id)) {
				System.out.printf("\nEmpregado encontrado: %s\n", empregados[i][1][estadoAtual]);
				encontrou = true;
				break;
			}
		}
		if(!encontrou) {
			System.out.println("\nEmpregado nao encontrado!");
			return;
		}
		
		System.out.println("\nAlterar:\n(1) Nome\t\t\t(5) Pertencer ao sindicato\n"
				+ "(2) Endereco\t\t\t(6) ID sindicato\n"
				+ "(3) Tipo\t\t\t(7) Taxa sindical\n"
				+ "(4) Metodo de pagamento\n");
		
		opcao = teclado.nextLine();
		
		if(opcao.equals("1")) {
			copiarEstado();
			inserirNome(i);
			
		} else if(opcao.equals("2")) {
			copiarEstado();
			inserirEndereco(i);
			
		} else if(opcao.equals("3")) {
			copiarEstado();
			inserirTipo(i);
			
		} else if(opcao.equals("4")) {
			copiarEstado();
			inserirMetodoDePagamento(i);
			
		} else if(opcao.equals("5")) {
			copiarEstado();
			pertencerSindicato(i);
			
		} else if(opcao.equals("6")) {
			alterarIdSindicato(i);
			
		} else if(opcao.equals("7")) {
			alterarTaxaSindical(i);
			
		} else {
			System.out.println("\nOpcao invalida!");
			
		}
		
		mostrarInfo(i);
	}
	
	private static void pertencerSindicato(int i) {
		
		String opcao;
		boolean aux = false;
		
		while(true) {
			System.out.println("\n(1) Fazer parte do sindicato (2) Nao fazer parte do sindicato");
			opcao= teclado.nextLine();
			
			if(opcao.equals("1") || opcao.equals("2")) {
				break;
			}
		}
		if(opcao.equals("2")) {
			empregados[i][5][estadoAtual] = "2";
			return;
		}
		
		empregados[i][5][estadoAtual] = "1";

		while(true) {
			System.out.println("\nEntre com o ID de sindicato do empregado:");
			empregados[i][6][estadoAtual] = teclado.nextLine();
			aux = true;
			
			if(empregados[i][6][estadoAtual].equals(empregados[i][0][estadoAtual])) {
				System.out.println("\nO ID no sindicato nao pode ser igual ao ID na empresa!");
				continue;
			}
			for(int j = 0; j < linhas; j++) {
				if(empregados[j][0][estadoAtual] != null) {
					if(empregados[i][6][estadoAtual].equals(empregados[j][6][estadoAtual]) && i != j) {
						System.out.println("\nJa existe um empregado com este ID!");
						aux = false;
						break;
					}
				}
			}
			if(aux) {
				break;
			}
		}

		System.out.println("\nTaxa sindical:");
		empregados[i][7][estadoAtual] = teclado.nextLine();

		while(Double.parseDouble(empregados[i][7][estadoAtual]) < 0 ) {
			System.out.println("\nEntre com um valor valido!");
			empregados[i][7][estadoAtual] = teclado.nextLine();
		}
	}
	
	private static void alterarIdSindicato(int i) {
		
		boolean aux;

		if(Integer.parseInt(empregados[i][5][estadoAtual]) != 1) {
			System.out.println("\nO empregado nao e sindicalista!");
			return;
		}
		
		copiarEstado();
				
		while(true) {
			System.out.println("\nEntre com o ID de sindicato do empregado:");
			empregados[i][6][estadoAtual] = teclado.nextLine();
			aux = true;
			
			if(empregados[i][6][estadoAtual].equals(empregados[i][0][estadoAtual])) {
				System.out.println("\nO ID no sindicato nao pode ser igual ao ID na empresa!");
				continue;
			}
			
			for(int j = 0; j < linhas; j++) {
				if(empregados[j][0][estadoAtual] != null) {
					if(empregados[i][6][estadoAtual].equals(empregados[j][6][estadoAtual]) && i != j) {
						System.out.println("\nJa existe um empregado com este ID!");
						aux = false;
						break;
					}
				}
			}
			
			if(aux) {
				break;
			}
		}
	}
	
	private static void alterarTaxaSindical(int i) {

		if(Integer.parseInt(empregados[i][5][estadoAtual]) != 1) {
			System.out.println("\nO empregado nao e sindicalista!");
			return;
		}
		
		copiarEstado();
		
		System.out.println("\nTaxa sindical:");
		empregados[i][7][estadoAtual] = teclado.nextLine();

		while(Double.parseDouble(empregados[i][7][estadoAtual]) < 0 ) {
			System.out.println("\nEntre com um valor valido!");
			empregados[i][7][estadoAtual] = teclado.nextLine();
		}
	}
	
	private static void rodarFolhaDePagamento() {
		
		if(cal[5][estadoAtual] == 1) {
			System.out.println("\nA folha ja foi rodada hoje!");
			return;
		}
		
		copiarEstado();
		
		boolean aux = false;
		double salario;
		
		for(int i = 0; i < linhas; i++) {
			
			salario = 0.0;
			
			if(empregados[i][0][estadoAtual] == null) {
				continue;
				
			} else if(empregados[i][15][estadoAtual].equals("1") && cal[0][estadoAtual] == 6) {
				if(empregados[i][14][estadoAtual] != null) {
					salario = Double.parseDouble(empregados[i][14][estadoAtual]);
					empregados[i][14][estadoAtual] = null;
				}
				if(cal[4][estadoAtual] == 4) {
					if(empregados[i][5][estadoAtual].equals("1") && empregados[i][7][estadoAtual] != null) {
						salario -= Double.parseDouble(empregados[i][7][estadoAtual]);
					}
				}
				aux = true;
				
			} else if(empregados[i][15][estadoAtual].equals("2") && eUltimoDiaUtil()) {
				salario = Double.parseDouble(empregados[i][9][estadoAtual]);
				if(empregados[i][5][estadoAtual].equals("1") && empregados[i][7][estadoAtual] != null) {
					salario -= Double.parseDouble(empregados[i][7][estadoAtual]);
				}
				aux = true;
				
			} else if(empregados[i][15][estadoAtual].equals("3") && cal[4][estadoAtual] % 2 == 0) {
				salario = Double.parseDouble(empregados[i][9][estadoAtual]) / 2;
				if(empregados[i][14][estadoAtual] != null) {
					salario += Double.parseDouble(empregados[i][14][estadoAtual]);
					empregados[i][14][estadoAtual] = null;
				}
				if(cal[4][estadoAtual] == 4) {
					if(empregados[i][5][estadoAtual].equals("1") && empregados[i][7][estadoAtual] != null) {
						salario -= Double.parseDouble(empregados[i][7][estadoAtual]);
					}
				}
				aux = true;
				
			} else {
				continue;
			}
			
			System.out.println("\nEmpregado: " + empregados[i][1][estadoAtual]);
			System.out.println("ID: " + empregados[i][0][estadoAtual]);
			
			if(empregados[i][5][estadoAtual].equals("1") && empregados[i][11][estadoAtual] != null) {
				salario -= Double.parseDouble(empregados[i][11][estadoAtual]);
				empregados[i][11][estadoAtual] = null;
			}
			
			System.out.println("Salario liquido pago: " + salario);
			
		}
		
		if(!aux) {
			System.out.println("\nNao ha pagamentos para a data de hoje.");
		}
		
		cal[5][estadoAtual] = 1;
	}
	
	private static boolean eUltimoDiaUtil() {
		
		if(cal[2][estadoAtual] == 1 || cal[2][estadoAtual] == 3 || cal[2][estadoAtual] == 5 || cal[2][estadoAtual] == 7 || cal[2][estadoAtual] == 8 || cal[2][estadoAtual] == 10 || cal[2][estadoAtual] == 12) {
			
			if(cal[1][estadoAtual] == 31 && (cal[0][estadoAtual] != 1 && cal[0][estadoAtual] != 7) || ((cal[1][estadoAtual] == 30 || cal[1][estadoAtual] == 29) && cal[0][estadoAtual] == 6)) {
				return true;
			}
		} else if(cal[2][estadoAtual] == 4 || cal[2][estadoAtual] == 6 || cal[2][estadoAtual] == 9 || cal[2][estadoAtual] == 11) {
			
			if(cal[1][estadoAtual] == 30 && (cal[0][estadoAtual] != 1 && cal[0][estadoAtual] != 7) || ((cal[1][estadoAtual] == 29 || cal[1][estadoAtual] == 28) && cal[0][estadoAtual] == 6)) {
				return true;
			}
		} else if(cal[2][estadoAtual] == 2 && eAnoBissexto()) {
			
			if(cal[1][estadoAtual] == 29 && (cal[0][estadoAtual] != 1 && cal[0][estadoAtual] != 7) || ((cal[1][estadoAtual] == 28 || cal[1][estadoAtual] == 27) && cal[0][estadoAtual] == 6)) {
				return true;
			}
		} else if(cal[2][estadoAtual] == 2 && !eAnoBissexto()) {
			
			if(cal[1][estadoAtual] == 28 && (cal[0][estadoAtual] != 1 && cal[0][estadoAtual] != 7) || ((cal[1][estadoAtual] == 27 || cal[1][estadoAtual] == 26) && cal[0][estadoAtual] == 6)) {
				return true;
			}
		}
		return false;
	}
	
	private static void desfazerRefazer() {
		
		int opcao;
		
		teclado = new Scanner(System.in);
		
		System.out.println("\n(1) Desfazer\t(2) Refazer");
		
		opcao = teclado.nextInt();
		
		if(opcao == 1 && estadoAtual > 0) {
			estadoAtual--;
			refazer = true;
			System.out.println("\nDesfeito!");
			
		} else if(opcao == 1 && estadoAtual == 0) {
			System.out.println("\nNao ha o que desfazer");
		
		} else if(opcao == 2 && refazer == true && estadoAtual < estadoMaximo) {
			estadoAtual++;
			System.out.println("\nRefeito");
			
		} else if(opcao == 2 && (refazer == false || estadoAtual == estadoMaximo)) {	
			System.out.println("\nNao ha o que refazer");
	
		}
		
	}
	
	private static void exibirAgenda() {
		
		boolean aux = false;
		
		for(int i = 0; i < linhas; i++) {
			
			if(empregados[i][15][estadoAtual] == null) {
				continue;
			}
			
			if(empregados[i][15][estadoAtual].equals("1")) {
				aux = true;
				System.out.println("\nID: " + empregados[i][0][estadoAtual]);
				System.out.println("Nome: " + empregados[i][1][estadoAtual]);
				System.out.println("Agenda: SEMANALMENTE");
				
			} else if(empregados[i][15][estadoAtual].equals("2")) {
				aux = true;
				System.out.println("\nID: " + empregados[i][0][estadoAtual]);
				System.out.println("Nome: " + empregados[i][1][estadoAtual]);
				System.out.println("Agenda: MENSALMENTE");				
				
			} else if(empregados[i][15][estadoAtual].equals("3")) {
				aux = true;
				System.out.println("\nID: " + empregados[i][0][estadoAtual]);
				System.out.println("Nome: " + empregados[i][1][estadoAtual]);
				System.out.println("Agenda: BI-SEMANALMENTE");

			}
		}
		
		if(!aux) {
			System.out.println("\nNao ha empregados no sistema!");
		}
		
	}
	
	private static void listarEmpregados() {
		
		String id;
		boolean aux = false;
		
		teclado = new Scanner(System.in);
		
		for(int i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] != null) {
				System.out.printf("\nNome: %s\nID: %s\n\n", empregados[i][1][estadoAtual], empregados[i][0][estadoAtual]);
				aux = true;
			}
		}
		
		if(!aux) {
			System.out.println("\nNao ha empregados no sistema!");
			return;
		}
		
		System.out.println("\nMostrar informacao de um empregado: (1) Sim (2) Nao");
		
		if(!teclado.nextLine().equals("1")) {
			return;
		}
		
		System.out.println("\nEntre com o ID:");
		id = teclado.nextLine();
		
		for(int i = 0; i < linhas; i++) {
			if(empregados[i][0][estadoAtual] != null) {
				if(empregados[i][0][estadoAtual].equals(id)) {
					mostrarInfo(i);
					return;
				}
			}
		}
		
		System.out.println("\nEmpregado nao encontrado!");
		
	}
	
	private static void mostrarInfo(int i) {
		
		System.out.printf("\nNome: %s\nID: %s\nEndereco: %s\n",
				empregados[i][1][estadoAtual], empregados[i][0][estadoAtual], empregados[i][2][estadoAtual]);
		if(empregados[i][3][estadoAtual].equals("1")) {
			System.out.println("Tipo: Horista");
			System.out.printf("Salario horario: %s\n", empregados[i][8][estadoAtual]);
			
		} else if(empregados[i][3][estadoAtual].equals("2")) {
			System.out.println("Tipo: Assalariado");
			System.out.printf("Salario mensal: %s\n", empregados[i][9][estadoAtual]);
			
		} else if(empregados[i][3][estadoAtual].equals("3")) {
			System.out.println("Tipo: Comissionado");
			System.out.printf("Salario mensal: %s\n", empregados[i][9][estadoAtual]);
			System.out.printf("Comissao: %s%%\n", empregados[i][10][estadoAtual]);
		}
		if(empregados[i][4][estadoAtual].equals("1")) {
			System.out.println("Forma de pagamento: Em mуos");
		} else if(empregados[i][4][estadoAtual].equals("2")) {
			System.out.println("Forma de pagamento: Correios");
		} else if(empregados[i][4][estadoAtual].equals("3")) {
			System.out.println("Forma de pagamento: Banco");
		}
		if(empregados[i][5][estadoAtual].equals("1")) {
			System.out.println("Sindicalista: Sim");
			System.out.printf("ID sindicato: %s\n", empregados[i][6][estadoAtual]);
			System.out.printf("Taxa sindical: %s\n", empregados[i][7][estadoAtual]);
		
		} else {
			System.out.println("Sindicalista: Nao");
		}
		System.out.println();
		
	}
	
	
}
