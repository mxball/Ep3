package arvore;

import java.util.List;

import model.Conteudo;

public class Arvore {
	public static No raiz; // o único campo de dado em Arvore

	public Arvore() { // construtor
		raiz = null; // nenhum nó na arvore
	}

	public static No insere(Conteudo conteudo, No no) { // metodo insere
		if (no == null) {
			no = new No(conteudo); // se nao existir nó cria um novo
		}
		return no;
	}

	public static void caminhando(Arvore arv) {// caminha na arvore
		System.out.println("Pré-ordem: ");
		arv.preordem(Arvore.raiz);
	}

	public void preordem(No no) {// caminha em preordem
		if (no == null) {
			return;
		}
		no.mostraNo();
		List<No> filhos = no.getFilhos();
		for (No filho : filhos) {
			preordem(filho);
		}
	}
}
