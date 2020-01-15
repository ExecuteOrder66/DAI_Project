package es.uvigo.esei.dai.hybridserver.model.dao;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryDAO implements DAO {
	Map<String, String> pages = new LinkedHashMap<String, String>();
	List<String> list = new LinkedList<String>();

	public MemoryDAO(Map<String, String> pages) {
		this.pages = pages;
	}

	@Override
	public List<String> getList(String contentType) {
		for (String key : this.pages.keySet()) {
			this.list.add(key);
		}
		return this.list;
	}

	@Override
	public boolean isPage(String uuid, String contentType) {
		boolean result = false;
		for (String key : this.pages.keySet()) {
			if (key.equals(uuid)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public String getPage(String uuid, String contentType) {
		return this.pages.get(uuid);
	}

	@Override
	public String addPage(String xsdUuid, String value, String contentType) {
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
		pages.put(uuid, value);
		list.add(uuid);
		return uuid;
	}

	@Override
	public String deletePage(String uuid, String contentType) {
		String toret = this.pages.remove(uuid);
		if (toret != null) {
			int elemIndex = this.list.indexOf(uuid);
			if (elemIndex != -1)
				this.list.remove(elemIndex);
		}
		return toret; // Devuelve el valor de la clave borrada si tiene exito, null si no existia
	}

	@Override
	public String getXSDuuid(String xsltId) {
		return null;
	}

}
