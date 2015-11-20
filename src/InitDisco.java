

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class InitDisco {

	public static void main(String[] args) throws IOException {
		OutputStream writer = new FileOutputStream("arquivoBinario");
		byte vazio = -127;
		for (int i = 0; i < 100000000; i++) {
			writer.write(vazio);
		}
		writer.close();
	}
	
}
