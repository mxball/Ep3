

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Fat {

	private int[] tabelaFat = new int[25000];
	private ParticaoDisco particaoDisco;
	
	public Fat(ParticaoDisco particao) throws IOException {
		this.particaoDisco = particao;
		if(particao.isNovo()) {
			for (int i = 0; i < tabelaFat.length; i++) {
				this.tabelaFat[i] = -1;
			}
		} else {
			leFat();
		}
	}
	
	public void armazenaArquivo(String origem, String destino, Bitmap bitmap, Superblock superblock) throws SemEspacoException, IOException {
		FileInputStream inputStream = new FileInputStream(new File(origem));
		int tamanhoEmBytes = inputStream.available(); //NÃO MOVA ISSO DE LUGAR 
		int posicaoAnterior = -1;
		byte[] bloco = new byte[4000];
		int numeroBytes = inputStream.read(bloco);
		int primeiro = -1;
		superblock.aumentaTamanhoDesperdicado(4-(numeroBytes%4));
		
		String[] diretorios = destino.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length - 1; i++) {
			path += "/" + diretorios[i];
			System.out.println(diretorios[i]);
		}
		int posicaoBlocoPai = particaoDisco.buscaPosicaoDiretorio(path);
		
		while(numeroBytes > 0) {
			int posicaoLivre = bitmap.procuraPosicaoLivreArquivo();
			if(primeiro == -1) {
				primeiro = posicaoLivre;
			}
			this.particaoDisco.escreveBloco(bloco, posicaoLivre);
			bitmap.ocupaPosicao(posicaoLivre);
			if(posicaoAnterior != -1) {
				this.tabelaFat[posicaoAnterior] = posicaoLivre;
			}
			posicaoAnterior = posicaoLivre;
			bloco = new byte[4000];
			numeroBytes = inputStream.read(bloco);
		}
		this.particaoDisco.guardaNoDiretorio(diretorios[diretorios.length-1], primeiro, tamanhoEmBytes, posicaoBlocoPai);
		this.tabelaFat[posicaoAnterior] = -2;
		inputStream.close();
	}
	
	public void armazenaArquivoVazio(String destino, Bitmap bitmap, int posicaoBlocoPai) throws SemEspacoException, IOException {
		byte[] bloco = new byte[4000];
		int posicaoLivre = bitmap.procuraPosicaoLivreArquivo();
		this.particaoDisco.escreveBloco(bloco, posicaoLivre);
		bitmap.ocupaPosicao(posicaoLivre);
		this.tabelaFat[posicaoLivre] = -2;
		this.particaoDisco.guardaNoDiretorio(destino, posicaoLivre, 0, posicaoBlocoPai);
	}
	
	public void removeArquivo(String destino) throws IOException {
		String[] diretorios = destino.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length - 1; i++) {
			path += "/" + diretorios[i];
		}
		int posicaoBlocoPai = particaoDisco.buscaPosicaoDiretorio(path);
		int i = this.particaoDisco.tiraArquivoDoDiretorio(diretorios[diretorios.length-1], posicaoBlocoPai);
		while(this.tabelaFat[i] != -2) {
			int j = this.tabelaFat[i];
			i = this.tabelaFat[i];
			this.particaoDisco.removeDoBitmap(i);
			i = j;
			this.particaoDisco.limpaBloco(j);
		}
		this.tabelaFat[i] = -1;
		this.particaoDisco.removeDoBitmap(i);
	}


	public void buscaArquivo(String string) throws IOException {
		String[] diretorios = string.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length - 1; i++) {
			path += "/" + diretorios[i];
		}
		int posicaoBlocoPai = particaoDisco.buscaPosicaoDiretorio(path);
		int i = this.particaoDisco.posicaoArquivo(diretorios[diretorios.length-1], posicaoBlocoPai);
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.particaoDisco.leBloco(i));
		while(this.tabelaFat[i] != -2) {
			i = this.tabelaFat[i];
			buffer.append(this.particaoDisco.leBloco(i));
		}
		System.out.println(buffer.toString());
	}
	

	public void inicializaFat() throws IOException {
		particaoDisco.randomAccessFile.seek(12000);
		byte[] b = new byte[52000];
		b[0] = (byte) (b[0] | 15);
		for (int i = 1; i < 52000; i++) {
			b[i] = 0;
		}
		particaoDisco.randomAccessFile.write(b);
	}
	
	public void escreveFat() throws IOException {
		for (int i = 0; i < 25000; i++) {
			particaoDisco.randomAccessFile.seek(8000 + i*2);
			byte[] bytesDaPosicao = particaoDisco.get2BytesDaPosicao(i);
			particaoDisco.randomAccessFile.write(bytesDaPosicao);
		}
	}
	
	public void leFat() throws IOException {
		particaoDisco.randomAccessFile.seek(12000);
		for (int i = 0; i < 25000; i++) {
			byte[] b = new byte[2];
			particaoDisco.randomAccessFile.read(b);
			tabelaFat[i] = particaoDisco.getPosicaoDe2Bytes(b);
		}
	}

	public void mostraDiretorio(String string) throws IOException {
		String[] diretorios = string.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length; i++) {
			path += "/" + diretorios[i];
		}
		int posicaoBlocoPai = particaoDisco.buscaPosicaoDiretorio(path);
		particaoDisco.listaConteudoDiretorio(posicaoBlocoPai);
	}

	public void touch(String string, Fat fat, Bitmap bitmap, Superblock superblock) throws IOException, SemEspacoException {
		String[] diretorios = string.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length-1; i++) {
			path += "/" + diretorios[i];
		}
		int posicaoBlocoPai = particaoDisco.buscaPosicaoDiretorio(path);
		this.particaoDisco.modificaDataAcesso(posicaoBlocoPai, diretorios[diretorios.length - 1], fat, bitmap);
		superblock.aumentaTamanhoDesperdicado(4);
	}

}
