package model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import arvore.Arvore;
import arvore.No;

public class testa {
	
	public static void main(String[] args) throws IOException {
//		new File("teste2").mkdir();
//		Gerenciador gerenciador = new Gerenciador("teste");
//		gerenciador.println("Testando");
//		gerenciador.close();
		
		FileOutputStream os = new FileOutputStream("arquivo");
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		Arvore arvore = new Arvore();
		Diretorio root = new Diretorio("root");
		No no = new No(root);
		arvore.raiz = no;
		arvore.posicaoAtual = no;
		No pai = no;
		
		while(true) {
			System.out.print("[ep3]:");
			Scanner sc = new Scanner(System.in);
			String string = sc.nextLine();
			String[] strings = string.split(" ");
			switch (strings[0]) {
				case "mount":
					bw.write("teste\n");
					bw.write("teste");
					System.out.println("sistema criado");
					break;
				case "unmount":;
					break;
				case "mkdir":
					arvore.inserePasta(arvore, no, strings);
					break;
				case "rmdir":
					arvore.removeDiretorio(arvore, no, pai, strings);
					break;
				case "imprime":
					arvore.printArvore();
					break;
				case "cp":	
				case "cat":	
				case "touch":	
				case "rm":	
				case "ls":	
				case "find":	
				case "df":
				case "sai":
					break;
				default:
					break;
			}
		}
	}

}
