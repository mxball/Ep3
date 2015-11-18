package br.usp.ep3.disco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class ParticaoDisco {

//	private int[] tabelaBlocos = new int[25000];
	private File arquivoBinario;
	private RandomAccessFile randomAccessFile;
	
	public ParticaoDisco() throws FileNotFoundException {
	}
	
	public void escreveBloco(byte[] conteudo, int posicaoConteudo) throws IOException {
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		int posicaoArquivo =  posicaoConteudo * 4000;
		randomAccessFile.seek(posicaoArquivo);
		randomAccessFile.write(conteudo);
		randomAccessFile.close();
	}

	public void escreveNoRoot(String nomeArquivo, Integer posicao) throws IOException {
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		randomAccessFile.seek(0);
//		while(true) {
//			byte[] lido = new byte[10];
//			randomAccessFile.read(lido);
//			int i = 0;
//			for (byte b : lido) {
//				if(b != -127) {
//					break;
//				}
//				i++;
//			}
//			if(i == 10) {
//				break;
//			}
//		}
		ByteBuffer buffer = ByteBuffer.allocate(10);
		for (byte b : nomeArquivo.getBytes()) {
			buffer.put(b);
		}
		byte[] bytesDaPosicao = get2BytesDaPosicao(posicao);
		buffer.put(8, bytesDaPosicao[0]);
		buffer.put(9, bytesDaPosicao[1]);
		randomAccessFile.write(buffer.array());
		randomAccessFile.close();
	}
	
	public void escreveDir(byte[] conteudo, int posicaoConteudo) throws IOException {
		arquivoBinario = new File("arquivoBinario2");
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		int posicaoArquivo =  1 * 4000;
		randomAccessFile.seek(posicaoArquivo);
		byte[] lido = new byte[4000];
		randomAccessFile.read(lido);
		randomAccessFile.write(conteudo);
		randomAccessFile.close();
	}

	public int leRoot(String nomeArquivo) throws IOException {
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		randomAccessFile.seek(0);
		byte[] lido = new byte[10];
		randomAccessFile.read(lido);
		
		byte[] nomeBytes = new byte[8];
		byte[] posicaoBytes = new byte[2];
		System.arraycopy(lido, 0, nomeBytes, 0, nomeBytes.length);
		System.arraycopy(lido, 8, posicaoBytes, 0, posicaoBytes.length);
		if(nomeArquivo.equals(new String(nomeBytes))) {
			return getPosicaoDe2Bytes(posicaoBytes);
		}
		return -1;
	}

	public String leBloco(int i) throws IOException {
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		randomAccessFile.seek(i*4000);
		byte[] b = new byte[4000];
		randomAccessFile.read(b);
		randomAccessFile.close();
		return new String(b);
	}

	private byte[] get2BytesDaPosicao(int posicao) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(posicao & 0xFF);
		bytes[1] = (byte)((posicao >> 8) & 0xFF);
		return bytes;
	}
	
	private int getPosicaoDe2Bytes(byte[] doisBytes) {
		int high = doisBytes[1] >= 0 ? doisBytes[1] : 256 + doisBytes[1];
		int low = doisBytes[0] >= 0 ? doisBytes[0] : 256 + doisBytes[0];
		int posicao = low | (high << 8);
		return  posicao;
	}
}
