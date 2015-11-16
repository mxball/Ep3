package model;


public abstract class Conteudo {
	@Override
	public String toString() {
		return "Conteudo [nome=" + nome + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conteudo other = (Conteudo) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}



	public String nome;
	
	public int criado;
	
	public int modificado;
	
	public int acessado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCriado() {
		return criado;
	}

	public void setCriado(int criado) {
		this.criado = criado;
	}

	public int getModificado() {
		return modificado;
	}

	public void setModificado(int modificado) {
		this.modificado = modificado;
	}

	public int getAcessado() {
		return acessado;
	}

	public void setAcessado(int acessado) {
		this.acessado = acessado;
	}
	
}
