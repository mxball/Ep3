package model;

import java.io.FileNotFoundException;

import arvore.Arvore;
import arvore.No;

public class testa {
	public static void main(String[] args) throws FileNotFoundException {
//		new File("teste2").mkdir();
//		Gerenciador gerenciador = new Gerenciador("teste");
//		gerenciador.println("Testando");
//		gerenciador.close();
		
			Arvore arvore = new Arvore();
			Diretorio root = new Diretorio("root");
			No no = new No(root);
			arvore.raiz = no;
			Diretorio home = new Diretorio("home");
			no.addFilha(new No(home));
			no.addFilha(new No(new Arquivo("teste")));
			arvore.preordem(no);
	}
}
