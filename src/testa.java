

import java.io.IOException;
import java.util.Scanner;

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
		while(true) {
			System.out.print("[ep3]:");
			Scanner sc = new Scanner(System.in);
			String string = sc.nextLine();
			String[] strings = string.split(" ");
			switch (strings[0]) {
				case "mount":
					superblock = new Superblock();
					disco = new ParticaoDisco(strings[1], superblock);
					fat = new Fat(disco);
					bitmap = new Bitmap(disco);
					if(disco.isNovo()) {
						disco.inicializaBitmap();
						fat.inicializaFat();
					}
					break;
				case "unmount":
					superblock.salvaSuperblock(disco);
					fat.escreveFat();
					disco.fechaArquivo();
					break;
				case "mkdir":
					disco.criaDiretorio(strings[1]);
					break;
				case "rmdir":
					disco.removeDiretorio(strings[1], fat);
					break;
				case "cp":
					fat.armazenaArquivo(strings[1], strings[2],bitmap, superblock);
					superblock.incrementaNumeroArquivos();
					break;
				case "cat":
					fat.buscaArquivo(strings[1]);
					break;
				case "touch":
					fat.touch(strings[1],fat,bitmap, superblock);
					break;
				case "rm":
					fat.removeArquivo(strings[1]);
					superblock.decrementaNumeroArquivos();
					break;
				case "ls":
					fat.mostraDiretorio(strings[1]);
				case "find":
					break;
				case "df":
					superblock.getInfo();
					break;
				case "sai":
					return;
				default:
					break;
			}
		}
	}

}
