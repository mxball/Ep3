package br.usp.ep3.disco;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ParticaoDisco {

//	private int[] tabelaBlocos = new int[25000];
	private File arquivoBinario;
	public RandomAccessFile randomAccessFile;
	private boolean ehNovo = false;/*0 novo 1 existente*/
	private Superblock superblock;
	
	public ParticaoDisco(String nomeDoSistema, Superblock superblock) throws IOException {
		this.superblock = superblock;
		arquivoBinario = new File(nomeDoSistema);
		if(!arquivoBinario.exists()) { 
			ehNovo = true;
		}
		randomAccessFile = new RandomAccessFile(arquivoBinario, "rw");
		superblock.setInfoSuperblock(this);
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
		System.out.println(posicaoConteudo);
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

	public byte[] get2BytesDaPosicao(int posicao) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(posicao & 0xFF);
		bytes[1] = (byte)((posicao >> 8) & 0xFF);
		return bytes;
	}
	
	public int getPosicaoDe2Bytes(byte[] doisBytes) {
		int high = doisBytes[1] >= 0 ? doisBytes[1] : 256 + doisBytes[1];
		int low = doisBytes[0] >= 0 ? doisBytes[0] : 256 + doisBytes[0];
		int posicao = low | (high << 8);
		return  posicao;
	}

	public void fechaArquivo() throws IOException {
		randomAccessFile.close();
	}

	public boolean isNovo() {
		return ehNovo;
	}

	public int leBitmap() throws IOException {
		randomAccessFile.seek(4000);
		for (int i = 0; i < 4000; i++) {
			byte[] b = new byte[1];
			randomAccessFile.read(b);
			for(int j = 0; j < 8; j++) {
				int bit = getBit(b[0], j);
				if(bit == 0) {
					return (i)*8 + j;
				}
			}
		}
		return -1;
	}
	
	public void inicializaBitmap() throws IOException {
		randomAccessFile.seek(4000);
		byte[] b = new byte[3125];
		b[0] = (byte) (b[0] | -1);
		b[1] = (byte) (b[1] | -1);
		for (int i = 16; i < 3125; i++) {
			b[i] = 0;
		}
		randomAccessFile.write(b);
	}
	
	public int procuraBitmapArquivo() throws IOException {
		for (int i = 3124; i >= 0; i--) {
			randomAccessFile.seek(4000 + i);
			byte[] b = new byte[1];
			randomAccessFile.read(b);
			for (int j = 7; j >= 0; j--) {
				int bit = getBit(b[0], j);
				if(bit == 0) {
					return (i)*8 + j;
				}
			}
		}
		return -1;
	}
	
	/*Falta preencher o bit certo, ja estou no lugar certo*/
	public void ocupaBitmap(int posicao) throws IOException {
		randomAccessFile.seek(4000);
		int seekbit = 3999;
		int resto = posicao % 8;
		int conta = posicao/8;
		byte[] b = new byte[1];
		for (int i = 0;i <= conta; i++) {
			b = new byte[1];
			randomAccessFile.read(b);
			seekbit++;
		}
		int x = (int) Math.pow(2, resto);
		System.out.println("conta = " + conta);
		System.out.println("resto = " + resto);
		System.out.println("seekbit = " + seekbit);
		System.out.println("x = " + x);
		int bit = b[0] | x;
		b[0] = (byte) bit;
		System.out.println("Posicao bit depois: " + bit);
		System.out.println(Arrays.toString(b));
		randomAccessFile.seek(seekbit);
		randomAccessFile.write(b);
	}

	public void criaDiretorio(String caminhoCompleto) throws IOException {
		String[] diretorios = caminhoCompleto.split("/");
		String path = "";
		for (int i = 1; i < diretorios.length - 1; i++) {
			path += "/" + diretorios[i];
		}
		int posicaoBlocoPai = buscaPosicaoDiretorio(path);
		randomAccessFile.seek(posicaoBlocoPai * 4000);
		byte[] conteudo = new byte[10];
		randomAccessFile.read(conteudo);
		int novaPosicao = posicaoBlocoPai  * 4000;
		while(!isConteudoVazio(conteudo)){
			System.out.println("AQUI");
			novaPosicao += 10;
			conteudo = new byte[10];
			randomAccessFile.read(conteudo);
		}
		ByteBuffer buffer = ByteBuffer.allocate(10);
		for (byte b : diretorios[diretorios.length - 1].getBytes()) {
			buffer.put(b);
		}
		int posicaoBloco = leBitmap();
		System.out.println("BLOCO " + posicaoBloco);
		System.out.println(posicaoBloco * 4000);
		byte[] bytesDaPosicao = get2BytesDaPosicao(posicaoBloco);
		buffer.put(8, bytesDaPosicao[0]);
		buffer.put(9, bytesDaPosicao[1]);
		ocupaBitmap(posicaoBloco);
		superblock.incrementaNumeroDiretorios();
		System.out.println("Escrevendo " + novaPosicao);
		randomAccessFile.seek(novaPosicao);
		randomAccessFile.write(buffer.array());
	}
	

	private boolean isConteudoVazio(byte[] conteudo) {
		for (int i = 0; i < conteudo.length; i++) {
			if(conteudo[i] != 0) {
				return false;
			}
		}
		return true;
	}

	public int buscaPosicaoDiretorio(String caminho) throws IOException {
		String[] diretorios = caminho.split("/");
		int posicaoByte = 60000;
		randomAccessFile.seek(posicaoByte);
		byte[] conteudo = new byte[10];
		int indiceDiretorio = 1;
		while(indiceDiretorio < diretorios.length) {
			System.out.println("aqui" + indiceDiretorio);
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			byte[] posicaoBytes = new byte[2];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			System.arraycopy(conteudo, 8, posicaoBytes, 0, posicaoBytes.length);
			if(diretorios[indiceDiretorio].equals(new String(nomeBytes).trim())) {
				indiceDiretorio++;
				posicaoByte = getPosicaoDe2Bytes(posicaoBytes) * 4000;
				System.out.println("ACHEI " + posicaoByte);
				randomAccessFile.seek(posicaoByte);
			}
			conteudo = new byte[10];
		}
		int posicaoBloco = posicaoByte / 4000;
		return posicaoBloco;
	}

	public void imprimeBitmap() throws IOException {
		randomAccessFile.seek(4000);
		byte[] b = new byte[3125];
		randomAccessFile.read(b);
		for (int i = 0; i < 3125; i++) {
		   for(int  j = 0; j < 8;j++) {
			   int a = (i*8)+j;
			   System.out.print("bit[" + a + "] : " + getBit(b[i], j) + " ");
		   }
		   System.out.println();
		}
	}
	
	public int posicaoArquivo(String nomeDoArquivo, int blocoPai) throws IOException {
		randomAccessFile.seek(blocoPai*4000);
		System.out.println(blocoPai);
		while(true) {
			byte[] conteudo = new byte[10];
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			byte[] posicaoBytes = new byte[2];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			System.arraycopy(conteudo, 8, posicaoBytes, 0, posicaoBytes.length);
			if(nomeDoArquivo.equals(new String(nomeBytes).trim())) {
				return getPosicaoDe2Bytes(posicaoBytes);
			}
			System.out.println(new String(nomeBytes));
		}
	}

	private static int getBit(byte b, int posicao) {  
	    int mascara = 1 << posicao;  
	    int retorno = b & mascara;  
	    return retorno != 0 ? 1 : 0;  
	}  
	
	public static void main(String[] args) {  
//	   byte b = 0 ;
//	   byte	 c = (byte) (b | 128);
//	   for(int  i = 0; i < 8;i++) {
//		   System.out.println(getBit(c, i));
//	   }
		String teste = "/teste";
		System.out.println(teste.split("/").length);
	}

	public void guardaNoDiretorio(String nomeArquivo, int primeiro,
			int blocoPai) throws IOException {
		randomAccessFile.seek(blocoPai*4000);
		System.out.println(blocoPai);
		int i = 0;
		while(true) {
			byte[] conteudo = new byte[10];
			randomAccessFile.read(conteudo);
			if(isConteudoVazio(conteudo)) {
				byte[] nomeBytes = nomeArquivo.getBytes();
				byte[] posicaoBytes = get2BytesDaPosicao(primeiro);
				System.arraycopy(nomeBytes, 0, conteudo, 0, nomeBytes.length);
				System.arraycopy(posicaoBytes, 0, conteudo, 8, posicaoBytes.length);
				System.out.println(new String(nomeBytes));
				randomAccessFile.seek(blocoPai*4000 + i);
				randomAccessFile.write(conteudo);
				return;
			}
			i = i+ 10;
		}
	}
}
