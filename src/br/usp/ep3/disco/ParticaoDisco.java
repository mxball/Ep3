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
		ByteBuffer buffer = ByteBuffer.allocate(12);
		for (byte b : nomeArquivo.getBytes()) {
			buffer.put(b);
		}
		buffer.put(posicao.toString().getBytes());
		System.out.println(new String(buffer.array()));
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

	public String leRoot(String string) throws IOException {
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		randomAccessFile.seek(0);
		byte[] lido = new byte[12];
		randomAccessFile.read(lido);
		System.out.println("Lido ==== " + new String(lido));
		ByteBuffer buffer = ByteBuffer.allocate(8);
		for (int i = 0; i < 8; i++) {
			buffer.put(lido[i]);
		}
		System.out.println(new String(buffer.array()));
		if(string.equals(new String(buffer.array()))) {
			ByteBuffer posicao = ByteBuffer.allocate(1);
			posicao.put(lido[8]);
			return new String(posicao.array());
		}
		return "-1";
	}

	public String leBloco(int i) throws IOException {
		System.out.println(i);
		arquivoBinario = new File("arquivoBinario2");	
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		randomAccessFile.seek(1*4000);
		byte[] b = new byte[4000];
		randomAccessFile.read(b);
		randomAccessFile.close();
		return new String(b);
	}
	
}
