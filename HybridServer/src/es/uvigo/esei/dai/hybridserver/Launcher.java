package es.uvigo.esei.dai.hybridserver;

import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;


public class Launcher {
	public static void main(String[] args) {
		
		if(args.length == 0 ) {
			new HybridServer().start();
		}else if(args.length == 1) {		
			Properties prop = new Properties();
			//cargar las propiedades del fichero properties
			try (InputStream input = new FileInputStream(args[0])) {
				prop.load(input);
				
				new HybridServer(prop).start();
				System.out.println("Servidor activo...");
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("Error al cargar el fichero de configuracion");
			}

		}else {
			System.err.println("Error en el numero de argumentos");
		}
	}
}
