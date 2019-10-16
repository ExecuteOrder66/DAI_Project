package es.uvigo.esei.dai.hybridserver;

import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;


public class Launcher {
	public static void main(String[] args) {
		Map<String, String> pages = new LinkedHashMap<String, String>();
		pages.put("6df1047e-cf19-4a83-8cf3-38f5e53f7725", "<html><head></head><body>This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.</body></html>");
		pages.put("79e01232-5ea4-41c8-9331-1c1880a1d3c2", "<html><head></head><body>This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2.</body></html>");
		pages.put("a35b6c5e-22d6-4707-98b4-462482e26c9e", "<html><head></head><body>This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e.</body></html>");
		pages.put("3aff2f9c-0c7f-4630-99ad-27a0cf1af137", "<html><head></head><body>This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137.</body></html>");
		pages.put("77ec1d68-84e1-40f4-be8e-066e02f4e373", "<html><head></head><body>This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373.</body></html>");
		pages.put("8f824126-0bd1-4074-b88e-c0b59d3e67a3", "<html><head></head><body>This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3.</body></html>");
		pages.put("c6c80c75-b335-4f68-b7a7-59434413ce6c", "<html><head></head><body>This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c.</body></html>");
		pages.put("f959ecb3-6382-4ae5-9325-8fcbc068e446", "<html><head></head><body>This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446.</body></html>");
		pages.put("2471caa8-e8df-44d6-94f2-7752a74f6819", "<html><head></head><body>This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819.</body></html>");
		pages.put("fa0979ca-2734-41f7-84c5-e40e0886e408", "<html><head></head><body>This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408.</body></html>");
		try {
			if(args.length == 0) {
				new HybridServer(pages).start();
				System.out.println("Servidor activo...");
			}else if(args.length == 1) {
				//El argumento recibido es la ruta del fichero de configuracion
				try {
					File fileConfig = new File(args[1]);
					FileInputStream inFile = new FileInputStream(fileConfig);
					
					Properties properties = new Properties();
					properties.load(inFile);	//Cargamos el fichero de configuracion
					
					new HybridServer(properties).start();	//Arrancamos servidor con propiedades
				} catch (FileNotFoundException eFile) {
					throw new FileNotFoundException("Archivo de configuracion no encontrado");
				} catch (IOException eIO) {
					throw new FileNotFoundException("Error al cargar el archivo de configuracion");
				}
			}else {
				throw new IllegalArgumentException("Numero de argumentos incorrecto");
			}	
			System.out.println("Servidor activo...");
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		
	}
}
