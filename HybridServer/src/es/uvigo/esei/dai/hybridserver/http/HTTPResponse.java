package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
	HTTPResponseStatus status;
	String version = "";
	String content = "";
	String contentType = "";
	Map<String, String> parameters = new LinkedHashMap<>();
	
	public HTTPResponse() {
		// por defecto devuelve un estado 200 OK
		this.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		this.setStatus(HTTPResponseStatus.forCode(200));
	}

	public HTTPResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		if(contentType.equals("html")) {
			this.contentType = MIME.TEXT_HTML.getMime();
		} else if(contentType.equals("xml") || contentType.equals("xsd") || contentType.equals("xslt")) {
			this.contentType = MIME.APPLICATION_XML.getMime();
		}
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public String putParameter(String name, String value) {
		String  previousValue = this.parameters.put(name, value);
		return previousValue;
	}

	public boolean containsParameter(String name) {
		return this.parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		String previousValue = this.parameters.remove(name);
		return previousValue;
	}

	public void clearParameters() {
		this.parameters.clear();
	}

	public List<String> listParameters() {
		List<String> parametersList = new LinkedList<String>();
		for (String key : this.parameters.keySet()) {
			parametersList.add(key + ": " + parameters.get(key));
        }
		return parametersList;
	}

	public void print(Writer writer) throws IOException {
		writer.write(this.getVersion());
		writer.write(" ");
		String code = Integer.toString(this.getStatus().getCode());
		writer.write(code);
		writer.write(" ");
		writer.write(this.getStatus().getStatus());
		writer.write("\r\n");
		if (!this.listParameters().isEmpty()) {
			writer.write(this.listParameters().toString());
			writer.write("\r\n");
		}
		if (this.content.length() != 0) {
			writer.write("Content-Type: " + getContentType());
			writer.write("\r\n");
			writer.write("Content-Length: " + this.content.length());
			writer.write("\r\n\r\n");
			writer.write(this.getContent());
		} else {
			writer.write("\r\n");
		}
		writer.flush();
	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();

		try {
			this.print(writer);
		} catch (IOException e) {
		}

		return writer.toString();
	}
}
