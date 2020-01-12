package es.uvigo.esei.dai.hybridserver.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;

@WebService(name = "HybridServerService", targetNamespace = "http://hybridserver.dai.esei.uvigo.es/")
public interface HybridServerService {
	@WebMethod
	public List<String> getUuids(String contentType);
	
	@WebMethod
	public String getContent(String uuid, String contentType) throws InvalidPageException;
	
	@WebMethod
	public String getXSDuuid(String xsltId);
}
