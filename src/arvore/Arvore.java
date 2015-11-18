package arvore;

import java.util.List;

import model.Arquivo;
import model.Conteudo;
import model.Diretorio;

public class Arvore {
	public No raiz; // o único campo de dado em Arvore
	public No posicaoAtual;
	
	public Arvore() { // construtor
		raiz = null; // nenhum nó na arvore
	}

	public static No insere(Conteudo conteudo, No no) { // metodo insere
		if (no == null) {
			no = new No(conteudo); // se nao existir nó cria um novo
		}
		return no;
	}

	public void caminhando() {// caminha na arvore
		System.out.println("Pré-ordem: ");
		this.preordem(raiz);
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
	
	public void printArvore() {
		printArvore(this.raiz, 0);
	}

	private void printArvore(No no, int i) {
		if(no == null) {
			return;
		}
		for (int j = 0; j < i; j++) {
			System.out.print('*');
		}
		System.out.println(no + "Filho: " + no.getFilhos());
		for (No filho : no.getFilhos()) {
			printArvore(filho, i+1);
		}
	}

	public No getPosicaoAtual() {
		return posicaoAtual;
	}

	public void setPosicaoAtual(No posicaoAtual) {
		this.posicaoAtual = posicaoAtual;
	}

	public void inserePasta(No no, String[] strings) {
		String[] split = strings[1].split("/");
		String nomePasta = split[split.length-1];
		No localDeInsercao = achaLocal(split);
		Diretorio novo = new Diretorio(nomePasta);
		localDeInsercao.addFilha(new No(novo));
	}
	
	private No achaLocal(String[] split) {
		int t = split.length;
		int i = 0;
		No posicao = raiz; 
		while(i < t-1) {
			List<No> filhos = posicao.getFilhos();
			for (No filho : filhos) {
				if(filho.getConteudo().getNome().compareTo(split[i]) == 0) {
					posicao = filho;
				}
			}
			i++;
		}
		return posicao;
	}
	
	public void removeDiretorio(No no, No pai, String[] strings) {
		No pasta = this.achaPastaARemover(strings[1], no, pai);
		this.removeDiretorio(pasta);
		pai.removeFilha(pasta);
	}
	
	private No achaPastaARemover(String string, No posicao, No pai) {
		String[] split = string.split("/");
		int t = split.length;
		int i = 0;
		while(i < t) {
			List<No> filhos = posicao.getFilhos();
			for (No filho : filhos) {
				if(filho.getConteudo().getNome().compareTo(split[i])==0) {
					pai = posicao;
					posicao = filho;
				}
			}
			i++;
		}
		return posicao;
	}
		
	public void removeDiretorio(No no) {// caminha em preordem
		if (no == null) {
			return;
		}
		List<No> filhos = no.getFilhos();
		for (No filho : filhos) {
			preordem(filho);
			System.out.println("Removido:" + filho.getConteudo().getNome());
		}
	}

	public void listaPasta(String caminho) {
		String[] split = caminho.split("/");
		No pasta = achaLocal(split);
		List<No> filhos = pasta.getFilhos();
		pasta.imprimeFilhos();
	}

	public void insereArquivo(String caminho) {
		String[] split = caminho.split("/");
		No localDeInsercao = achaLocal(split);
		localDeInsercao.addFilha(new No(new Arquivo(split[split.length -1])));
	}

	public void busca(String caminho, String arquivo) {
		No pasta = findLocal(caminho.split("/"));
		procura(pasta, pasta.getConteudo().getNome(), arquivo);
	}
	
	private No findLocal(String[] split) {
		int t = split.length;
		int i = 0;
		No posicao = raiz; 
		while(i < t) {
			List<No> filhos = posicao.getFilhos();
			for (No filho : filhos) {
				if(filho.getConteudo().getNome().compareTo(split[i]) == 0) {
					posicao = filho;
				}
			}
			i++;
		}
		return posicao;
	}
	
	public void procura(No no, String caminho, String arquivo) {
		if (no == null) {
			return;
		}
		if(no.getConteudo().getClass().equals(Arquivo.class) &&
				no.getConteudo().getNome().equals(arquivo)) {
			System.out.println(caminho);
		}
		List<No> filhos = no.getFilhos();
		for (No filho : filhos) {
			System.out.println("Procurando no caminho:" + caminho);
			procura(filho, caminho + "/" + filho.getConteudo().getNome(), arquivo);
		}
	}
}
