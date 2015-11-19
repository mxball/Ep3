package model;

import java.io.IOException;
import java.util.Scanner;

import br.usp.ep3.Bitmap;
import br.usp.ep3.disco.ParticaoDisco;
import br.usp.ep3.disco.Superblock;
import br.usp.ep3.exceptions.SemEspacoException;
import br.usp.ep3.memoria.Fat;

public class testa {
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws SemEspacoException
	 */
	public static void main(String[] args) throws IOException, SemEspacoException {
		
		ParticaoDisco disco = null;
		Superblock superblock = null;
		Fat fat = null;
		Bitmap bitmap = null;
		int inicializado = 0;
		
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
					inicializado  = disco.getNovo();
					if(inicializado == 0) {
						disco.inicializaBitmap();
						fat.inicializaFat();
					}
					break;
				case "unmount":;
					break;
				case "mkdir":
					disco.criaDiretorio(strings[1]);
					int buscaPosicaoDiretorio = disco.buscaPosicaoDiretorio(strings[1]);
					System.out.println(buscaPosicaoDiretorio);
					System.out.println(buscaPosicaoDiretorio * 4000);
					break;
				case "rmdir":
					break;
				case "imprime":
					bitmap.imprimeBitmap();
					break;
				case "cp":
					fat.armazenaArquivo(strings[1], strings[2],bitmap);
					break;
				case "cat":
					fat.buscaArquivo(strings[1]);
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
					superblock.salvaSuperblock(disco);
					fat.escreveFat();
					return;
				default:
					break;
			}
		}
	}

}
