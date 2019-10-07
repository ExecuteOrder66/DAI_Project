package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HybridServer {
	private static final int SERVICE_PORT = 8888;
	private final int MAX_THREADS = 50;
	private Thread serverThread;
	private boolean stop;
	Controller controller;

	public HybridServer() {
		// TODO Auto-generated constructor stub
	}

	public HybridServer(Map<String, String> pages) {
		// TODO Auto-generated constructor stub
		this.controller = new Controller(new MemoryDAO(pages));
	}

	public HybridServer(Properties properties) {
		// TODO Auto-generated constructor stub
	}

	public int getPort() {
		return SERVICE_PORT;
	}

	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {
					ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
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

		this.serverThread = null;
	}
}
