package es.uvigo.esei.dai.hybridserver.controller;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;
import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.model.dao.DAO;
import es.uvigo.esei.dai.hybridserver.ws.HybridServerService;
import es.uvigo.esei.dai.hybridserver.ws.WebServiceConnection;

public class Controller implements DAO{
	private final DAO dao;
	private List<ServerConfiguration> serverConfList = new LinkedList<ServerConfiguration>();
	
	public Controller(DAO dao) {
		this.dao = dao;
	}
	
	public Controller(DAO dao, List<ServerConfiguration> serverConfList) {
		this.dao = dao;
		this.serverConfList = serverConfList;
	}
	
	@Override
	public List<String> getList(String contentType) {
		return dao.getList(contentType);
	}
	
	@Override
	public boolean isPage(String uuid, String contentType) {
		return dao.isPage(uuid, contentType);
	}
	
	@Override
	public String getPage(String uuid, String contentType) throws InvalidPageException {
		String content = this.dao.getPage(uuid, contentType);
		if(content == null) {
			try {
				WebServiceConnection ws = new WebServiceConnection(this.serverConfList);
				List<HybridServerService> hybridServerServiceList = ws.getServers();
				for(HybridServerService server : hybridServerServiceList) {
					content = server.getContent(uuid, contentType);
					if(content != null) {
						return content;
					}
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return content;
	}
	
	@Override
	public String addPage(String xsdUuid, String content, String contentType) throws SQLException, InvalidPageException {
		return this.dao.addPage(xsdUuid, content, contentType);
	}
	
	@Override
	public String deletePage(String uuid, String contentType) throws SQLException {
		return this.dao.deletePage(uuid, contentType);
	}
	
	public String getXSDContent(String xsltId) throws InvalidPageException {
		return this.dao.getXSDContent(xsltId);
	}
		
}
