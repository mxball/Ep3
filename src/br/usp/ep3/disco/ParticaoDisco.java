package br.usp.ep3.disco;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

import br.usp.ep3.memoria.Fat;

public class ParticaoDisco {

	private File arquivoBinario;
	public RandomAccessFile randomAccessFile;
	private boolean ehNovo = false;/*0 novo 1 existente*/
	private Superblock superblock;
	private final int tamanhoEmBytesMaximoElementoNoDiretorio = 32;
	
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
		int bit = b[0] | x;
		b[0] = (byte) bit;
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
		byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
		randomAccessFile.read(conteudo);
		int novaPosicao = posicaoBlocoPai  * 4000;
		while(!isConteudoVazio(conteudo)){
			novaPosicao += tamanhoEmBytesMaximoElementoNoDiretorio;
			conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.read(conteudo);
		}
		
		byte[] novoDiretorio = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
		byte[] nomeEmBytes = diretorios[diretorios.length - 1].getBytes();
		System.arraycopy(nomeEmBytes, 0, novoDiretorio, 0, (nomeEmBytes.length < 8) ? nomeEmBytes.length : 8);
		novoDiretorio[8] = -1;
		novoDiretorio[9] = -1;
		novoDiretorio[10] = -1; 
		novoDiretorio[11] = -1;
		Calendar hoje = Calendar.getInstance();
		byte[] data = getBytesDaData(hoje);
		System.arraycopy(data, 0, novoDiretorio, 12, 6);
		System.arraycopy(data, 0, novoDiretorio, 18, 6);
		System.arraycopy(data, 0, novoDiretorio, 24, 6);		
		int posicaoBloco = leBitmap();
		byte[] bytesDaPosicao = get2BytesDaPosicao(posicaoBloco);
		System.arraycopy(bytesDaPosicao, 0, novoDiretorio, 30, 2);
		
		ocupaBitmap(posicaoBloco);
		superblock.incrementaNumeroDiretorios();
		randomAccessFile.seek(novaPosicao);
		randomAccessFile.write(novoDiretorio);
	}
	

	private byte[] getBytesDaData(Calendar data) {
		byte[] dataEmByte = new byte[6]; 
		dataEmByte[0] = formataInteiroParaNBytes(data.get(Calendar.DAY_OF_MONTH), 1)[0];
		dataEmByte[1] = formataInteiroParaNBytes(data.get(Calendar.MONTH) + 1, 1)[0];
		byte[] ano = formataInteiroParaNBytes(data.get(Calendar.YEAR), 2);
		dataEmByte[2] = ano[0];
		dataEmByte[3] = ano[1];
		dataEmByte[4] = formataInteiroParaNBytes(data.get(Calendar.HOUR_OF_DAY), 1)[0];
		dataEmByte[5] = formataInteiroParaNBytes(data.get(Calendar.MINUTE), 1)[0];
		return dataEmByte;
	}
	
	public byte[] formataInteiroParaNBytes(int inteiro, int numeroBytes) {
		byte[] bytes = new byte[numeroBytes];
		for (int i = 0; i < numeroBytes; i++) {
			bytes[i] = (byte)((inteiro >> (8 * i)) & 0xFF);
			
		}
		return bytes;
	}
	
	public int formataNBytesInteiro(byte[] bytes) {
		int inteiro = 0;
		for (int i = 0; i < bytes.length; i++) {
			int valor = bytes[i] >= 0 ? bytes[i] : 256 + bytes[i];
			inteiro = inteiro | (valor << (8 * i));
		}
		return inteiro;
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
		byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
		int indiceDiretorio = 1;
		while(indiceDiretorio < diretorios.length) {
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			byte[] posicaoBytes = new byte[2];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			System.arraycopy(conteudo, 30, posicaoBytes, 0, posicaoBytes.length);
			if(diretorios[indiceDiretorio].equals(new String(nomeBytes).trim())) {
				indiceDiretorio++;
				posicaoByte = getPosicaoDe2Bytes(posicaoBytes) * 4000;
				randomAccessFile.seek(posicaoByte);
			}
			conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
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
		while(true) {
			byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			byte[] posicaoBytes = new byte[2];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			System.arraycopy(conteudo, 30, posicaoBytes, 0, posicaoBytes.length);
			if(nomeDoArquivo.equals(new String(nomeBytes).trim())) {
				return getPosicaoDe2Bytes(posicaoBytes);
			}
		}
	}
	
	public int tiraArquivoDoDiretorio(String nomeDoArquivo, int blocoPai) throws IOException {
		randomAccessFile.seek(blocoPai*4000);
		System.out.println(blocoPai);
		System.out.println(nomeDoArquivo);
		int seek = blocoPai*4000;
		while(true) {
			byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			byte[] posicaoBytes = new byte[2];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			System.arraycopy(conteudo, 8, posicaoBytes, 0, posicaoBytes.length);
			if(nomeDoArquivo.equals(new String(nomeBytes).trim())) {
				int posicaoDe2Bytes = getPosicaoDe2Bytes(posicaoBytes);
				randomAccessFile.seek(seek);
				byte[] b = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
				randomAccessFile.write(b);
				return posicaoDe2Bytes;
			}
			seek+=tamanhoEmBytesMaximoElementoNoDiretorio;
		}
	}

	private static int getBit(byte b, int posicao) {  
	    int mascara = 1 << posicao;  
	    int retorno = b & mascara;  
	    return retorno != 0 ? 1 : 0;  
	}  
	
	public void guardaNoDiretorio(String nomeArquivo, int posicaoPrimeiroBloco, int tamanhoArquivo, int blocoPai) throws IOException {
		randomAccessFile.seek(blocoPai*4000);
		int i = 0;
		while(true) {
			byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.read(conteudo);
			if(isConteudoVazio(conteudo)) {
				byte[] nomeBytes = nomeArquivo.getBytes();
				System.arraycopy(nomeBytes, 0, conteudo, 0, (nomeBytes.length < 8) ? nomeBytes.length : 8);
				byte[] tamanho = formataInteiroParaNBytes(tamanhoArquivo, 4);
				System.arraycopy(tamanho, 0, conteudo, 8, 4);
				Calendar hoje = Calendar.getInstance();
				byte[] data = getBytesDaData(hoje);
				System.arraycopy(data, 0, conteudo, 12, 6);
				System.arraycopy(data, 0, conteudo, 18, 6);
				System.arraycopy(data, 0, conteudo, 24, 6);		
				byte[] posicaoBytes = get2BytesDaPosicao(posicaoPrimeiroBloco);
				System.arraycopy(posicaoBytes, 0, conteudo, 30, 2);
				randomAccessFile.seek(blocoPai*4000 + i);
				randomAccessFile.write(conteudo);
				return;
			}
			i = i + tamanhoEmBytesMaximoElementoNoDiretorio;
		}
	}

	public void listaConteudoDiretorio(int posicaoDaPasta) throws IOException {
		randomAccessFile.seek(posicaoDaPasta*4000);
		byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
		for (int i = 0; i < 4000; i += tamanhoEmBytesMaximoElementoNoDiretorio) {
			randomAccessFile.read(conteudo);
			if(!isConteudoVazio(conteudo)) {
				byte[] nomeBytes = new byte[8];
				System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
				byte[] tamanhoEmBytes = new byte[4];
				System.arraycopy(conteudo, 8, tamanhoEmBytes, 0, tamanhoEmBytes.length);
				byte[] tempoCriacao = new byte[6];
				System.arraycopy(conteudo, 12, tempoCriacao, 0, tempoCriacao.length);
				byte[] tempoModificacao = new byte[6];
				System.arraycopy(conteudo, 18, tempoModificacao, 0, tempoModificacao.length);
				byte[] tempoUltimoAcesso = new byte[6];
				System.arraycopy(conteudo, 24, tempoUltimoAcesso, 0, tempoUltimoAcesso.length);
				StringBuilder builder = new StringBuilder();
				if(isDiretorio(conteudo)) {
					builder.append("/");
				}
				builder.append(new String(nomeBytes).trim());
				builder.append(" ");
				if(!isDiretorio(conteudo)) {
					builder.append(formataNBytesInteiro(tamanhoEmBytes));
					builder.append(" ");
				}
				builder.append(getDataFormatada(tempoCriacao));
				builder.append(" ");
				builder.append(getDataFormatada(tempoModificacao));
				builder.append(" ");
				builder.append(getDataFormatada(tempoUltimoAcesso));
				System.out.println(builder);
			}
			conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
		}
	}

	private boolean isDiretorio(byte[] conteudo) {
		return conteudo[8] == -1 && conteudo[9] == -1 && conteudo[10] == -1 && conteudo[11] == -1;
	}

	private String getDataFormatada(byte[] tempoCriacao) {
		byte[] dia = new byte[1];
		System.arraycopy(tempoCriacao, 0, dia, 0, dia.length);
		byte[] mes = new byte[1];
		System.arraycopy(tempoCriacao, 1, mes, 0, mes.length);
		byte[] ano = new byte[2];
		System.arraycopy(tempoCriacao, 2, ano, 0, ano.length);
		byte[] hora = new byte[1];
		System.arraycopy(tempoCriacao, 4, hora, 0, hora.length);
		byte[] minuto = new byte[1];
		System.arraycopy(tempoCriacao, 5, minuto, 0, minuto.length);
		return formataNBytesInteiro(dia) + "/" + formataNBytesInteiro(mes) + "/" + formataNBytesInteiro(ano) +
			" " + formataNBytesInteiro(hora) + ":" + formataNBytesInteiro(minuto);
	}
	public void limpaBloco(int i) throws IOException {
		randomAccessFile.seek(i*4000);
		byte[] b = new byte[4000];
		randomAccessFile.write(b);
	}

	public void removeDiretorio(String caminhoCompleto, Fat fat) throws IOException {
		String[] diretorios = caminhoCompleto.split("/");
		String pai = "";
		for (int i = 1; i < diretorios.length - 1; i++) {
			pai += "/" + diretorios[i];
		}
		int posicaoPai = buscaPosicaoDiretorio(pai) * 4000;
		randomAccessFile.seek(posicaoPai);
		for (int i = 0; i < 4000; i+= tamanhoEmBytesMaximoElementoNoDiretorio) {
			byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			if(diretorios[diretorios.length - 1].equals(new String(nomeBytes).trim())){
				removeTodoConteudoDiretorio(caminhoCompleto, fat);
				randomAccessFile.seek(posicaoPai + i);
				randomAccessFile.write(new byte[tamanhoEmBytesMaximoElementoNoDiretorio]);
				return;
			}
		}
	}
		
	private void removeTodoConteudoDiretorio(String caminhoCompleto, Fat fat) throws IOException {
		int posicaoBytes = buscaPosicaoDiretorio(caminhoCompleto) * 4000;
		for (int i = 0; i < 4000; i+= tamanhoEmBytesMaximoElementoNoDiretorio) {
			byte[] conteudo = new byte[tamanhoEmBytesMaximoElementoNoDiretorio];
			randomAccessFile.seek(posicaoBytes);
			randomAccessFile.read(conteudo);
			byte[] nomeBytes = new byte[8];
			System.arraycopy(conteudo, 0, nomeBytes, 0, nomeBytes.length);
			if(!isConteudoVazio(conteudo)){
				if(isDiretorio(conteudo)) {
					removeDiretorio(caminhoCompleto + "/" + new String(nomeBytes).trim(), fat);
				} else {
					//ARRUMAR O REMOVE ARQUIVO
					fat.removeArquivo(caminhoCompleto + "/" + new String(nomeBytes).trim());
				}
			}
			//ATUALIZAR O BITMAP PARA LIBERAR O ESPACO
			randomAccessFile.seek(posicaoBytes);
			randomAccessFile.write(new byte[tamanhoEmBytesMaximoElementoNoDiretorio]);
			posicaoBytes +=  tamanhoEmBytesMaximoElementoNoDiretorio;
		}
	}

	public void removeDoBitmap(int posicao) throws IOException {
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
		int bit = b[0] ^ x;
		b[0] = (byte) bit;
		randomAccessFile.seek(seekbit);
		randomAccessFile.write(b);
	}
	
	public static void main(String[] args) {  
		byte b = 121;
		byte c = (byte) (b ^ 32);
		for (int i = 0; i < 8; i++) {
			System.out.println(getBit(c, i));
		}
	}  
}
