package br.usp.ep3.memoria;

import java.io.FileInputStream;
import java.io.IOException;

import br.usp.ep3.Bitmap;
import br.usp.ep3.disco.ParticaoDisco;
import br.usp.ep3.exceptions.SemEspacoException;

public class Fat {

	private int[] tabelaFat = new int[25000];
	private ParticaoDisco particaoDisco;
	
	
	public Fat(ParticaoDisco particao) throws IOException {
		this.particaoDisco = particao;
		if(particao.getNovo() == 0) {
			for (int i = 0; i < tabelaFat.length; i++) {
				this.tabelaFat[i] = -1;
			}
		} else {
			leFat();
		}
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
			}else {
				System.out.println("Proxima:" + posicaoLivre);
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
		this.tabelaFat[posicaoAnterior] = -2;
		System.out.println("Escrevento " + nomeArquivo + " " + primeiro);
		this.particaoDisco.escreveNoRoot(nomeArquivo, primeiro);
		inputStream.close();
	}

	public void buscaArquivo(String string) throws IOException {
		int i = this.particaoDisco.leRoot(string);
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
			particaoDisco.randomAccessFile.seek(12000 + i*2);
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

}
