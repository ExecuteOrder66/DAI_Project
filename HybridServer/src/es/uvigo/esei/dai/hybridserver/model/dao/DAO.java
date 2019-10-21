package es.uvigo.esei.dai.hybridserver.model.dao;

import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.InvalidHtmlPageException;

public interface DAO {
	public List<String> getList();
	public boolean isPage(String uuid);
	public String getPage(String uuid) throws InvalidHtmlPageException;
	public String addPage(String content) throws SQLException;
	public String deletePage(String uuid) throws SQLException;
}
