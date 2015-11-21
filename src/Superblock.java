

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
		if(disco.isNovo()) {
			setTamanhoOcupado(0);
			setNumedoDiretorios(0);
			setNumeroArquivos(0);
			setEspacoDesperdicado(0);
		}
		else {
			String string = disco.leBloco(0);
			String[] split = string.split(" ");

			setNumedoDiretorios(Integer.parseInt(split[3]));
			setNumeroArquivos(Integer.parseInt(split[5]));
			setEspacoDesperdicado(Integer.parseInt(split[7]));
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
	
	public void incrementaNumeroDiretorios() {
		this.numedoDiretorios++;
	}

	public void incrementaNumeroArquivos() {
		this.numeroArquivos++;
	}
	
	public void decrementaNumeroArquivos() {
		this.numeroArquivos--;
	}

	public void getInfo() {
		System.out.println("Numero de arquivos: " + this.numeroArquivos);
		System.out.println("Numero de diretorios: " + this.numedoDiretorios);
		System.out.println("Espaco livre: " + (250000 - this.tamanhoOcupado)*4 + "kb");
		System.out.println("Espaco desperdicado: " + this.espacoDesperdicado);
	}
	
	public void aumentaTamanhoOcupado() {
		this.tamanhoOcupado++;
	}

	public void decrementaTamanhoOcupado() {
		this.tamanhoOcupado--;
	}
	
	public void aumentaTamanhoDesperdicado(int i) {
		this.espacoDesperdicado +=i;
	}
	
	public void decrementaTamanhoDesperdicado(int i) {
		this.espacoDesperdicado -=i;
	}
}