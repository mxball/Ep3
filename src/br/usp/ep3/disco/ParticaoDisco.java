package br.usp.ep3.disco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParticaoDisco {

//	private int[] tabelaBlocos = new int[25000];
	private File arquivoBinario;
	private RandomAccessFile randomAccessFile;
	private int novo = 0;/*0 novo 1 existente*/
	
	public ParticaoDisco(String nomeDoSistema) throws IOException {
		Path path = Files.createTempFile(nomeDoSistema,"");
		Files.exists(path);
		File file = new File(nomeDoSistema);
		if(file.exists()) {
			novo = 1;
		}
		arquivoBinario = file;
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
	}
	
	public void escreveBloco(byte[] conteudo, int posicaoConteudo) throws IOException {
		int posicaoArquivo =  posicaoConteudo * 4000;
		randomAccessFile.seek(posicaoArquivo);
		randomAccessFile.write(conteudo);
	}

	public void escreveNoRoot(String nomeArquivo, Integer posicao) throws IOException {
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
	}
	
	public void escreveDir(byte[] conteudo, int posicaoConteudo) throws IOException {
		int posicaoArquivo =  1 * 4000;
		randomAccessFile.seek(posicaoArquivo);
		byte[] lido = new byte[4000];
		randomAccessFile.read(lido);
		randomAccessFile.write(conteudo);
	}

	public int leRoot(String nomeArquivo) throws IOException {
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
		randomAccessFile.seek(i*4000);
		byte[] b = new byte[4000];
		randomAccessFile.read(b);
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

	public void fechaArquivo() throws IOException {
		randomAccessFile.close();
	}

	public int getNovo() {
		return novo;
	}

	public int leBitmap() throws IOException {
		randomAccessFile.seek(4000);
		for (int i = 1; i < 50000; i++) {
			byte[] b = new byte[1];
			randomAccessFile.read(b);
			for(int j = 0; j < 8; j++) {
				int bit = getBit(b[0], j);
				if(bit == 0) {
					return (i-1)*8 + j+1;
				}
			}
		}
		return -1;
	}
	
	public int escreveBitmap() throws IOException {
		randomAccessFile.seek(4000);
		for (int i = 1; i < 6250; i++) {
			byte[] b = new byte[1];
			randomAccessFile.read(b);
			for(int j = 0; j < 8; j++) {
				int bit = getBit(b[0], j);
				if(bit == 0) {
					return (i-1)*8 + j+1;
				}
			}
		}
		return -1;
	}
	/*Falta preencher o bit certo, ja estou no lugar certo*/
	public int ocupaBitmap(int posicao) throws IOException {
		randomAccessFile.seek(4000);
		int resto = posicao % 8;
		int conta = posicao/8;
		int i = 1;
		byte[] b = new byte[1];
		for (;i <= conta; i++) {
			b = new byte[1];
			randomAccessFile.read(b);
		}
		for(int j = 0; j < resto; j++) {
			int bit = getBit(b[0], j);
				if(bit == 0) {
					return (i-1)*8 + j+1;
			}
		}
		return -1;
	}
	
	
	private int getBit(byte b, int posicao) {  
	    int mascara = 1 << posicao;  
	    int retorno = b & mascara;  
	      
	    return retorno != 0 ? 1 : 0;  
	}  

}