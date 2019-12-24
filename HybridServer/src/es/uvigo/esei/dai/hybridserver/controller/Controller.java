package es.uvigo.esei.dai.hybridserver.controller;

import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;
import es.uvigo.esei.dai.hybridserver.model.dao.DAO;

public class Controller implements DAO{
	private final DAO dao;
	
	public Controller(DAO dao) {
		this.dao = dao;
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
		return this.dao.getPage(uuid, contentType);
	}
	
	@Override
	public String addPage(String xsdUuid, String content, String contentType) throws SQLException, InvalidPageException {
		return this.dao.addPage(xsdUuid, content, contentType);
	}
	
	@Override
	public String deletePage(String uuid, String contentType) throws SQLException {
		return this.dao.deletePage(uuid, contentType);
	}
		
}
