package model;

import java.math.BigDecimal;

public class Arquivo extends Conteudo{
	private BigDecimal tamanho;
	
	public Arquivo(String name) {
		this.nome = name;
	}
	
	public BigDecimal getTamanho() {
		return tamanho;
	}

	public void setTamanho(BigDecimal tamanho) {
		this.tamanho = tamanho;
	}
}
