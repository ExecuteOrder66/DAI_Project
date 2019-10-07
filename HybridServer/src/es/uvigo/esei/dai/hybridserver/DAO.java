package es.uvigo.esei.dai.hybridserver;

public interface DAO {
	
	public String getIndex();
	
	public String getPage(String uid);
	
	public int addPage(String value);
	
	public boolean isPage(String uuid);
}
