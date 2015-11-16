package model;

public class Bloco {
	private int tamanhoTotal = 4;
	private int espacoLivre;
	private int espacoOcupado;
	private int proximoBloco;
	
	public int getTamanhoTotal() {
		return tamanhoTotal;
	}

	public void setEspaco(int t) {
		if(t > 4) {
			System.out.println("Erro");
		}
		else {
			espacoOcupado = t;
			espacoLivre = 4 - t;
		}
	}
	 
	
}
