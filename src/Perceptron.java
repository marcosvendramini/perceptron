import java.util.*;
import java.io.*;

public class Perceptron {

	public static double b; // bias
	public static double y; // resposta
	public static boolean d; // desejado
	public static double e; // erro
	public static double t; // tx aprendizado
	public static double[] x; // entrada
	public static double[] w; // peso
	public static int m; // num entrada
	public static double n=1000; // tempo
	public static boolean te; // tem erro
	public static double ni; //contador de ciclos
	/**
	 * Gera a tabela verdade de n entradas para servir como massa de treino
	 * (mtreino) para o perceptron
	 **/
	public static boolean[][] mtreino(int k) {
		int mp = (int) Math.pow(2, k);
		boolean mat[][] = new boolean[mp][k];
		int aux;
		for (int i = 0; i < k; i++) { //gera a tabela verdade
			int count = 0;
			boolean val = false;
			aux = (int) Math.pow(2, i);
			for (int j = 0; j < mp; j++) {
				if (i == 0) {
					mat[j][i] = val;
					val = !val;
				} else if (count < aux) {
					mat[j][i] = val;
					count++;
				} else {
					val = !val;
					mat[j][i] = val;
					count = 1;
				}
			}
		}
		return mat;
	}

	/**
	 * Gera as saídas corretas de um and da tabela verdade para ser usada no treino
	 **/
	public static boolean[] andtreino(boolean[][] k, int a) {
		boolean resp = true;
		int tam = (int) Math.pow(2, m);
		boolean[] vet = new boolean[tam];
		for (int i = 0; i < tam; i++) {
			for (int j = 0; j < a; j++) {
				resp = resp & k[i][j]; //faz and entre as entradas
			}
			vet[i] = resp;
			resp = true;
		}
		return vet;
	}

	/**
	 * Gera as saídas corretas de um or da tabela verdade para ser usada no treino
	 **/
	public static boolean[] ortreino(boolean[][] k, int a) {
		boolean resp = false;
		int tam = (int) Math.pow(2, m);
		boolean[] vet = new boolean[tam];
		for (int i = 0; i < tam; i++) {
			for (int j = 0; j < a; j++) {
				resp = resp | k[i][j]; //faz or entre as entradas
			}
			vet[i] = resp;
			resp = false;
		}
		return vet;
	}

	/**
	 * Gera as saídas corretas de um xor da tabela verdade para ser usada no treino
    **/
	public static boolean[] xortreino(boolean[][] k, int a) {
		int tam = (int) Math.pow(2, m);
		boolean[] vet = new boolean[tam];
      int count=0;
		for (int i = 0; i < tam; i++) {
         for (int j = 0; j < a; j++) {
			   if(k[i][j]) count++; //conta o numero de entradas verdadeiras
         }
         if(count%2==0)
			vet[i] = false;
         else
			vet[i] = true;
         count=0;
		}
		return vet;
	}

	/**
	 * Treina o perceptron com a massa de teste e os resultados obtidos acima O
	 * perceptron para quando todas as entradas atingem o resultado esperado ou faz 1000 ciclos
	 **/
	public static void treino(boolean[][] matriz, boolean[] corretas) {
		do {
			te = false; //ponto de parada do treino
			for (int i = 0; i < matriz.length; i++) {
				for (int j = 1; j < x.length; j++) {
					x[j] = matriz[i][j - 1] ? 1 : 0;
				}
				d = corretas[i];
				int aux = d ? 1 : 0;
				y = 0;
				for (int p = 0; p < x.length; p++) {
					y = y + (x[p] * w[p]); //soma as entradas e multiplica pelo peso (bias posicao 0 de x e w)
				}
				if (y > 0) //limiar
					y = 1;
				else
					y = 0;
				e = aux - y; //calculo do erro
				if (e == 0) {
				} else {
					te = true;
					for (int p = 0; p < x.length; p++) {
						w[p] = w[p] + (e * t * x[p]); //calculo novo peso
					}
				}
			}
			ni++;
		} while (te&&ni<n);
	}

	/**
	 * Executa o perceptron com a entrada escolhida e printa a resposta na tela
	 **/
	public static void rodar(double[] entrada) {
		double resp = 0;
		for (int i = 0; i < entrada.length; i++) {
			resp = resp + (entrada[i] * w[i + 1]); //soma das entrada * peso
		}
		resp = resp + x[0] * w[0]; // soma o bias * peso no que teve de cima
		if (resp > 0) //limiar
			resp = 1;
		else
			resp = 0;
		System.out.println("resposta: " + resp);
	}

	public static void main(String[] args) {
		System.out.println("Perceptron - Marcos Felipe Vendramini Carvalho\n" + "Exemplo de entrada:\n" + "3\n"
				+ "0.2\n" + "and\n" + "1\n" + "0\n" + "1\n"
				+ "Sendo o primeiro o número de entrada(int) o segundo a taxa de aprendizagem(double), a operação desejada seguida pelas entradas\n"
				+ "Pode colocar várias entradas no mesmo arquivo, mas todas tem q seguir o padrao acima\n"
				+ "O treino para depois de 1000 ciclos\n");
		Scanner sc = new Scanner(System.in);
		try {
			sc = new Scanner(new FileReader("C:\\Users\\Marcos\\Downloads\\entrada.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNext()) {
			ni=0;
			m = sc.nextInt();
			x = new double[m + 1];
			w = new double[m + 1];
			for (int i = 0; i < m + 1; i++)
				w[i] = Math.random(); // gera pesos randoms
			b = -1; //bias
			x[0] = b;
			t = sc.nextDouble();
			String tipoop = sc.next(); 
			char type = tipoop.charAt(0);
			boolean tm[][];
			boolean rm[];
			tm = mtreino(m); //gera massa de treino
			if (type == 'a') //escolhe qual operação será feita
				rm = andtreino(tm, m);
			else if (type == 'o')
				rm = ortreino(tm, m);
			else
				rm = xortreino(tm, m);
			treino(tm, rm);
			System.out.println("Número de entrada: " + m + " Taxa: " + t + " Operação: " + tipoop + " ciclos de treino: "+ni+ " entradas: ");
			double[] entradatxt = new double[m];
			for (int i = 0; i < m; i++) { 
				entradatxt[i] = sc.nextDouble();
				System.out.println(entradatxt[i]);
			}
			rodar(entradatxt); //executa o teste com a entrada
		}
		sc.close();
	}
}
