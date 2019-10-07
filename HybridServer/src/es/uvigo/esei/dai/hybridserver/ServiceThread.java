package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ServiceThread implements Runnable{

	private Socket clientSocket;
	private Controller controller;
	
	public ServiceThread(Socket client, Controller control) {
		this.clientSocket = client;
		this.controller = control;
	}
	
	@Override
	public void run() {
		try(Socket socket = this.clientSocket){
			System.out.println("Dentro Run()");
			// http://localhost:8888/html
			// http://localhost:8888/html?uuid=6df1047e-cf19-4a83-8cf3-38f5e53f7725
			Reader reader = new InputStreamReader(socket.getInputStream());
			System.out.println("input");
			HTTPRequest request = new HTTPRequest(reader);
			System.out.println("request");
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			System.out.println("write");
			HTTPResponse response = new HTTPResponse();
			
			System.out.println("Hola");

			
			if(request.getResourceName().equals("html")) { //comprobar error 400
				if(request.getMethod().equals(HTTPRequestMethod.GET)) {
					if(request.getResourceParameters().isEmpty()) {
						response.setContent(controller.getIndex());	
					} else {
						String uuid = request.getResourceParameters().get("uuid");
						if(controller.isPage(uuid)) {	//comprobar error 404
							response.setContent(controller.getPage(uuid));
						} else {
							String error404 = "<html><head></head><body>ERROR 404: page not found</body></html>";
							response.setStatus(HTTPResponseStatus.forCode(404));
							response.setContent(error404);
						}		
					}
					response.print(writer);
				} else {
					if(request.getMethod().equals(HTTPRequestMethod.POST)) {
						controller.addPage(request.getContent());	//hacer que devuelva un valor en caso de error?
					}
				}
			} else {
				String error400 = "<html><head></head><body>ERROR 400: bad request</body></html>";
				response.setStatus(HTTPResponseStatus.forCode(400));
				response.setContent(error400);
				response.print(writer);
			}
			

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
		}catch(HTTPParseException eHTTP) {
			//ERROR PARSEO HTTP
			eHTTP.printStackTrace();
		}
		catch(IOException e) {
			//Duda si capturar aqui la IOException o si capturarla en el serverThread 
			//o si capturarla y relanzarla
			//ERROR 500
			e.printStackTrace();
		}
	}
}
