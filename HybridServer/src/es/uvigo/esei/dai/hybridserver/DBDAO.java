package es.uvigo.esei.dai.hybridserver;

public class DBDAO implements DAO {
	/*
	 * numClients=50 port=8888 
	 * db.url=jdbc:mysql://localhost:3306/hstestdb
	 * db.user=hsdb db.password=hsdbpass
	 */
	private String url;
	private String user;
	private String password;

	public DBDAO(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.password = pass;
	}

	@Override
	public String getIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPage(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addPage(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPage(String uuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String deletePage(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
