package arvore; 

import java.util.ArrayList;
import java.util.List;

import model.Conteudo;

public class No {  

    Conteudo conteudo;   
	List<No> filhos = new ArrayList<No>();

    public No(Conteudo palavra){  
        this.conteudo = palavra;  
    }  

    public Conteudo getConteudo() {
		return conteudo;
	}

	public void setConteudo(Conteudo conteudo) {
		this.conteudo = conteudo;
	}

	public List<No> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<No> filhos) {
		this.filhos = filhos;
	}
	
	public void addFilha(No no) {
		if(!filhos.contains(no)) {
			this.filhos.add(no);
		}
		else {
			System.out.println("pasta ou arquivo já existente!");
		}
	}
	
	public void removeFilha(No no) {
		this.filhos.remove(no);
	}
	
	public void imprimeFilhos() {
		for (No filho : filhos) {
			System.out.println(filho.toString()  + filho.getConteudo().getClass());
		}
	}

	@Override
	public String toString() {
		return conteudo.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		No other = (No) obj;
		if (conteudo == null) {
			if (other.conteudo != null)
				return false;
		} else if (!conteudo.equals(other.conteudo))
			return false;
		return true;
	}

	public void mostraNo() {          
    	System.out.print(conteudo.getNome());     
        System.out.print(", ");     
    }    
} 