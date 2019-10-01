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
	Map<String, String> parameters = new LinkedHashMap<String,String>();
	
	public HTTPResponse() {
		
	}

	public HTTPResponseStatus getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	public String putParameter(String name, String value) {
		// TODO Auto-generated method stub
		String  previousValue = this.parameters.put(name, value);
		return previousValue;
	}

	public boolean containsParameter(String name) {
		// TODO Auto-generated method stub	
		return this.parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		// TODO Auto-generated method stub
		String previousValue = this.parameters.remove(name);
		return previousValue;
	}

	public void clearParameters() {
		this.parameters.clear();
	}

	public List<String> listParameters() {
		// TODO Auto-generated method stub
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
		if(!this.listParameters().isEmpty()) {
			writer.write(this.listParameters().toString());
			writer.write("\r\n");
		}
		if(this.content.length() != 0) {			
			writer.write("Content-Length: " + this.content.length());
			writer.write("\r\n\r\n");
			writer.write(this.getContent());
		} else {
			writer.write("\r\n");
		}
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
