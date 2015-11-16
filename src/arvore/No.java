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
		this.filhos.add(no);
	}

	public void mostraNo() {          
    	System.out.print(conteudo.getNome());     
        System.out.print(", ");     
    }    
} 