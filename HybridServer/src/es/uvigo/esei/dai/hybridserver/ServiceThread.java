package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.Socket;

import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.hybridserver.controller.Controller;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ServiceThread implements Runnable {

	private Socket clientSocket;
	private Controller controller;

	public ServiceThread(Socket client, Controller controller) {
		this.clientSocket = client;
		this.controller = controller;
	}

	@Override
	public void run() {
		try (Socket socket = this.clientSocket) {
			try {
				Reader reader = new InputStreamReader(socket.getInputStream());
				HTTPRequest request = new HTTPRequest(reader);
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();

				if (request.getResourceChain().equals("/")) {
					 response.setContent(
					 "<html><head></head><body>Boveda Martinez, Alejandro</br>Curras Ferradas, Rodrigo</body></html>");
					//response.setContent("<html><head></head><body>Hybrid Server</body></html>");
				} else {
					String contentType = request.getResourceName();
					if (contentType.equals("html") || contentType.equals("xml") || contentType.equals("xsd")
							|| contentType.equals("xslt")) {
						switch (request.getMethod()) {
						case GET:
							if (request.getResourceParameters().isEmpty()) {
								List<String> list = controller.getList(contentType);
								Iterator<String> it = list.iterator();
								String content = "<html><head></head><body>";
								while (it.hasNext()) {
									String link = it.next();
									content = content.concat(
											"<a href=\"" + contentType + "?uuid=" + link + "\">" + link + "</a><br>");
								}
								content = content.concat("</body></html>");
								response.setContentType("html");
								response.setContent(content);
							} else {
								if (request.getResourceParameters().containsKey("uuid")
										&& request.getResourceParameters().containsKey("xslt")) {
									String xmlId = request.getResourceParameters().get("uuid");
									String xsltId = request.getResourceParameters().get("xslt");
									String xmlContent = controller.getPage(xmlId, contentType);
									String xsltContent = controller.getPage(xsltId, "xslt");
									if (xmlContent != null && xsltContent != null) {
										String xsdUUID = controller.getXSDuuid(xsltId);
										String xsdContent = controller.getPage(xsdUUID, "xsd");

										StringReader xmlReader = new StringReader(xmlContent);
										StringReader xsltReader = new StringReader(xsltContent);
										StringReader xsdReader = new StringReader(xsdContent);

										StringReader input = new StringReader(controller.getPage(xmlId, contentType));
										StringWriter output = new StringWriter();

										// validar xml con el xsd
										try {
											SchemaFactory schemaFactory = SchemaFactory
													.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
											Schema schema = schemaFactory.newSchema(new StreamSource(xsdReader));
											Validator validator = schema.newValidator();
											validator.validate(new StreamSource(xmlReader));
										} catch (SAXException e) {
											String error400 = "<html><head></head><body>ERROR 400: Bad request</body></html>";
											response.setStatus(HTTPResponseStatus.forCode(400));
											response.setContent(error400);
										}
										// transformar xml
										TransformerFactory tFactory = TransformerFactory.newInstance();
										Transformer transformer = tFactory.newTransformer(new StreamSource(xsltReader));
										transformer.transform(new StreamSource(input), new StreamResult(output));

										response.setContentType("html");
										response.setContent(output.getBuffer().toString());
									} else {
										String error404 = "<html><head></head><body>ERROR 404: Page not found</body></html>";
										response.setStatus(HTTPResponseStatus.forCode(404));
										response.setContent(error404);
									}
								} else {
									if (request.getResourceParameters().containsKey("uuid")) {
										String uuid = request.getResourceParameters().get("uuid");
										String content = controller.getPage(uuid, contentType);
										if (content != null) {
											response.setContentType(contentType);
											response.setContent(content);
										} else {
											String error404 = "<html><head></head><body>ERROR 404: Page not found</body></html>";
											response.setStatus(HTTPResponseStatus.forCode(404));
											response.setContent(error404);
										}
									}
								}
							}
							break;
						case POST:
							if (request.getMethod().equals(HTTPRequestMethod.POST)) {
								if (request.getResourceParameters().containsKey("html")
										|| request.getResourceParameters().containsKey("xml")
										|| request.getResourceParameters().containsKey("xsd")
										|| (request.getResourceParameters().containsKey("xslt")
												&& request.getResourceParameters().containsKey("xsd"))) {
									String uuid = controller.addPage(request.getResourceParameters().get("xsd"),
											request.getResourceParameters().get(contentType), contentType);
									response.setContentType(contentType);
									response.setContent("<html><head></head><body><a href=\"" + contentType + "?uuid="
											+ uuid + "\">" + uuid + "</a></body></html>");
								} else {
									String error400 = "<html><head></head><body>ERROR 400: Bad request</body></html>";
									response.setStatus(HTTPResponseStatus.forCode(400));
									response.setContent(error400);
								}
							}
							break;
						case DELETE:
							if (request.getMethod().equals(HTTPRequestMethod.DELETE)) {
								String uuid = request.getResourceParameters().get("uuid");
								if (controller.deletePage(uuid, contentType) != null) {
									response.setContent(
											"<html><head></head><body>Deleted page: " + uuid + "</body></html>");
								} else {
									String error404 = "<html><head></head><body>ERROR 404: Page not found</body></html>";
									response.setStatus(HTTPResponseStatus.forCode(404));
									response.setContent(error404);
								}
							}
							break;
						default:
							break;
						}
					} else {
						String error400 = "<html><head></head><body>ERROR 400: Bad request</body></html>";
						response.setStatus(HTTPResponseStatus.forCode(400));
						response.setContent(error400);
					}
				}
				response.print(writer);
			} catch (HTTPParseException eHTTP) {
				// ERROR EN EL PARSEO HTTP
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();
				String error400 = "<html><head></head><body>ERROR 400: Bad Request</body></html>";

				response.setStatus(HTTPResponseStatus.forCode(400));
				response.setContent(error400);
				response.print(writer); // Enviamos respuesta

				eHTTP.printStackTrace();
			} catch (InvalidPageException e) {
				// ERROR AL NO LOCALIZAR LA PAGINA
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();
				String error404 = "<html><head></head><body>ERROR 404: Not Found</body></html>";

				response.setStatus(HTTPResponseStatus.forCode(404));
				response.setContent(error404);
				response.print(writer); // Enviamos respuesta
			} catch (IOException e) {
				// ERROR SERVIDOR
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				HTTPResponse response = new HTTPResponse();
				String error500 = "<html><head></head><body>ERROR 500: Internal Server Error</body></html>";

				response.setStatus(HTTPResponseStatus.forCode(500));
				response.setContent(error500);
				response.print(writer); // Enviamos respuesta

				e.printStackTrace();
			} catch (RuntimeException e) {
				// ERROR SERVIDOR
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