package model;

import java.util.List;


public class Diretorio extends Conteudo{
	private List<Conteudo> conteudo;
	
	public Diretorio(String name) {
		this.nome = name;
	}

	public List<Conteudo> getConteudo() {
		return conteudo;
	}

	public void setConteudo(List<Conteudo> conteudo) {
		this.conteudo = conteudo;
	}
	
}
