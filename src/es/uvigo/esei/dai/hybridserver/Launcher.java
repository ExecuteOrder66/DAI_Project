package es.uvigo.esei.dai.hybridserver;

public class Launcher {
	public static void main(String[] args) {
		new HybridServer().start();
		System.out.println("Servidor activo...");
	}
}
