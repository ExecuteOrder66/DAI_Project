package es.uvigo.esei.dai.hybridserver.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "HybridServerService", targetNamespace = "http://hybridserver.dai.esei.uvigo.es/")
public interface HybridServerService {
	@WebMethod
	public List<String> getHTMLuuids();
	
	@WebMethod
	public List<String> getXMLuuids();
	
	@WebMethod
	public List<String> getXSDuuids();
	
	@WebMethod
	public List<String> getXSLTuuids();
	
	@WebMethod
	public String getHTMLContent(String htmlId);
	
	@WebMethod
	public String getXMLContent(String xmlId);
	
	@WebMethod
	public String getXSDContent(String xsdId);
	
	@WebMethod
	public String getXSLTContent(String xsltId);
	
	@WebMethod
	public String getXSDuuid(String xsltId);
}
