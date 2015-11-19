package br.usp.ep3.disco;

import java.io.IOException;


public class Superblock {
	
	private int inicioBitmap = 1;
	private int incioFat = 2;
	private int inicioRoot = 15;
	private int tamanhoOcupado;
	private int numedoDiretorios;
	private int numeroArquivos;
	private int espacoDesperdicado;
	
	public int getInicioBitmap() {
		return inicioBitmap;
	}
	public int getIncioFat() {
		return incioFat;
	}
	public int getInicioRoot() {
		return inicioRoot;
	}
	
	public int getTamanhoOcupado() {
		return tamanhoOcupado;
	}
	public void setTamanhoOcupado(int tamanhoOcupado) {
		this.tamanhoOcupado = tamanhoOcupado;
	}
	public int getNumedoDiretorios() {
		return numedoDiretorios;
	}
	public void setNumedoDiretorios(int inumedoDiretorios) {
		this.numedoDiretorios = inumedoDiretorios;
	}
	public int getNumeroArquivos() {
		return numeroArquivos;
	}
	public void setNumeroArquivos(int numeroArquivos) {
		this.numeroArquivos = numeroArquivos;
	}
	public int getEspacoDesperdicado() {
		return espacoDesperdicado;
	}
	public void setEspacoDesperdicado(int espacoDesperdicado) {
		this.espacoDesperdicado = espacoDesperdicado;
	}
	public void setInfoSuperblock(ParticaoDisco disco) throws IOException {
		if(disco.getNovo() == 1) {
			String string = disco.leBloco(0);
			String[] split = string.split(" ");

			setNumedoDiretorios(Integer.parseInt(split[3]));
			setNumeroArquivos(Integer.parseInt(split[5]));
			setEspacoDesperdicado(Integer.parseInt(split[7]));
		}
		else {
			setTamanhoOcupado(0);
			setNumedoDiretorios(0);
			setNumeroArquivos(0);
			setEspacoDesperdicado(0);
		}
		
	}
	
	public void salvaSuperblock(ParticaoDisco disco) throws IOException {
		String superBlock = "tamanhoOcupado " + getTamanhoOcupado() +
					   " numeroDiretorios " + getNumedoDiretorios() + 
					   " numeroArquivos " + getNumeroArquivos() + 
					   " espacoDisperdicado " + getEspacoDesperdicado()
					   + " ";
		byte[] conteudo = superBlock.getBytes();
		disco.escreveBloco(conteudo, 0);
	}
}