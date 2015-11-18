package br.usp.ep3.disco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ParticaoDisco {

//	private int[] tabelaBlocos = new int[25000];
	private File arquivoBinario;
	private RandomAccessFile randomAccessFile;
	
	public ParticaoDisco() throws FileNotFoundException {
		arquivoBinario = new File("arquivoBinario");
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
	}
	
	public void escreveBloco(byte[] conteudo, int posicaoConteudo) throws IOException {
		int posicaoArquivo = posicaoConteudo * 4000;
		randomAccessFile.write(conteudo, 0, posicaoArquivo);
	}
	
}
