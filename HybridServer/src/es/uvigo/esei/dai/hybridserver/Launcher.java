package es.uvigo.esei.dai.hybridserver;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Launcher {
	//http://localhost:8888/html?
	public static void main(String[] args) {

		if (args.length == 0) {
			new HybridServer().start();
			System.out.println("Servidor activo...");
		} else if (args.length == 1) {
			Properties prop = new Properties();
			// cargar las propiedades del fichero properties
			try (InputStream input = new FileInputStream(args[0])) {
				prop.load(input);
				new HybridServer(prop).start();
				System.out.println("Servidor activo...");
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("Error al cargar el fichero de configuracion");
			}
		} else {
			System.err.println("Error en el numero de argumentos");
		}
	}
}
