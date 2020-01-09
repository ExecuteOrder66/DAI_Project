package es.uvigo.esei.dai.hybridserver.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.InvalidPageException;

public class DBDAO implements DAO {

	private final String db_url;
	private final String db_user;
	private final String db_password;

	public DBDAO(String url, String user, String passwd) {
		this.db_url = url;
		this.db_user = user;
		this.db_password = passwd;
	}

	@Override
	public List<String> getList(String contentType) {
		List<String> list = new LinkedList<>();
		String query = "SELECT uuid FROM " + contentType.toUpperCase();
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
	public boolean isPage(String uuid, String contentType) {
		String query = "SELECT * FROM " + contentType.toUpperCase() + " WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					if (result == null) {
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
	public String getPage(String uuid, String contentType) throws InvalidPageException {
		String query = "SELECT * FROM " + contentType.toUpperCase() + " WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					if (result.next()) {
						return result.getString("content");
					} else {
						throw new InvalidPageException("Error al recuperar el contenido");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String addPage(String xsdUuid, String content, String contentType)
			throws SQLException, InvalidPageException {
		// Generar uuid aleatorio
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();

		if (!contentType.equals("xslt")) {
			String query = "INSERT INTO " + contentType.toUpperCase() + " VALUES (?, ?)";
			// 1. Conexión a la base de datos
			try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
				// 2. Creación de la consulta
				try (PreparedStatement statement = connection.prepareStatement(query)) {
					statement.setString(1, uuid);
					statement.setString(2, content);
					// 3. Ejecución de la consulta
					int result = statement.executeUpdate();
					if (result == 1) {
						return uuid;
					} else {
						throw new RuntimeException("Erro al insertar el contenido");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		} else {
			if (isPage(xsdUuid, "xsd")) {
				String query = "INSERT INTO " + contentType.toUpperCase() + " VALUES (?, ?, ?)";
				// 1. Conexión a la base de datos
				try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
					// 2. Creación de la consulta
					try (PreparedStatement statement = connection.prepareStatement(query)) {
						statement.setString(1, uuid);
						statement.setString(2, content);
						statement.setString(3, xsdUuid);
						// 3. Ejecución de la consulta
						int result = statement.executeUpdate();
						if (result == 1) {
							return uuid;
						} else {
							throw new RuntimeException("Erro al insertar el contenido");
						}
					} catch (SQLException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			} else {
				throw new InvalidPageException("Error al recuperar el contenido");
			}
		}

	}

	public String deletePage(String uuid, String contentType) throws SQLException {
		String query = "DELETE FROM " + contentType.toUpperCase() + " WHERE uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				// 3. Ejecución de la consulta
				statement.executeUpdate();
				return uuid;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	public String getXSDContent(String xsltId) throws InvalidPageException {
		String query = "SELECT XSD.content FROM XSD, XSLT WHERE XSD.uuid=XSLT.xsd AND XSLT.uuid LIKE ?";
		// 1. Conexión a la base de datos
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			// 2. Creación de la consulta
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, xsltId);
				// 4. Ejecución de la consulta
				try (ResultSet result = statement.executeQuery()) {
					if (result.next()) {
						String xsdContent = result.getString("content");
						return xsdContent;

					} else {
						throw new InvalidPageException("Error al recuperar el contenido");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
