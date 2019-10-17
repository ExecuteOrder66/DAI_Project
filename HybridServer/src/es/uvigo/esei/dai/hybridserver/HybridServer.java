package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HybridServer {
	private static final int SERVICE_PORT = 8888;
	private Thread serverThread;
	private boolean stop;
	private Controller controller;
	private ExecutorService threadPool; 
	private int numClients;
	private int port;
	private String dbUrl;
	private String dbUser;
	private String dbPassword;

	public HybridServer() {
		this.numClients = 50;
		this.port = SERVICE_PORT;
		this.dbUrl = "jdbc:mysql://localhost:3306/hstestdb";
		this.dbUser = "hsdb";
		this.dbPassword = "hsdbpass";
		//CONSTRUIR DBDAO
	}

	public HybridServer(Map<String, String> pages) {
		this.controller = new Controller(new MemoryDAO(pages));
	}

	public HybridServer(Properties properties) {
		this.numClients = Integer.parseInt(properties.getProperty("numClients"));
		this.port = Integer.parseInt(properties.getProperty("port"));
		this.dbUrl = properties.getProperty("db.url");
		this.dbUser = properties.getProperty("db.user");
		this.dbPassword = properties.getProperty("db.password");
		//CONSTRUIR DBDAO
	}

	public int getPort() {
		return this.port;
	}

	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(port)) {
					threadPool = Executors.newFixedThreadPool(numClients);
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop) break;
						//Asignacion cliente a un hilo
						threadPool.execute(new ServiceThread(socket, controller));		
					}
				} catch (IOException e) {
					//Capturar aqui las IOException? Si las capturamos aqui se rompe el bucle while true, pero 
					//si no, sera un bucle infinito
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
	}

	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		threadPool.shutdownNow();
		 
		try {
		  threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		  e.printStackTrace();
		}

		this.serverThread = null;
	}
}
