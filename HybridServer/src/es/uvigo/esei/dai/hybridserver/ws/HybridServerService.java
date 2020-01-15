package es.uvigo.esei.dai.hybridserver.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService()
public interface HybridServerService {
	@WebMethod
	public List<String> getUuidList(String contentType);

	@WebMethod
	public String getContent(String uuid, String contentType);

	@WebMethod
	public String getXSD(String xsltId);
}
