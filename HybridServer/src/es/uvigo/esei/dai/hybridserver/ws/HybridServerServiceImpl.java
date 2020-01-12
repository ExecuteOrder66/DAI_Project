package es.uvigo.esei.dai.hybridserver.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;
import es.uvigo.esei.dai.hybridserver.model.dao.DAO;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.ws.HybridServerService")
public class HybridServerServiceImpl implements HybridServerService {
	private final DAO dao;
	
	public HybridServerServiceImpl(DAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<String> getUuids(String contentType) {
		return dao.getList(contentType);
	}

	@Override
	public String getContent(String uuid, String contentType) throws InvalidPageException {
		return dao.getPage(uuid, contentType);
	}


	@Override
	public String getXSDuuid(String xsltId) {
		// TODO Auto-generated method stub
		return null;
	}
}
