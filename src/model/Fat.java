package model;

import java.util.List;

import arvore.Arvore;

public class Fat {
	
	private Arvore arvore;
	private int tamanhoTotal;
	private int tamanhoLivre;
	private Bloco[] blocos;
	
	public Fat() {
		arvore = new Arvore();
		blocos = new Bloco[2500];
	}
	
	public void fillBlocos(int tamanho) {
		if(tamanhoLivre < tamanho) {
			System.out.println("Erro: disco cheio");
		}
		else {
			int blocos = tamanho/4;
			int resto = tamanho % 4;
		}
	}
	

}
