package es.uvigo.esei.dai.hybridserver.ws;

import java.util.List;

import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.model.dao.DAO;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.ws.HybridServerService", targetNamespace = "http://hybridserver.dai.esei.uvigo.es/", serviceName = "HybridServerService")
public class HybridServerServiceImpl implements HybridServerService {
	private final DAO dao;

	public HybridServerServiceImpl(DAO dao) {
		this.dao = dao;
	}

	@Override
	public List<String> getUuidList(String contentType) {
		return dao.getList(contentType);
	}

	@Override
	public String getContent(String uuid, String contentType) {
		return dao.getPage(uuid, contentType);
	}

	@Override
	public String getXSD(String xsltId) {
		return dao.getXSDuuid(xsltId);
	}
}
