package model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import arvore.Arvore;
import arvore.No;
import br.usp.ep3.Bitmap;
import br.usp.ep3.disco.ParticaoDisco;
import br.usp.ep3.exceptions.SemEspacoException;
import br.usp.ep3.memoria.Fat;

public class testa {
	
	public static void main(String[] args) throws IOException, SemEspacoException {
//		new File("teste2").mkdir();
//		Gerenciador gerenciador = new Gerenciador("teste");
//		gerenciador.println("Testando");
//		gerenciador.close();
		
		FileOutputStream os = new FileOutputStream("arquivo");
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		
		ParticaoDisco disco = new ParticaoDisco();
		Fat fat = new Fat(disco);
		Bitmap bitmap = new Bitmap();
		
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
					arvore.inserePasta(no, strings);
					break;
				case "rmdir":
					arvore.removeDiretorio(no, pai, strings);
					break;
				case "imprime":
					arvore.printArvore();
					break;
				case "cp":
					fat.armazenaArquivo("teste.txt", bitmap);
					break;
				case "cat":
				case "touch":
					arvore.insereArquivo(strings[1]);
					break;
				case "rm":
					arvore.removeDiretorio(no, pai, strings);
					break;
				case "ls":
					if(strings.length < 2) {
						arvore.printArvore();
					}
					else{
						arvore.listaPasta(strings[1]);
					}
					break;
				case "find":
					arvore.busca(strings[1], strings[2]);
					break;
				case "df":
				case "sai":
					break;
				default:
					break;
			}
		}
	}

}
