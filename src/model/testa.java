package model;

import java.io.IOException;
import java.util.Scanner;

import br.usp.ep3.Bitmap;
import br.usp.ep3.disco.ParticaoDisco;
import br.usp.ep3.disco.Superblock;
import br.usp.ep3.exceptions.SemEspacoException;
import br.usp.ep3.memoria.Fat;

public class testa {
	
	public static void main(String[] args) throws IOException, SemEspacoException {
		
		ParticaoDisco disco;
		Superblock superblock;
		Fat fat = null;
		Bitmap bitmap = null;
		boolean inicializado = false;
		
		while(true) {
			System.out.print("[ep3]:");
			Scanner sc = new Scanner(System.in);
			String string = sc.nextLine();
			String[] strings = string.split(" ");
			switch (strings[0]) {
				case "mount":
					disco = new ParticaoDisco(strings[1]);
					superblock = new Superblock();
					superblock.setInfoSuperblock(disco);
					fat = new Fat(disco);
					bitmap = new Bitmap(disco);
					inicializado = true;
					break;
				case "unmount":;
					break;
				case "mkdir":
					break;
				case "rmdir":
					break;
				case "imprime":
					break;
				case "cp":
					fat.armazenaArquivo("dem2.txt", bitmap);
					break;
				case "cat":
//					fat.buscaArquivo(strings[1]);
					break;
				case "touch":
//					arvore.insereArquivo(strings[1]);
					break;
				case "rm":
					break;
				case "ls":
					if(strings.length < 2) {
					}
					else{
					}
					break;
				case "find":
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
