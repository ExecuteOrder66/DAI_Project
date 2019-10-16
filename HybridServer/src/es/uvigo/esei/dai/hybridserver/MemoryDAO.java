package es.uvigo.esei.dai.hybridserver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryDAO implements DAO {
	Map<String, String> pages = new LinkedHashMap<String, String>();
	List<String> index = new LinkedList<String>();

	public MemoryDAO(Map<String, String> pages) {
		this.pages = pages;
		for (String key : this.pages.keySet()) {
			index.add("<a href=\"html?uuid=" + key + "\">" + key + "</a><br>");
		}
	}

	public String getIndex() {
		Iterator<String> it = this.index.iterator();
		String content = "";
		while (it.hasNext()) {
			content = content.concat(it.next());
		}
		return content;
	}

	public String getPage(String uid) {
		return this.pages.get(uid);
	}

	public String addPage(String value) {
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		pages.put(uuid, value);
		index.add("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a><br>");
		return uuid;
	}

	public String deletePage(String uid) {
		String toret = this.pages.remove(uid);
		if(toret != null) {
			int elemIndex = this.index.indexOf("<a href=\"html?uuid=" + uid + "\">" + uid + "</a><br>");
			if (elemIndex != -1) 
				this.index.remove(elemIndex);			
		}
		return toret; // Devuelve el valor de la clave borrada si tiene exito, null si no existia
	}

	public boolean isPage(String uuid) {
		boolean result = false;
		for (String key : this.pages.keySet()) {
			if (key.equals(uuid)) {
				result = true;
			}
		}
		return result;
	}
}
