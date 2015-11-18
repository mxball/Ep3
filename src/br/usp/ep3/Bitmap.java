package br.usp.ep3;

import br.usp.ep3.exceptions.SemEspacoException;

public class Bitmap {

	private final int tamanhoMaximo = 4000;

	private int[] posicaoLivre = new int[32000];
	
	public int getTamanhoMaximo() {
		return tamanhoMaximo;
	}
	
	public int procuraPosicaoLivre() throws SemEspacoException {
		for (int posicao : posicaoLivre) {
			if(posicao == 0) {
				return posicao;
			}
		}
		throw new SemEspacoException();
	}
	
	public void ocupaPosicao(int posicao) {
		this.posicaoLivre[posicao] = 1;
	}
	
}
