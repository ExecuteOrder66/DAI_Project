package es.uvigo.esei.dai.hybridserver.model.dao;

import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;

public interface DAO {
	public List<String> getList(String contentType);
	public boolean isPage(String uuid, String contentType);
	public String getPage(String uuid, String contentType);
	public String addPage(String xsdUuid, String content, String contentType) throws SQLException, InvalidPageException;
	public String deletePage(String uuid, String contentType) throws SQLException;
	public String getXSDuuid(String xsltId);
}
