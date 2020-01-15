package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.configuration.Configuration;
import es.uvigo.esei.dai.hybridserver.configuration.XMLConfigurationLoader;

import java.io.File;

public class Launcher {
	// http://localhost:xxxx/html?
	public static void main(String[] args) {
		if (args.length == 0) {
			new HybridServer().start();
			System.out.println("Servidor activo...");
		} else if (args.length == 1) {
			//Cargar fichero properties
			/*Properties prop = new Properties();
			try (InputStream input = new FileInputStream(args[0])) {
				prop.load(input);
				new HybridServer(prop).start();
				System.out.println("Servidor activo...");
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("Error al cargar el fichero de configuracion");
			}*/
			
			//Cargar fichero configuration
			XMLConfigurationLoader configuration = new XMLConfigurationLoader();
			try {
				Configuration config = configuration.load(new File(args[0]));
				new HybridServer(config).start();
				System.out.println("Servidor activo...");
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Error al cargar el fichero de configuracion");
			}
		} else {
			System.err.println("Error en el numero de argumentos");
		}
	}
}
