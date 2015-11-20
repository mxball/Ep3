

import java.io.IOException;

public class Bitmap {

	private final int tamanhoMaximo = 4000;

	private ParticaoDisco disco;
	
	public Bitmap(ParticaoDisco disco) {
		this.disco = disco;
	}
	
	public int getTamanhoMaximo() {
		return tamanhoMaximo;
	}
	
	public int procuraPosicaoLivre() throws SemEspacoException, IOException {
		int bitmap = disco.leBitmap();
		return bitmap;
	}
	
	public int procuraPosicaoLivreArquivo() throws SemEspacoException, IOException {
		int bitmap = disco.procuraBitmapArquivo();
		return bitmap;
	}
	
	public void ocupaPosicao(int posicao) throws IOException {
		disco.ocupaBitmap(posicao);
	}
	
	public void imprimeBitmap() throws IOException {
		disco.imprimeBitmap();
	}
	
}
