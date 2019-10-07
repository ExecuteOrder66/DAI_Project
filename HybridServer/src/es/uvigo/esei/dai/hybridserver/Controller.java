package es.uvigo.esei.dai.hybridserver;



public class Controller {
	DAO memory;
	
	public Controller(DAO memory) {
		// TODO Auto-generated constructor stub
		this.memory = memory;
	}
	
	public String getIndex() {
		return this.memory.getIndex();
	}
	
	public String getPage(String uid) {
		return this.memory.getPage(uid);
	}
	
	public void addPage(String value) {
		this.memory.addPage(value);
	}
	
	public boolean isPage(String uuid) {
		return this.memory.isPage(uuid);
	}
		
}
