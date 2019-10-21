package es.uvigo.esei.dai.hybridserver.controller;

import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.InvalidHtmlPageException;
import es.uvigo.esei.dai.hybridserver.model.dao.DAO;

public class Controller implements DAO{
	private final DAO dao;
	
	public Controller(DAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<String> getList() {
		return dao.getList();
	}
	
	@Override
	public boolean isPage(String uuid) {
		return dao.isPage(uuid);
	}
	
	@Override
	public String getPage(String uid) throws InvalidHtmlPageException {
		return this.dao.getPage(uid);
	}
	
	@Override
	public String addPage(String content) throws SQLException {
		return this.dao.addPage(content);
	}
	
	@Override
	public String deletePage(String uuid) throws SQLException {
		return this.dao.deletePage(uuid);
	}
		
}
