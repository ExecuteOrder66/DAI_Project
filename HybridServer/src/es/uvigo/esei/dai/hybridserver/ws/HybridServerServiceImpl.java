package es.uvigo.esei.dai.hybridserver.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.ws.HybridServerService")
public class HybridServerServiceImpl implements HybridServerService {

	@Override
	public List<String> getHTMLuuids() {
		List<String> list = new LinkedList<>();
		String query = "SELECT uuid FROM HTML";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					while (result.next()) {
						list.add(result.getString("uuid"));
					}
					return list;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> getXMLuuids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getXSDuuids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getXSLTuuids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHTMLContent(String htmlId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLContent(String xmlId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXSDContent(String xsdId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXSLTContent(String xsltId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXSDuuid(String xsltId) {
		// TODO Auto-generated method stub
		return null;
	}
}
