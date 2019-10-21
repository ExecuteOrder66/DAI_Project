package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.uvigo.esei.dai.hybridserver.controller.Controller;
import es.uvigo.esei.dai.hybridserver.model.dao.DBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.MemoryDAO;

public class HybridServer {
	private int service_port;
	private int max_thread;
	private Thread serverThread;
	private ExecutorService threadPool; 
	private boolean stop;
	
	private Controller controller;
	
	public HybridServer() {
		this.controller = new Controller(new DBDAO("jdbc:mysql://localhost:3306/hstestdb", 
				"hsdb", "hsdbpass"));
	}
	
	public HybridServer(Map<String, String> pages) {
		this.service_port = 8888;
		this.max_thread = 50;
		this.controller = new Controller(new MemoryDAO(pages));
	}
	
	public HybridServer(Properties properties) {
		this.max_thread = Integer.parseInt(properties.getProperty("numClients"));
		this.service_port = Integer.parseInt(properties.getProperty("port"));
		
		this.controller = new Controller(new DBDAO(properties.getProperty("db.url"), 
				properties.getProperty("db.user"), properties.getProperty("db.password")));
	}

	public int getPort() {
		return service_port;
	}
	
	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(service_port)) {
					threadPool = Executors.newFixedThreadPool(max_thread);
					while (true) {
						Socket socket = serverSocket.accept();
						if (stop) break;
						//Asignacion cliente a un hilo
						//threadPool.execute(new ServiceThread(socket, pages));	
						threadPool.execute(new ServiceThread(socket, controller));		
					}
				} catch (IOException e) {
					//Capturar aqui las IOException? Si las capturamos aqui se rompe el bucle while true, pero 
					//si no, sera un bucle infinito
					e.printStackTrace();
				}
				
				
				
				
				/*try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {
					while (true) {
						try (Socket socket = serverSocket.accept()) {
							if (stop) break;
							// http://localhost:8888/html
							// http://localhost:8888/html?uuid=6df1047e-cf19-4a83-8cf3-38f5e53f7725
							Reader reader = new InputStreamReader(socket.getInputStream());
							HTTPRequest request = new HTTPRequest(reader);
							Writer writer = new OutputStreamWriter(socket.getOutputStream());
							HTTPResponse response = new HTTPResponse();
							
							/*BufferedReader bfr = new BufferedReader(reader);
							String data = bfr.readLine();
							while(data != null) {
								System.out.println(data);
								data = bfr.readLine();
							}*/
							
							/*if(request.getResourceName().equals("html")) { //comprobar error 400
								if(request.getMethod().equals(HTTPRequestMethod.GET)) {
									if(request.getResourceParameters().isEmpty()) {
										response.setContent(controller.getIndex());	
									} else {
										String uuid = request.getResourceParameters().get("uuid");
										if(controller.isPage(uuid)) {	//comprobar error 404
											response.setContent(controller.getPage(uuid));
										} else {
											String error404 = "<html><head></head><body>ERROR 404: page not found</body></html>";
											//response.setStatus(HTTPResponseStatus.forCode(404));
											response.setContent(error404);
										}		
									}
								} else {
									if(request.getMethod().equals(HTTPRequestMethod.POST)) {
										System.out.println("POST");
										//controller.addPage(request.getContent());
										//System.out.println(request.getResourceParameters().toString());
										controller.addPage(request.getResourceParameters().get("html"));
									}
								}
							} else {
								String error400 = "<html><head></head><body>ERROR 400: bad request</body></html>";
								response.setStatus(HTTPResponseStatus.forCode(400));
								response.setContent(error400);
							}
							response.print(writer);

							// Responder al cliente
							/*HTTPResponse response = new HTTPResponse();
							response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
							response.setStatus(HTTPResponseStatus.S200);
							response.setContent(controller.getPage(uuid));					
							Writer writer = new OutputStreamWriter(socket.getOutputStream());
							response.print(writer);
								
							/*String html = "<html><head></head><body>Hybrid Server</body></html>";
							OutputStream clientOutput = socket.getOutputStream();
								    
							clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
							clientOutput.write(("Content-Length: " + html.length() + "\r\n\r\n").getBytes());
							clientOutput.write(html.getBytes());
							
							clientOutput.flush();*/
						/*} catch (HTTPParseException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}*/
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
	}
}
