package model;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Gerenciador extends PrintStream{
	private static OutputStream file;

	public Gerenciador(String nome) throws FileNotFoundException {
		super(nome);
	}

	public static OutputStream getFile() {
		return file;
	}

	public static void setFile(OutputStream file) {
		Gerenciador.file = file;
	}


}
