package br.usp.ep3.memoria;

import java.io.FileInputStream;
import java.io.IOException;

import br.usp.ep3.Bitmap;
import br.usp.ep3.disco.ParticaoDisco;
import br.usp.ep3.exceptions.SemEspacoException;

public class Fat {

	private int[] tabelaFat = new int[25000];
	private ParticaoDisco particaoDisco;
	
	
	public Fat(ParticaoDisco particao) {
		//constroi a tabela de blocos
		for (int i = 0; i < tabelaFat.length; i++) {
			this.tabelaFat[i] = -1;
		}
		this.particaoDisco = particao;
	}
	
	public void armazenaArquivo(String nomeArquivo, Bitmap bitmap) throws SemEspacoException, IOException {
		FileInputStream inputStream = new FileInputStream(nomeArquivo);
		int posicaoAnterior = -1;
		byte[] bloco = new byte[4000];
		int numeroBytes = inputStream.read(bloco);
		int primeiro = -1;
		while(numeroBytes > 0) {
			int posicaoLivre = bitmap.procuraPosicaoLivre();
			if(primeiro == -1) {
				primeiro = posicaoLivre;
			}
			this.particaoDisco.escreveBloco(bloco, posicaoLivre);
			bitmap.ocupaPosicao(posicaoLivre);
			if(posicaoAnterior != -1) {
				this.tabelaFat[posicaoAnterior] = posicaoLivre;
			}
			posicaoAnterior = posicaoLivre;
			numeroBytes = inputStream.read(bloco);
		}
		this.particaoDisco.escreveNoRoot(nomeArquivo, primeiro);
		inputStream.close();
	}

	public void buscaArquivo(String string) throws IOException {
		String i = this.particaoDisco.leRoot(string);
		this.particaoDisco.leBloco(1);
	}
}
