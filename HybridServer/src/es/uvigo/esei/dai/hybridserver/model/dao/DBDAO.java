package es.uvigo.esei.dai.hybridserver.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.InvalidHtmlPageException;

public class DBDAO implements DAO{

	private final String db_url; 
	private final String db_user;
	private final String db_password;

	public DBDAO (String url, String user, String passwd){
		this.db_url = url;
		this.db_user = user;
		this.db_password = passwd;
	}
	
	@Override
	public List<String> getList() {
		List<String> list = new LinkedList<>();
		String query = "SELECT uuid FROM HTML";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
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
	public boolean isPage(String uuid) {
		String query = "SELECT * FROM HTML WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1,  uuid);
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					if(result == null) {
						return false;
					} else {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getPage(String uuid) throws InvalidHtmlPageException{
		String query = "SELECT * FROM HTML WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1,  uuid);
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					// 5. Visualización de resultado
					if(result.next()) {
						return result.getString("content");
					} else {
						throw new InvalidHtmlPageException("Error al recuperar el contenido");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String addPage(String content) throws SQLException {
		//Generar uuid aleatorio
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		
		String query = "INSERT INTO HTML VALUES (?, ?)";
		
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1,  uuid);
				statement.setString(2,  content);
				int result = statement.executeUpdate();
				if(result == 1) {
					return uuid;
				} else {
					throw new RuntimeException("Erro al insertar el contenido");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	public String deletePage(String uuid) throws SQLException {
		String query = "DELETE FROM HTML WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1,  uuid);
				statement.executeUpdate();
				return uuid;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
