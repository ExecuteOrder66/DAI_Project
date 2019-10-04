package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URLDecoder;

public class HTTPRequest {
	HTTPRequestMethod method;
	String resourceChain = "";
	String httpVersion = "";
	String resourceName = "";
	String[] resourcePath = new String[0];
	Map<String, String> resourceParameters = new LinkedHashMap<String, String>();
	Map<String, String> headerParameters = new LinkedHashMap<String, String>();
	String content = null;
	int contentLength = 0;
	
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		BufferedReader br = new BufferedReader(reader);
		String data = br.readLine();
		String[] dataLineRscChainParams;
		String[] dataLineRscChainParam;
		
		//leer y analizar primera línea
		String[] dataLine = data.split(" ");
		if(dataLine.length < 3) {
			throw new HTTPParseException();
		} else {
			this.method = HTTPRequestMethod.valueOf(dataLine[0]);
			this.resourceChain = dataLine[1];	//cadena de busq
			this.httpVersion =dataLine[2];
			if(dataLine[1].equals("/")) {
	            this.resourceChain = dataLine[1];
	        } else {
	        	String[] dataLineRscChain = dataLine[1].split("\\?");	//separar nombre de busq de param de busq
	    		if(dataLineRscChain.length == 1) {	//comprobar si la cadena de busq contiene un nombre de busq pero no param de busq
	    			this.resourceChain = dataLineRscChain[0]; 
	                this.resourceName = dataLineRscChain[0].replaceFirst("/", "");//almacenar resourceName eliminando el slash de comienzo de línea
	                this.resourcePath = resourceName.split("/"); 
	    		} else {
	    			if(dataLineRscChain.length == 2) {	//comprobar si la cadena de busq contiene nombre de busq y param de búsqueda
	        			this.resourceName = dataLineRscChain[0].replaceFirst("/", "");
	        			this.resourcePath = resourceName.split("/");
	        			dataLineRscChainParams = dataLineRscChain[1].split("&");
	        			for(int i = 0; i < dataLineRscChainParams.length; i++){
	                    	dataLineRscChainParam = dataLineRscChainParams[i].split("=");
	                    	resourceParameters.put(dataLineRscChainParam[0], dataLineRscChainParam[1]);
	                    }
	        		}
	    		}	
	        }
			//leer y analizar el resto de líneas
			data = br.readLine();
	        while(!data.equals("")){ 
	            dataLine = data.split(": ");
	            if(dataLine.length < 2) {
	            	throw new HTTPParseException();
	            } else {
	            	headerParameters.put(dataLine[0], dataLine[1]);
		            data = br.readLine(); 
	            }  
	        }
	        this.content = br.readLine();
	        if(content != null) {
	        	this.contentLength = content.length();
	        	String type = headerParameters.get("Content-Type");
	        	if(type != null && type.startsWith("application/x-www-form-urlencoded")) {
	        		content = URLDecoder.decode(content, "UTF-8");
	        	}
	        	dataLineRscChainParams = content.split("&");
	            for(int i = 0; i < dataLineRscChainParams.length; i++){
	            	dataLineRscChainParam = dataLineRscChainParams[i].split("=");
	            	resourceParameters.put(dataLineRscChainParam[0], dataLineRscChainParam[1]);
	            }
	        }
		}
	}
	
	
	public HTTPRequestMethod getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	public String getResourceChain() {
		// TODO Auto-generated method stub
		return this.resourceChain;
	}

	public String[] getResourcePath() {
		// TODO Auto-generated method stub
		return resourcePath;
	}

	public String getResourceName() {
		// TODO Auto-generated method stub
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		// TODO Auto-generated method stub
		return resourceParameters;
	}

	public String getHttpVersion() {
		// TODO Auto-generated method stub
		return this.httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		// TODO Auto-generated method stub
		return this.headerParameters;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return contentLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}
		
		return sb.toString();
	}
}
