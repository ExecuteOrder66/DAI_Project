package es.uvigo.esei.dai.hybridserver.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;

@WebService()
//@SOAPBinding(style = Style.RPC)
public interface HybridServerService {
	@WebMethod
	public List<String> getUuidList(String contentType);
	
	@WebMethod
	public String getContent(String uuid, String contentType) throws InvalidPageException;
	
	@WebMethod
	public String getXSD(String xsltId);
}
