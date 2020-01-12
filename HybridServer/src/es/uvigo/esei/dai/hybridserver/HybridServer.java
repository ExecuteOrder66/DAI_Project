package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.hybridserver.controller.Controller;
import es.uvigo.esei.dai.hybridserver.model.dao.DAO;
import es.uvigo.esei.dai.hybridserver.model.dao.DBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.MemoryDAO;
import es.uvigo.esei.dai.hybridserver.ws.HybridServerServiceImpl;

public class HybridServer {
	private int service_port = 8888;
	private int max_thread = 10;
	private Thread serverThread;
	private ExecutorService threadPool;
	private boolean stop;
	private String webService = null;
	private List<ServerConfiguration> servers;
	private Controller controller;
	private DAO dao;

	private Endpoint endPoint;

	public HybridServer() {
		this.controller = new Controller(new DBDAO("jdbc:mysql://localhost:3306/hstestdb", "hsdb", "hsdbpass"));
	}

	public HybridServer(Map<String, String> pages) {
		this.service_port = 8888;
		this.max_thread = 50;
		this.controller = new Controller(new MemoryDAO(pages));
	}

	public HybridServer(Properties properties) {
		this.max_thread = Integer.parseInt(properties.getProperty("numClients"));
		this.service_port = Integer.parseInt(properties.getProperty("port"));
		this.controller = new Controller(new DBDAO(properties.getProperty("db.url"), properties.getProperty("db.user"),
				properties.getProperty("db.password")));
	}

	public HybridServer(Configuration config) {
		this.max_thread = config.getNumClients();
		this.service_port = config.getHttpPort();
		this.dao = new DBDAO(config.getDbURL(), config.getDbUser(), config.getDbPassword());
		this.controller = new Controller(this.dao, config.getServers());
		this.webService = config.getWebServiceURL();
		this.servers = config.getServers();
	}

	public int getPort() {
		return service_port;
	}

	public void start() {
		if (this.webService != null) {
			endPoint = Endpoint.publish(this.webService, new HybridServerServiceImpl(this.dao));
		}
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(service_port)) {
					threadPool = Executors.newFixedThreadPool(max_thread);
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop)
							break;
						// Asignacion cliente a un hilo
						threadPool.execute(new ServiceThread(socket, controller));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		this.stop = false;
		this.serverThread.start();
	}

	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", service_port)) {
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
		
		if (this.webService != null) {
			this.endPoint.stop();
		}
	}
}
