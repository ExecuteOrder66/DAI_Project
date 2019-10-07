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

public class ServiceThread implements Runnable {

	private Socket clientSocket;
	private Controller controller;

	public ServiceThread(Socket client, Controller control) {
		this.clientSocket = client;
		this.controller = control;
	}

	@Override
	public void run() {
		try (Socket socket = this.clientSocket) {
			try {

				// http://localhost:8888/html
				// http://localhost:8888/html?uuid=6df1047e-cf19-4a83-8cf3-38f5e53f7725
				Reader reader = new InputStreamReader(socket.getInputStream());
				HTTPRequest request = new HTTPRequest(reader);
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();

				if (request.getResourceName().equals("html")) { // comprobar error 400
					if (request.getMethod().equals(HTTPRequestMethod.GET)) {
						if (request.getResourceParameters().isEmpty()) {
							response.setContent(controller.getIndex());
						} else {
							String uuid = request.getResourceParameters().get("uuid");
							if (controller.isPage(uuid)) { // comprobar error 404
								response.setContent(controller.getPage(uuid));
							} else {
								String error404 = "<html><head></head><body>ERROR 404: page not found</body></html>";
								response.setStatus(HTTPResponseStatus.forCode(404));
								response.setContent(error404);
							}
						}

					} else {
						if (request.getMethod().equals(HTTPRequestMethod.POST)) {
							controller.addPage(request.getResourceParameters().get("html"));
						}
						if (request.getMethod().equals(HTTPRequestMethod.DELETE)) {
							String uuid = request.getResourceParameters().get("uuid");
							if (controller.deletePage(uuid) != null) {
								response.setContent(
										"<html><head></head><body>Borrada la pagina: " + uuid + "</body></html>");
							} else {
								String error404 = "<html><head></head><body>ERROR 404: page not found</body></html>";
								response.setStatus(HTTPResponseStatus.forCode(404));
								response.setContent(error404);
							}
						}
					}
					response.print(writer); // Enviamos respuesta
				} else {
					String error400 = "<html><head></head><body>ERROR 400: bad request</body></html>";
					response.setStatus(HTTPResponseStatus.forCode(400));
					response.setContent(error400);
					response.print(writer);
				}
			} catch (HTTPParseException eHTTP) {
				// ERROR PARSEO HTTP
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();
				String error400 = "<html><head></head><body>ERROR 400: bad request</body></html>";
				
				response.setStatus(HTTPResponseStatus.forCode(400));
				response.setContent(error400);
				response.print(writer); // Enviamos respuesta
				
				eHTTP.printStackTrace();
			} catch (IOException e) {
				// ERROR 500
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();
				String error500 = "<html><head></head><body>ERROR 500: Internal Server Error</body></html>";
				
				response.setStatus(HTTPResponseStatus.forCode(500));
				response.setContent(error500);
				response.print(writer); // Enviamos respuesta
				
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
